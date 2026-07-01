#!/usr/bin/env bash
# ============================================================
# diff_guard.sh — 最小改动范围守卫
# 检查 git diff 是否有"用户没要求的改动"
# 修复 v1：v1 全靠 agent 自觉跑 git diff 自检，本脚本机械执行
# ============================================================
set -uo pipefail

RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'; NC='\033[0m'

# 只检查已暂存 + 工作区的改动
if ! git rev-parse --is-inside-work-tree >/dev/null 2>&1; then
  echo "${YELLOW}[SKIP]${NC} 非 git 仓库，跳过 diff_guard"
  exit 0
fi

DIFF=$(git diff HEAD --unified=0 2>/dev/null)
if [[ -z "$DIFF" ]]; then
  echo "${GREEN}[OK]${NC} 无改动"
  exit 0
fi

VIOLATIONS=0
REPORT=""

# --- 检查 1：纯格式/空行改动（无实质内容变化）---
# 提取所有 + 行（去掉 +++ 文件头和 diff 元信息）
while IFS= read -r line; do
  # 跳过 diff/hunk 头
  [[ "$line" =~ ^(diff|index|---|\+\+\+|@@) ]] && continue
  # 只看新增行
  [[ "$line" =~ ^\+ ]] || continue
  content="${line:1}"
  # 去空格后为空 = 纯空行改动
  trimmed="${content// /}"
  if [[ -z "$trimmed" ]]; then
    REPORT+="  - 纯空行改动（可能是无关格式调整）\n"
    ((VIOLATIONS++))
  fi
done <<< "$DIFF"

# --- 检查 2：import 行改动（常见"顺手清理"）---
IMPORT_CHANGES=$(echo "$DIFF" | grep -E '^[+-]import ' | grep -vE '^[+-]{3}' || true)
if [[ -n "$IMPORT_CHANGES" ]]; then
  IMPORT_COUNT=$(echo "$IMPORT_CHANGES" | wc -l | tr -d ' ')
  if [[ "$IMPORT_COUNT" -gt 2 ]]; then
    REPORT+="  - import 改动 ${IMPORT_COUNT} 行（检查是否顺手清理了无关 import）\n"
    ((VIOLATIONS++))
  fi
fi

# --- 检查 3：改动文件数（防止一次改太多）---
CHANGED_FILES=$(git diff HEAD --name-only 2>/dev/null | wc -l | tr -d ' ')
if [[ "$CHANGED_FILES" -gt 5 ]]; then
  REPORT+="  - 改动文件数 ${CHANGED_FILES}（>5，确认是否都在用户要求范围内）\n"
  ((VIOLATIONS++))
fi

# --- 输出 ---
if [[ "$VIOLATIONS" -gt 0 ]]; then
  echo "${RED}[DIFF_GUARD] 发现 ${VIOLATIONS} 项可疑改动${NC}"
  echo "改动文件："
  git diff HEAD --stat 2>/dev/null | head -10
  echo ""
  printf "%b" "$REPORT"
  echo ""
  echo "${YELLOW}请逐一确认：这些改动都是用户明确要求的吗？${NC}"
  echo "若有无关注动，请 git checkout 撤回后再提交。"
  exit 1
else
  echo "${GREEN}[DIFF_GUARD OK]${NC} 改动范围正常（${CHANGED_FILES} 文件）"
  exit 0
fi
