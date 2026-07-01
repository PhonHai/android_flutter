#!/usr/bin/env bash
# ============================================================
# sk — skills 套件统一入口（v3.1）
# agent 和用户都只需记这一个命令，不用记一堆脚本路径。
#
# 用法:
#   sk index                  显示能力清单 + 统一触发词（agent 进项目先读）
#   sk inspect [root]         全量扫描项目，生成 .agent/ 三件套
#   sk sync [range]           增量同步（基于 git diff）
#   sk diff-guard [pre|post]  最小改动校验（hook 用，也可手动）
#   sk ai-smell <file>        检查文件 AI 味注释
#   sk anchor [map_file]      校验 .agent/project-map.md 锚点引用
#   sk commit-safety          扫暂存区有无敏感文件（git-commit 用）
#   sk update-zcode           把源同步到 ZCode plugin（bump 版本号）
#   sk update-source [dir]    把外部源目录同步到 ~/.skills-kit/ 或 ~/.mavis/skills-kit/
#   sk help                   帮助
#
# 路径自适应（项目级优先，全局回退）:
#   harness 脚本: .skills/_harness/scripts → ~/.skills/_harness/scripts → ~/.mavis/skills/_harness/scripts
#   memory 脚本:  project-memory/scripts → ~/.skills-kit/project-memory/scripts → ~/.mavis/skills/project-memory/scripts
#   源目录:       ~/.skills-kit/ → ~/.mavis/skills-kit/ → $SCRIPT_DIR/../..
# ============================================================
set -uo pipefail

GREEN='\033[0;32m'; YELLOW='\033[1;33m'; RED='\033[0;31m'; NC='\033[0m'
info()  { printf "${GREEN}[INFO]${NC} %s\n" "$1"; }
warn()  { printf "${YELLOW}[WARN]${NC} %s\n" "$1"; }
error() { printf "${RED}[ERROR]${NC} %s\n" "$1" >&2; }

# 脚本自身所在目录（用于回退定位源）
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" 2>/dev/null && pwd || echo "$HOME/.skills/_harness/scripts")"

# ---- 定位脚本（项目级优先，全局回退，含 Mavis 路径）----
find_script() {
  local name="$1"
  # 1) 项目级 harness 脚本
  for base in ".skills/_harness/scripts"; do
    [[ -f "$base/$name" ]] && { echo "$base/$name"; return; }
  done
  # 2) 全局 harness（兼容标准 / Mavis）
  for base in "$HOME/.skills/_harness/scripts" "$HOME/.mavis/skills/_harness/scripts"; do
    [[ -f "$base/$name" ]] && { echo "$base/$name"; return; }
  done
  # 3) 全局 skill 脚本
  for base in ".skills/scripts" \
             "$HOME/.skills-kit/project-memory/scripts" \
             "$HOME/.mavis/skills-kit/project-memory/scripts" \
             "$HOME/.mavis/skills/project-memory/scripts" \
             "$HOME/.skills/scripts"; do
    [[ -f "$base/$name" ]] && { echo "$base/$name"; return; }
  done
  echo ""
}

# ---- 定位源目录（含 Mavis 路径）----
find_kit_root() {
  if [[ -n "${SKILLS_KIT_ROOT:-}" && -f "$SKILLS_KIT_ROOT/skills.config.yaml" ]]; then
    echo "$SKILLS_KIT_ROOT"; return
  fi
  # 优先级: 环境变量 > ~/.skills-kit/ > ~/.mavis/skills-kit/ > 脚本旁
  for base in "$HOME/.skills-kit" "$HOME/.mavis/skills-kit" "$SCRIPT_DIR/../.."; do
    [[ -f "$base/skills.config.yaml" ]] && { echo "$base"; return; }
  done
  echo ""
}

# ============================================================
# 子命令
# ============================================================
case "${1:-help}" in

  index)
    cat << 'EOF'
=== Skills 套件能力清单（v3.1，8 个 skill）===

【用户统一触发词——所有平台通用，不用记各平台的重命名】
  "梳理项目"   → project-memory (explore 全量扫描)
  "同步项目"   → project-memory (sync 增量更新)
  "提交"       → git-commit (规范提交)
  "懒人模式"   → lazy-build (full 档，默认)
  "懒人lite"   → lazy-build (lite 档)
  "懒人ultra"  → lazy-build (ultra 档)
  "停止懒人"   → lazy-build 关闭

