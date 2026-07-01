#!/usr/bin/env bash
# ============================================================
# ai_smell_check.sh — AI 味注释检查
# 扫描改动文件，找"我们决定""用户要求""我加了"等 AI 味短语
# 修复 v1：v1 只在文档里列禁用词，靠 agent 自觉；本脚本机械扫描
# 用法：bash ai_smell_check.sh [文件路径]
#       不传文件则检查 git diff 涉及的所有文件
# ============================================================
set -uo pipefail

RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'; NC='\033[0m'

# AI 味短语清单（来自 no-ai-smell.md，机器可判）
AI_SMELL_PATTERNS=(
  '我们决定'
  '经过讨论'
  '用户要求'
  '用户反馈'
  '用户说'
  '我加了'
  '我注意到'
  '我修改了'
  '我优化了'
  '重构自'
  '改版'
  'v[0-9]\.[0-9] 改'
  '暂时这么写'
  '未来可能'
  '以后会改成'
  '很 tricky'
  '神奇的'
  'workaround'
  'TODO 后续'  # 区分正常 TODO
  'AI 生成'
  'agent 建议'
)

# 构建 grep 正则
PATTERN=$(printf '%s\\|' "${AI_SMELL_PATTERNS[@]}")
PATTERN="${PATTERN%\\|}"

# 确定检查文件
if [[ -n "${1:-}" ]]; then
  FILES=("$1")
else
  if git rev-parse --is-inside-work-tree >/dev/null 2>&1; then
    mapfile -t FILES < <(git diff HEAD --name-only -- '*.kt' '*.java' 2>/dev/null)
  else
    echo "${YELLOW}[SKIP]${NC} 非 git 仓库且未指定文件"
    exit 0
  fi
fi

if [[ ${#FILES[@]} -eq 0 ]]; then
  echo "${GREEN}[AI_SMELL OK]${NC} 无改动文件可检查"
  exit 0
fi

TOTAL_HITS=0
REPORT=""

for f in "${FILES[@]}"; do
  [[ -f "$f" ]] || continue
  # 只检查改动行（git diff 的 + 行），避免误报历史代码
  if git rev-parse --is-inside-work-tree >/dev/null 2>&1; then
    ADDED_LINES=$(git diff HEAD -- "$f" 2>/dev/null | grep -E '^\+' | grep -vE '^\+{3}')
  else
    ADDED_LINES=$(cat "$f")
  fi
  [[ -z "$ADDED_LINES" ]] && continue

  # 逐个模式匹配
  while IFS=: read -r match; do
    [[ -z "$match" ]] && continue
    LINENO=$(echo "$match" | cut -d: -f1)
    LINE=$(echo "$match" | cut -d: -f2-)
    # 提取命中关键词
    for p in "${AI_SMELL_PATTERNS[@]}"; do
      if echo "$LINE" | grep -qE "$p"; then
        REPORT+="  $f:$LINENO  [命中: $p]\n    $LINE\n"
        ((TOTAL_HITS++))
        break
      fi
    done
  done < <(echo "$ADDED_LINES" | grep -nE "$PATTERN" 2>/dev/null || true)
done

if [[ "$TOTAL_HITS" -gt 0 ]]; then
  echo "${RED}[AI_SMELL] 发现 ${TOTAL_HITS} 处 AI 味注释${NC}"
  printf "%b" "$REPORT"
  echo ""
  echo "${YELLOW}注释只写代码本身(what/why/约束)，删除对话/历史/AI思考痕迹。${NC}"
  echo "详见 coding-rules/references/no-ai-smell.md"
  exit 1
else
  echo "${GREEN}[AI_SMELL OK]${NC} 注释干净（检查 ${#FILES[@]} 文件）"
  exit 0
fi
