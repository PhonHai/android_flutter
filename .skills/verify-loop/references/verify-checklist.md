# 验证检查清单（详细）

> verify-loop 的详细检查项。弱模型靠逐项打勾完成验证，不依赖主观判断。

---

## 一、构建验证

### 命令
```bash
# 按 config 的 verify_loop.build_cmd，默认:
./gradlew assembleDebug 2>&1 | tail -50
```

### 检查项
```
□ 构建退出码 = 0?
□ 输出里有 "BUILD SUCCESSFUL"?
□ 输出里没有 warning 被忽略?(尤其是 deprecation/unchecked)
□ 读了完整输出，不只看最后一行?
```

### 常见失败处理
| 错误 | 原因 | 修复 |
|------|------|------|
| `Compilation error` | 语法/类型错 | 读错误行，修正 |
| `Unresolved reference` | import 缺失 | 加 import（仅必要的）|
| `Manifest merger failed` | 权限/属性冲突 | 检查 manifest 改动 |
| `OutOfMemoryError` | gradle 内存不足 | 改 gradle.properties（非代码）|

---

## 二、对照原始需求（核心，不能省）

> agent 最容易跳过这步。构建成功 ≠ 任务完成。

### 检查项
```
□ 用户原话要的是什么?(从会话/`.agent/progress.json` 确认)
□ 代码实现了用户要的功能/修复?
□ 实现方式符合用户的约束?
   - 没动用户没要求改的代码?
   - 没改 manifest/gradle(除非用户要求)?
   - 没重构?
□ 有没有引入用户没要求的行为?
□ 改动范围和 confirm-first 的方案一致?
```

### 反例：只对照自己的代码
```
❌ agent: 我改了 onCreate，加了 async，代码看起来对，构建成功，完成。
   (没验证：async 回调里 workspace.refresh() 真的被调用了吗?首屏真的不白屏了吗?)
```

### 正例：对照原始需求
```
✅ agent: 用户要"修 launcher 启动慢"。
   我改了 onCreate 用 async。
   验证:
   - 构建: 成功
   - 需求对照: onCreate 不再同步 loadWorkspace ✓
   - 但需确认: async 回调是否真的刷新了 workspace?
   - 手动验证建议: adb install + 重启 + logcat 看 onCreate 耗时
```

---

## 三、边界情况

```
□ null 输入处理了?
□ 空集合/空字符串处理了?
□ 异常被 catch 且有处理(不只吞掉)?
□ 线程安全: 异步回调访问的变量线程安全?
□ 生命周期: Activity/Fragment 销毁时回调还安全?
```

---

## 四、harness 检查

```bash
bash .skills/_harness/scripts/diff_guard.sh
bash .skills/_harness/scripts/ai_smell_check.sh <改的文件>
```
```
□ diff_guard: 无"用户没要求的改动"?
□ ai_smell_check: 无 AI 味注释?
□ 任一报错 → 修正后重新验证
```

---

## 五、进度记录

更新 `.agent/progress.json`：
```json
"verify_stats": {
  "total_tasks": <+1>,
  "first_pass_ok": <若一次通过则+1>,
  "retries_needed": <重试次数>,
  "rollbacks": <若回滚则+1>
}
```

> trace 驱动改进：first_pass_ok / total 比例低 → 往 `.agent/project-skill.md` 加规则（免疫系统机制）。

---

## 六、报告模板

### 验证通过
```
✅ 验证通过
- 构建: <cmd> 成功
- 需求对照: 实现了 <用户要的>，未引入额外行为
- 边界: null/异常/生命周期 已处理
- harness: diff_guard ✓ / ai_smell ✓
- 下一步: 跑 project-memory sync 更新索引
```

### 验证失败（重试后仍失败）
```
❌ 验证失败（重试 N 次）
- 构建错误: <错误摘要>
- 已尝试: <修复尝试>
- 需要: 人工介入，可能方案有问题
- 建议: <重新审视方案 / 检查依赖 / 问用户>
```

### 循环检测触发
```
⚠️ 循环检测: <文件> 已编辑 N 次仍未通过
这可能是方案问题，不是实现问题。
建议停下来重新审视方案，或问用户。
```

---

## 七、配置

`skills.config.yaml`:
```yaml
verify_loop:
  enabled: true
  build_cmd: "./gradlew assembleDebug"
  max_retries: 2
```

- `enabled: false` → 跳过整个验证（不推荐，除非纯文档）
- `build_cmd: ""` → 跳过构建，只做需求对照 + harness 检查
- `max_retries` → 失败后最多重试次数，超过则报告用户