【自动加载（用户无需触发）】
  - code-locate : 输入带 .kt/.java/.aidl 等代码标识符时自动查 .agent/project-map.md
  - arch-rules  : 改 Launcher/Manifest/AIDL/Car API 时自动加载对应规则
  - coding-rules: 任何 Edit/Write .kt/.java 时自动加载(风格+最小改动+去AI味)
  - confirm-first: 任何修改代码任务自动输出方案卡等用户同意
  - lazy-build  : 准备写新代码时自动加载(精简阶梯)
  - verify-loop : 改完代码自动跑构建+对照原始需求验证
  - git-commit  : verify 通过后用户说"提交"触发

【agent 调用脚本（统一 sk 命令）】
  sk inspect [root]          全量扫描
  sk sync [range]            增量同步
  sk diff-guard [pre|post]   最小改动校验
  sk ai-smell <file>         AI 味注释检查
  sk anchor [map]            锚点引用校验
  sk commit-safety           暂存区敏感文件扫描

【完整链路】
  project-memory(懂项目) → code-locate(找代码) → arch-rules(架构约束)
  → confirm-first(提方案) → lazy-build(写多少) → coding-rules(怎么写)
  → verify-loop(验证) → git-commit(提交) → 提示 sync

【关键文件】
  .agent/project-map.md     项目代码地图(锚点结构)
  .agent/project-skill.md   项目级 skill 行为
  .agent/progress.json      会话间记忆(JSON)
  .skills/skills.config.yaml 项目级配置(覆盖全局)

【Mavis 路径兼容】
  sk 自动识别 ~/.skills-kit/ 和 ~/.mavis/skills-kit/ 两个源目录位置。
  Mavis 用户：把 skill 装到 ~/.mavis/skills/ 即可，sk 命令能自动定位。
