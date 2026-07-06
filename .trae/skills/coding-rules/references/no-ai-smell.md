# 注释去 AI 味

> 核心立场：注释给代码读者看，**不是 AI 工作记录**。
> v2 增加 harness 集成（ai_smell_check.sh 机械兜底）。

---

## 为什么重要

AI 写代码时，容易把**对话上下文**、**决策过程**、**AI 自己的思考**当注释塞进去。这些对代码读者毫无价值，只会污染代码，暴露"这是 AI 写的"。

---

## 严禁的 7 类 AI 味

| 类型 | 反例 |
|------|------|
| 讨论过程 | "经过讨论，我们决定用 A" |
| 重构历史 | "重构自原来的 X 方法" |
| 用户上下文 | "用户要求优化这里"、"Issue #123 要求" |
| AI 思考 | "我加了 null 检查"、"我注意到这里" |
| 主观情绪 | "这里很 tricky"、"神奇的修复" |
| 未来规划 | "未来可能改成 Y"、"暂时这么写" |
| 第一人称 | 任何"我/我们/AI" |

## 应该写的 8 类代码味

| 类型 | 例子 |
|------|------|
| what | "异步加载用户列表，完成后回调" |
| why（纯技术）| "CarPropertyManager.getProperty 阻塞，必须 IO 线程" |
| 使用约束 | "必须在主线程调用" |
| KDoc/Javadoc | 公共 API 完整文档 |
| 算法 | "快排，平均 O(n log n)" |
| 魔法数字 | "// 5 秒超时(车机 ANR 阈值)" |
| 兼容性 | "// Android 12+ 才有此 API" |
| 副作用 | "会清空缓存" |

---

## 对照表

| AI 味 ❌ | 代码味 ✓ |
|---------|---------|
| "优化：从 HandlerThread 改为协程" | （删，代码自己说明）|
| "用户反馈原来太卡了" | （删）|
| "经过讨论决定采用 Dispatchers.IO" | "Car API 同步阻塞，必须 IO 线程" |
| "这里我加了 early return 优化" | （删）|
| "静默失败，太丑了但是能 work" | "失败时记录日志并重抛" |
| "未来可能改成 Y" | （删）|

---

## 前后对比实例

### ❌ AI 味
```kotlin
// 优化：从 HandlerThread 改为协程
// 用户反馈原来太卡了
// 经过讨论决定采用 Dispatchers.IO
suspend fun loadData(): Data {
    // 加载前先检查缓存
    // 这里我加了 early return 优化
    cache.get(key)?.let { return it }
    return withContext(Dispatchers.IO) {
        try { api.fetch(key) }
        catch (e: Exception) {
            // 静默失败，太丑了但是能 work
            Log.e("TAG", "failed", e)
            throw e
        }
    }
}
```

### ✓ 干净
```kotlin
suspend fun loadData(): Data {
    cache.get(key)?.let { return it }
    return withContext(Dispatchers.IO) {
        try { api.fetch(key) }
        catch (e: Exception) {
            Log.e(TAG, "fetch failed for $key", e)
            throw e
        }
    }
}
```

---

## 自检清单（写完每条注释逐一查）

```
□ 包含对话/讨论/用户/团队? → 删
□ 包含历史/重构/变更? → 删
□ 包含"我"/"我们"/"AI"? → 改写或删
□ 是 AI 思考过程? → 删
□ 是主观判断? → 改客观或删
□ 是"未来可能..."? → 删
□ 描述代码做什么(what)? → 保留
□ 描述纯技术原因(why)? → 保留
□ 标注使用约束? → 保留
□ 是 KDoc/Javadoc? → 保留
```
**任一不过 → 修改或删除。**

> **默认删除原则**：拿不准这条注释该不该留，**默认删**。好注释是稀缺的，冗余注释是常见的。

---

## harness 兜底（v2 新增）

即使 agent 跳过自检，`_harness/scripts/ai_smell_check.sh` 会在 Edit 后机械扫描以下短语：
- 我们决定 / 经过讨论 / 用户要求 / 用户反馈
- 我加了 / 我注意到 / 我修改了 / 我优化了
- 重构自 / 改版 / 暂时这么写 / 未来可能
- 很 tricky / 神奇的 / workaround

命中则报错，agent 必须删除后重新 Edit。

### 配置
```yaml
harness:
  ai_smell_check: true   # skills.config.yaml
```

---

## 显式配置（保留某类注释）

如团队允许保留"重构历史"（用于追溯），在 `skills.config.yaml` 加：
```yaml
ai_smell:
  allow:
    - refactor_history   # 允许"重构自 XX"
```
脚本启动时读 config，跳过允许的类型。（默认全禁）

---

## 设计依据

- 注释是代码的一部分，进 git，长期存在
- AI 味注释暴露"这是 AI 写的"，降低代码可信度
- 冗余注释比没注释更糟（误导读者）
- 机械扫描比靠 agent 自觉可靠（弱模型会忘）
