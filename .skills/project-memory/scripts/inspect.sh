#!/usr/bin/env bash
# ============================================================
# inspect.sh — Android 项目结构扫描 v2
# 修复 v1 问题：
#   1. 修复 AUTOMOMOTIVE_HITS typo 导致车机评分恒为 0
#   2. 所有 find/grep 加 head 截断防爆 token
#   3. 统一错误处理（grep 无匹配返回 1 不误退出）
#   4. 读 skills.config.yaml 的阈值
# 用法: bash inspect.sh [项目根路径，默认当前目录]
# ============================================================
set -uo pipefail
# 注意: 不用 set -e，因为 grep 无匹配返回 1 是正常的

PROJECT_ROOT="${1:-.}"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_FILE="$SCRIPT_DIR/../../../skills.config.yaml"
MAX_CAT=30  # 默认值

# 读 config
if [[ -f "$CONFIG_FILE" ]]; then
  VAL=$(grep -E 'inspect_max_files_cat:' "$CONFIG_FILE" | head -1 | sed 's/.*: *//; s/#.*//')
  [[ -n "$VAL" ]] && MAX_CAT="$VAL"
fi

cd "$PROJECT_ROOT" 2>/dev/null || { echo "ERROR: 目录不存在 $PROJECT_ROOT"; exit 1; }

echo "=== PROJECT_TYPE ==="
if [[ -f "build/aosp_index.xml" || -d "frameworks" ]]; then
  echo "type: AOSP_INTREE"
elif [[ -f "settings.gradle" || -f "settings.gradle.kts" ]]; then
  echo "type: MULTI_MODULE_GRADLE"
elif [[ -f "build.gradle" || -f "build.gradle.kts" ]]; then
  echo "type: SINGLE_APP"
else
  echo "type: LEGACY_UNKNOWN"
fi

echo ""
echo "=== GRADLE_MODULES ==="
if [[ -f "settings.gradle.kts" ]]; then
  grep -oE 'include\("[^"]+"\)' settings.gradle.kts 2>/dev/null | sed 's/include("//;s/")//' | head -50
elif [[ -f "settings.gradle" ]]; then
  grep -oE "include '[^']+'" settings.gradle 2>/dev/null | sed "s/include '//;s/'//" | head -50
else
  echo "(无 settings.gradle)"
fi

echo ""
echo "=== MANIFEST ==="
# 找主 manifest，限制结果数防爆 token
MANIFEST=$(find . -name "AndroidManifest.xml" -not -path "*/build/*" -not -path "*/.git/*" 2>/dev/null | head -5)
if [[ -n "$MANIFEST" ]]; then
  echo "manifest_files:"
  echo "$MANIFEST"
  echo ""
  echo "key_declarations:"
  # 只看第一个 manifest 的关键声明
  echo "$MANIFEST" | head -1 | xargs grep -hoE '(package="[^"]+"|sharedUserId="[^"]+"|<uses-permission[^>]+>)' 2>/dev/null | head -20
else
  echo "(未找到 AndroidManifest.xml)"
fi

echo ""
echo "=== AIDL ==="
find . -name "*.aidl" -not -path "*/build/*" -not -path "*/.git/*" 2>/dev/null | head -30

echo ""
echo "=== AUTOMOTIVE_FEATURES ==="
# 修复 v1: AUTOMOMOTIVE_HITS typo → AUTOMOTIVE_HITS
AUTOMOTIVE_HITS=0
# 检测车机特征关键词，每个限制匹配数
for kw in "android.car" "CarPropertyManager" "CarSensorManager" "CarAppService" "Launcher3" "SettingsLib" "HVAC" "Cluster"; do
  CNT=$(grep -rl "$kw" --include="*.kt" --include="*.java" --include="*.gradle" --include="*.kts" . 2>/dev/null | grep -v "/build/" | head -20 | wc -l | tr -d ' ')
  AUTOMOTIVE_HITS=$((AUTOMOTIVE_HITS + CNT))
  [[ "$CNT" -gt 0 ]] && echo "  $kw: $CNT files"