EOF
    ;;

  inspect)
    SCRIPT="$(find_script inspect.sh)"
    [[ -z "$SCRIPT" ]] && { error "找不到 inspect.sh"; exit 1; }
    bash "$SCRIPT" "${2:-.}"
    ;;

  sync)
    SCRIPT="$(find_script sync.sh)"
    [[ -z "$SCRIPT" ]] && { error "找不到 sync.sh"; exit 1; }
    bash "$SCRIPT" "${2:-}"
    ;;

  diff-guard)
    SCRIPT="$(find_script diff_guard.sh)"
    [[ -z "$SCRIPT" ]] && { error "找不到 diff_guard.sh"; exit 1; }
    bash "$SCRIPT" "${2:-post}"
    ;;

  ai-smell)
    [[ -z "${2:-}" ]] && { echo "用法: sk ai-smell <文件路径>"; exit 1; }
    SCRIPT="$(find_script ai_smell_check.sh)"
    [[ -z "$SCRIPT" ]] && { error "找不到 ai_smell_check.sh"; exit 1; }
    bash "$SCRIPT" "$2"
    ;;

  anchor)
    SCRIPT="$(find_script anchor_check.sh)"
    [[ -z "$SCRIPT" ]] && { error "找不到 anchor_check.sh"; exit 1; }
    bash "$SCRIPT" "${2:-.agent/project-map.md}"
    ;;

  commit-safety)
    # git-commit 提交前扫暂存区敏感文件
    if ! git rev-parse --is-inside-work-tree >/dev/null 2>&1; then
      echo "[SKIP] 非 git 仓库"; exit 0
    fi
    STAGED="$(git diff --staged --name-only 2>/dev/null)"
    [[ -z "$STAGED" ]] && { echo "[OK] 暂存区为空，无敏感文件"; exit 0; }
    DANGER="$(echo "$STAGED" | grep -iE '\.(keystore|jks|pem|p12|pk8)$|credentials|secrets|\.env$|local\.properties|platform\.|\.apk$|\.aab$' || true)"
    if [[ -n "$DANGER" ]]; then
      echo "[DANGER] 暂存区含敏感文件，禁止提交："
      echo "$DANGER" | sed 's/^/  - /'
      echo "取消暂存: git restore --staged <file>"
      exit 1
    fi
    echo "[OK] 暂存区无敏感文件"
    ;;

  # ============================================================
  # update-zcode: 把源同步到 ZCode plugin（自动 bump 版本）
  # ============================================================
  update-zcode)
    echo "========== sk update-zcode =========="
    echo ""

    # 1. 定位源
    KIT_ROOT="$(find_kit_root)"
    if [[ -z "$KIT_ROOT" ]]; then
      error "找不到源目录。把源放 ~/.skills-kit/ 或设 SKILLS_KIT_ROOT=<路径>"
      exit 1
    fi
    info "源目录: $KIT_ROOT"

    # 2. 同步全局 harness + config（让 sk 自己和 harness 脚本全局可用）
    info "同步全局 harness → ~/.skills/_harness/"
    mkdir -p "$HOME/.skills/_harness/scripts"
    cp -r "$KIT_ROOT/_harness/scripts/"*.sh "$HOME/.skills/_harness/scripts/" 2>/dev/null
    chmod +x "$HOME/.skills/_harness/scripts/"*.sh
    if [[ ! -f "$HOME/.skills/skills.config.yaml" ]]; then
      cp "$KIT_ROOT/skills.config.yaml" "$HOME/.skills/skills.config.yaml"
      sed -i '' 's/^platform: .*/platform: zcode/' "$HOME/.skills/skills.config.yaml" 2>/dev/null
    fi
    info "  ✓ 全局 harness 已更新"

    # 3. 读当前 ZCode plugin 版本，bump patch
    PLUGIN_BASE="$HOME/.zcode/cli/plugins/cache/zcode-plugins-official/android-skills-harness"
    CUR_VER="$(ls "$PLUGIN_BASE" 2>/dev/null | sort -V | tail -1)"
    [[ -z "$CUR_VER" ]] && CUR_VER="1.0.0"
    # bump patch: 1.2.0 → 1.2.1
    IFS='.' read -r MAJOR MINOR PATCH <<< "$CUR_VER"
    NEW_VER="$MAJOR.$MINOR.$((PATCH + 1))"
    info "版本: $CUR_VER → $NEW_VER"

    NEW_PLUGIN="$PLUGIN_BASE/$NEW_VER"
    rm -rf "$NEW_PLUGIN"
    mkdir -p "$NEW_PLUGIN/.zcode-plugin" "$NEW_PLUGIN/skills" "$NEW_PLUGIN/hooks"

    # 4. 复制全部 skill（动态发现，不硬编码）
    info "复制 skills..."
    for skill_dir in "$KIT_ROOT"/*/; do
      skill_name="$(basename "$skill_dir")"
      [[ "$skill_name" == _* ]] && continue  # 跳过 _platform / _harness
      [[ ! -f "$skill_dir/SKILL.md" ]] && continue
      cp -r "$skill_dir" "$NEW_PLUGIN/skills/$skill_name"
      # 清理 .DS_Store
      find "$NEW_PLUGIN/skills/$skill_name" -name ".DS_Store" -delete 2>/dev/null
      echo "  ✓ $skill_name"
    done

    # INDEX.md（如有）
    [[ -f "$KIT_ROOT/INDEX.md" ]] && cp "$KIT_ROOT/INDEX.md" "$NEW_PLUGIN/INDEX.md"
    [[ -f "$KIT_ROOT/MIGRATION.md" ]] && cp "$KIT_ROOT/MIGRATION.md" "$NEW_PLUGIN/MIGRATION.md"

    # 5. plugin.json
    cat > "$NEW_PLUGIN/.zcode-plugin/plugin.json" << EOF
{
  "name": "android-skills-harness",
  "version": "$NEW_VER",
  "description": "Android/车机 agent skills 套件 v3.1 — 8 skill + 统一入口 sk + 懒人精简 + 规范提交。抗降智、可跨平台迁移。",
  "author": { "name": "local" },
  "license": "MIT",
  "skills": "skills"
}
EOF
    info "  ✓ plugin.json (v$NEW_VER)"

    # 6. hooks.json（接入全局 harness）
    cat > "$NEW_PLUGIN/hooks/hooks.json" << 'EOF'
{
  "hooks": {
    "PreToolUse": [
      {
        "matcher": "Edit|Write",
        "hooks": [
          {
            "type": "command",
            "command": "bash ~/.skills/_harness/scripts/diff_guard.sh pre 2>/dev/null || true",
            "async": false
          }
        ]
      }
    ],
    "PostToolUse": [
      {
        "matcher": "Edit|Write",
        "hooks": [
          {
            "type": "command",
            "command": "bash ~/.skills/_harness/scripts/ai_smell_check.sh \"$CLAUDE_FILE_PATH\" 2>/dev/null; bash ~/.skills/_harness/scripts/diff_guard.sh post 2>/dev/null || true",
            "async": false
          }
        ]
      }
    ]
  }
}
EOF
    info "  ✓ hooks.json"

    # 7. 更新 marketplace.json（用 python 安全改 JSON）
    info "更新 marketplace.json..."
    MARKET="$HOME/.zcode/cli/plugins/marketplaces/zcode-plugins-official/marketplace.json"
    python3 << PYEOF
import json
path = "$MARKET"
with open(path) as f:
    d = json.load(f)
found = False
for p in d['plugins']:
    if p['name'] == 'android-skills-harness':
        p['version'] = '$NEW_VER'
        p['cachePath'] = '$NEW_PLUGIN'
        found = True
        break
if not found:
    d['plugins'].append({
        "cachePath": "$NEW_PLUGIN",
        "name": "android-skills-harness",
        "source": "filesystem",
        "version": "$NEW_VER"
    })
d['plugins'].sort(key=lambda x: x['name'])
with open(path, 'w') as f:
    json.dump(d, f, indent=2, ensure_ascii=False)
print("  ✓ marketplace.json 已更新")
PYEOF

    # 8. 重建激活标记
    MARK="$HOME/.zcode/cli/plugins/data/android-skills-harness@zcode-plugins-official"
    rm -rf "$MARK"
    mkdir -p "$MARK"
    info "  ✓ 激活标记已重建"

    # 9. 删除旧版本目录（只保留最新）
    for old in "$PLUGIN_BASE"/*/; do
      old_ver="$(basename "$old")"
      [[ "$old_ver" == "$NEW_VER" ]] && continue
      rm -rf "$old"
      info "  ✓ 删除旧版本 $old_ver"
    done

    echo ""
    echo "========== 完成 =========="
    info "ZCode plugin 已更新到 v$NEW_VER"
    info "下一步: 重启 ZCode，新会话验证"
    echo ""
    echo "  提示: 以后改完源，只需跑一句 → sk update-zcode"
    ;;

  # ============================================================
  # update-source: 把外部源目录同步到 ~/.skills-kit/
  # ============================================================
  update-source)
    SRC="${2:-}"
    [[ -z "$SRC" ]] && { echo "用法: sk update-source <源目录>"; exit 1; }
    [[ ! -d "$SRC" ]] && { error "源目录不存在: $SRC"; exit 1; }
    [[ ! -f "$SRC/skills.config.yaml" ]] && { error "$SRC 不是合法源目录（缺 skills.config.yaml）"; exit 1; }
    # 检测 Mavis 环境：优先装到 ~/.mavis/skills-kit/，否则 ~/.skills-kit/
    if [[ -d "$HOME/.mavis" ]]; then
      DEST="$HOME/.mavis/skills-kit"
      info "检测到 Mavis 环境，源: $SRC → $DEST/"
    else
      DEST="$HOME/.skills-kit"
      info "源: $SRC → $DEST/"
    fi
    rm -rf "$DEST"
    cp -r "$SRC" "$DEST"
    # 清理 .DS_Store
    find "$DEST" -name ".DS_Store" -delete 2>/dev/null
    info "  ✓ 源已更新到 $DEST/"
    info "下一步: sk update-zcode  同步到 ZCode plugin"
    ;;

  help|--help|-h)
    cat << 'EOF'
