#!/usr/bin/env bash
# ============================================================
# anchor_check.sh — 锚点引用校验
# 检查 skill 文档里引用的锚点 ID 在 .agent/project-map.md 里是否存在
# 修复 v1：v1 用 §3.2 等编号引用，章节重排后静默断链；v2 用锚点 + 校验
# 用法：bash anchor_check.sh [.agent/project-map.md 路径]
# ============================================================
set -uo pipefail

RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'; NC='\033[0m'

MAP_FILE="${1:-.agent/project-map.md}"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
KIT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

if [[ ! -f "$MAP_FILE" ]]; then
  echo "${YELLOW}[SKIP]${NC} $MAP_FILE 不存在，跳过锚点校验"
  exit 0
fi

# 提取 .agent/project-map.md 里定义的所有锚点
mapfile -t DEFINED_ANCHORS < <(grep -oE '<a id="[^"]+"></a>' "$MAP_FILE" | sed 's/<a id="//; s/"><\/a>//' || true)
if [[ ${#DEFINED_ANCHORS[@]} -eq 0 ]]; then
  echo "${YELLOW}[WARN]${NC} $MAP_FILE 未定义任何锚点，建议用 v2 模板重新生成"
  exit 0
fi

# 收集 skill 文档里引用的锚点（格式：[文字](#anchor-id) 或 .agent/project-map.md#anchor-id）
BROKEN=0
REPORT=""

while IFS= read -r ref; do
  # 提取锚点名
  anchor=$(echo "$ref" | grep -oE '#[a-z0-9-]+' | head -1 | sed 's/#//')
  [[ -z "$anchor" ]] && continue
  # 检查是否在定义列表里
  found=false
  for d in "${DEFINED_ANCHORS[@]}"; do
    [[ "$d" == "$anchor" ]] && found=true && break
  done
  if ! $found; then
    REPORT+="  断链引用: #$anchor\n"
    ((BROKEN++))
  fi
done < <(grep -rhoE '\(#[a-z0-9-]+\)|.agent/project-map.md#[a-z0-9-]+|§[0-9.]+' "$KIT_ROOT" --include='*.md' 2>/dev/null | sort -u || true)

# 同时检查遗留的 §编号引用（v1 风格，应迁移到锚点）
LEGACY_REFS=$(grep -rhoE '§[0-9]+\.[0-9]*' "$KIT_ROOT" --include='*.md' 2>/dev/null | sort -u || true)
if [[ -n "$LEGACY_REFS" ]]; then
  LEGACY_COUNT=$(echo "$LEGACY_REFS" | wc -l | tr -d ' ')
  echo "${YELLOW}[WARN]${NC} 发现 ${LEGACY_COUNT} 处遗留 §编号引用（建议迁移到锚点 ID）："
  echo "$LEGACY_REFS" | head -10
fi

if [[ "$BROKEN" -gt 0 ]]; then
  echo "${RED}[ANCHOR] 发现 ${BROKEN} 处断链锚点${NC}"
  printf "%b" "$REPORT"
  echo "${YELLOW}请修正 .agent/project-map.md 或更新引用。${NC}"
  exit 1
else
  echo "${GREEN}[ANCHOR OK]${NC} 锚点引用正常（${#DEFINED_ANCHORS[@]} 个锚点）"
  exit 0
fi