done
echo "automotive_score: $AUTOMOTIVE_HITS"
if [[ "$AUTOMOTIVE_HITS" -gt 5 ]]; then
  echo "verdict: LIKELY_AUTOMOTIVE"
elif [[ "$AUTOMOTIVE_HITS" -gt 0 ]]; then
  echo "verdict: MAYBE_AUTOMOTIVE"
else
  echo "verdict: NOT_AUTOMOTIVE"
fi

echo ""
echo "=== CODE_STATS ==="
# 统计行数，限制 find 范围防爆 token
KT_FILES=$(find . -name "*.kt" -not -path "*/build/*" -not -path "*/.git/*" 2>/dev/null | head -500 | wc -l | tr -d ' ')
JAVA_FILES=$(find . -name "*.java" -not -path "*/build/*" -not -path "*/.git/*" 2>/dev/null | head -500 | wc -l | tr -d ' ')
CPP_FILES=$(find . -name "*.cpp" -o -name "*.cc" -not -path "*/build/*" 2>/dev/null | head -500 | wc -l | tr -d ' ')

# 行数统计：只取前 MAX_CAT 个文件 cat，避免大项目爆 token
KT_LINES=0
if [[ "$KT_FILES" -gt 0 ]]; then
  # 修复 v1: find -exec cat 无截断 → 加 head 限制
  KT_LINES=$(find . -name "*.kt" -not -path "*/build/*" -not -path "*/.git/*" 2>/dev/null | head -"$MAX_CAT" | xargs wc -l 2>/dev/null | tail -1 | awk '{print $1}')
  [[ -z "$KT_LINES" ]] && KT_LINES=0
fi
JAVA_LINES=0
if [[ "$JAVA_FILES" -gt 0 ]]; then
  JAVA_LINES=$(find . -name "*.java" -not -path "*/build/*" -not -path "*/.git/*" 2>/dev/null | head -"$MAX_CAT" | xargs wc -l 2>/dev/null | tail -1 | awk '{print $1}')
  [[ -z "$JAVA_LINES" ]] && JAVA_LINES=0
fi

TOTAL=$((KT_LINES + JAVA_LINES))
if [[ "$TOTAL" -gt 0 ]]; then
  KT_PCT=$((KT_LINES * 100 / TOTAL))
  JAVA_PCT=$((100 - KT_PCT))
else
  KT_PCT=0; JAVA_PCT=0
fi
echo "kotlin_files: $KT_FILES (sampled ${MAX_CAT} max, lines: $KT_LINES)"
echo "java_files: $JAVA_FILES (sampled ${MAX_CAT} max, lines: $JAVA_LINES)"
echo "cpp_files: $CPP_FILES"
echo "language_ratio: Kotlin ${KT_PCT}% / Java ${JAVA_PCT}%"

echo ""
echo "=== INTENT_FILTERS ==="
echo "$MANIFEST" | head -1 | xargs grep -hoE '<action android:name="[^"]+"' 2>/dev/null | sed 's/<action android:name="//;s/"//' | sort -u | head -20

echo ""
echo "=== PERMISSIONS ==="
echo "$MANIFEST" | head -1 | xargs grep -hoE 'android:name="android.permission.[^"]+"' 2>/dev/null | sed 's/.*\.//' | sort -u | head -30

echo ""
echo "=== ENTRY_HINTS ==="
# 找核心入口类，限制结果数
echo "activities:"
grep -rl "extends.*Activity\|: Activity()" --include="*.kt" --include="*.java" . 2>/dev/null | grep -v "/build/" | head -10
echo "services:"
grep -rl "extends.*Service\|: Service()" --include="*.kt" --include="*.java" . 2>/dev/null | grep -v "/build/" | head -10
echo "receivers:"
grep -rl "extends.*BroadcastReceiver\|: BroadcastReceiver()" --include="*.kt" --include="*.java" . 2>/dev/null | grep -v "/build/" | head -10

echo ""
echo "=== DONE ==="