sk — skills 套件统一入口 (v3.1)

用法:
  sk index                  显示能力清单 + 统一触发词
  sk inspect [root]         全量扫描，生成 .agent/ 三件套
  sk sync [range]           增量同步（基于 git diff）
  sk diff-guard [pre|post]  最小改动校验
  sk ai-smell <file>        AI 味注释检查
  sk anchor [map]           锚点引用校验
  sk commit-safety          暂存区敏感文件扫描
  sk update-zcode           把源同步到 ZCode plugin（bump 版本）
  sk update-source <dir>    把外部源同步到 ~/.skills-kit/ 或 ~/.mavis/skills-kit/
  sk help                   本帮助

用户统一触发词（所有平台通用）:
  "梳理项目"  → 全量扫描
  "同步项目"  → 增量同步
  "提交"      → git-commit
  "懒人模式"  → lazy-build (full)
  "懒人lite"  → lazy-build (lite)
  "懒人ultra" → lazy-build (ultra)
  "停止懒人"  → 关闭 lazy-build
  其他 skill 全自动加载，无需记忆。

路径支持:
  源目录:    ~/.skills-kit/  或  ~/.mavis/skills-kit/
  Harness:   ~/.skills/_harness/scripts/  或  ~/.mavis/skills/_harness/scripts/
  Mavis 用户: skill 装在 ~/.mavis/skills/，sk 命令自动识别。
EOF
    ;;

  *)
    echo "未知命令: $1"
    echo "运行 sk help 查看用法"
    exit 1
    ;;

esac
