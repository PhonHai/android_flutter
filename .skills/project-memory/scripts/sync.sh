#!/usr/bin/env bash
# ============================================================
# sync.sh — git diff 增量分析 v2
# 改进 v1:
#   1. 输出锚点区块映射（#sec-xxx）而非 §编号
#   2. 读 skills.config.yaml 阈值
#   3. 读 progress.json 的 last_commit 作为 diff 起点
#   4. 统一错误处理
# 用法: bash sync.sh [diff范围，默认 HEAD~1..HEAD]
# ============================================================
set -uo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_FILE="$SCRIPT_DIR/../../../skills.config.yaml"
PROGRESS_FILE="${PWD}/.agent/progress.json"
LARGE_THRESHOLD=20

# 读 config
if [[ -f "$CONFIG_FILE" ]]; then
  VAL=$(grep -E 'sync_large_change_files:' "$CONFIG_FILE" | head -1 | sed 's/.*: *//; s/#.*//')
  [[ -n "$VAL" ]] && LARGE_THRESHOLD="$VAL"
fi

# 读 progress.json 的 last_commit
DIFF_RANGE="${1:-}"
if [[ -z "$DIFF_RANGE" ]]; then
  if [[ -f "$PROGRESS_FILE" ]]; then
    LAST_COMMIT=$(grep -oE '"last_commit"[[:space:]]*:[[:space:]]*"[^"]*"' "$PROGRESS_FILE" | head -1 | sed 's/.*"last_commit"[[:space:]]*:[[:space:]]*"//;s/"$//')
    if [[ -n "$LAST_COMMIT" ]]; then
      DIFF_RANGE="${LAST_COMMIT}..HEAD"
    else
      DIFF_RANGE="HEAD~1..HEAD"
    fi
  else
    DIFF_RANGE="HEAD~1..HEAD"
  fi
fi

# 检查是否 git 仓库
if ! git rev-parse --is-inside-work-tree >/dev/null 2>&1; then
  echo "ERROR: 非 git 仓库"
  exit 1
fi

echo "=== DIFF_RANGE ==="
echo "range: $DIFF_RANGE"

echo ""
echo "=== AFFECTED_FILES ==="
git diff "$DIFF_RANGE" --name-status 2>/dev/null | head -50

echo ""
echo "=== CHANGE_STATS ==="
STATS=$(git diff "$DIFF_RANGE" --name-status 2>/dev/null)
ADDED=$(echo "$STATS" | grep -cE '^A' || true)
MODIFIED=$(echo "$STATS" | grep -cE '^M' || true)
DELETED=$(echo "$STATS" | grep -cE '^D' || true)
RENAMED=$(echo "$STATS" | grep -cE '^R' || true)
TOTAL=$((ADDED + MODIFIED + DELETED + RENAMED))
echo "added: $ADDED"
echo "modified: $MODIFIED"
echo "deleted: $DELETED"
echo "renamed: $RENAMED"
echo "total: $TOTAL"

echo ""
echo "=== AFFECTED_AREAS ==="
# 统计各类文件改动（用 grep -c，无匹配返回 0 不报错）
MANIFEST_CHANGES=$(echo "$STATS" | grep -cE 'AndroidManifest\.xml' || true)
AIDL_CHANGES=$(echo "$STATS" | grep -cE '\.aidl$' || true)
GRADLE_CHANGES=$(echo "$STATS" | grep -cE '\.(gradle|kts)$' || true)
KT_CHANGES=$(echo "$STATS" | grep -cE '\.kt$' || true)
JAVA_CHANGES=$(echo "$STATS" | grep -cE '\.java$' || true)
XML_CHANGES=$(echo "$STATS" | grep -cE '\.xml$' || true)

echo "manifest_changes: $MANIFEST_CHANGES"
echo "aidl_changes: $AIDL_CHANGES"
echo "gradle_changes: $GRADLE_CHANGES"
echo "kotlin_changes: $KT_CHANGES"
echo "java_changes: $JAVA_CHANGES"
echo "xml_changes: $XML_CHANGES"

# 检测车机相关改动
CAR_API_CHANGES=$(git diff "$DIFF_RANGE" --name-only 2>/dev/null | xargs grep -l "android.car\|CarPropertyManager" 2>/dev/null | head -10 | wc -l | tr -d ' ')
echo "car_api_changes: $CAR_API_CHANGES"

echo ""
echo "=== RECOMMENDED_UPDATES ==="
# 改进 v1: 输出锚点区块而非 §编号
echo "anchor_updates:"
[[ "$GRADLE_CHANGES" -gt 0 ]] && echo "  - #sec-modules (Gradle 模块变化)"
[[ $((KT_CHANGES + JAVA_CHANGES)) -gt 0 ]] && echo "  - #sec-entry (核心入口可能有新增类)"
[[ "$AIDL_CHANGES" -gt 0 ]] && echo "  - #sec-ipc (AIDL 接口变化)"
[[ "$MANIFEST_CHANGES" -gt 0 ]] && echo "  - #sec-permission / #sec-entry (权限或入口变化)"
[[ "$CAR_API_CHANGES" -gt 0 ]] && echo "  - #sec-ipc (Car API 引用变化)"
echo "  - #sec-changelog (总是追加变更日志)"

echo ""
echo "=== LARGE_CHANGE_WARNING ==="
if [[ "$TOTAL" -gt "$LARGE_THRESHOLD" ]]; then
  echo "WARNING: 改动 $TOTAL 文件 > 阈值 $LARGE_THRESHOLD，建议重跑 explore 模式而非增量更新"
else
  echo "OK: 改动量正常 ($TOTAL <= $LARGE_THRESHOLD)"
fi

echo ""
echo "=== CURRENT_COMMIT ==="
git rev-parse --short HEAD 2>/dev/null

echo ""
echo "=== DONE ==="
echo "下一步: agent 按上面 anchor_updates 只 Edit 对应区块，然后追加 #sec-changelog"
