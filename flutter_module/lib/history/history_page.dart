import 'package:flutter/material.dart';
import '../models/pomodoro_record.dart';
import 'history_db.dart';

/// ═══════════════════════════════════════════════════════════
/// 历史记录页面 — 从 sqflite 加载并展示
/// ═══════════════════════════════════════════════════════════
///
/// ===== Android 概念映射 =====
///
/// | Flutter                     | Android 对应                    | 说明                     |
/// |-----------------------------|---------------------------------|--------------------------|
/// | `StatefulWidget`            | `@Composable fun + remember {}` | 有本地状态的 Widget       |
/// | `setState(() {})`           | `mutableStateOf` 赋值            | 触发局部 UI 重绘          |
/// | `initState()`               | `LaunchedEffect(Unit)`          | 只执行一次的初始化         |
/// | `Future<void> _loadRecords()`| `suspend fun loadRecords()`     | 异步加载数据              |
/// | `ListView.builder`          | `LazyColumn`                    | 列表（懒加载每个 item）    |
/// | `Card`                      | `Card { }` (Material)           | 卡片容器                 |
/// | `CircularProgressIndicator` | `CircularProgressIndicator`     | 加载转圈                 |
///
/// ===== 注意：项目中这个页面未被使用 =====
/// 当前导航策略: 原生 PomodoroListActivity 用 Compose 写历史列表
/// HistoryPage 是 Flutter 版的备用实现（未来可能启用）
///
/// ===== 面试话术 =====
/// "HistoryPage 从 sqflite 加载历史记录并用 ListView 展示。
///  用 Future + setState 做异步加载（对标 Kotlin 的 suspend + mutableStateOf）。
///  当前导航走原生壳的 PomodoroListActivity，
///  展示的是 Flutter 和原生两种列表实现共存的混合架构。"
class HistoryPage extends StatefulWidget {
  const HistoryPage({super.key});

  /// StatefulWidget 需要 createState()
  /// 对标 Compose 中 `var x by remember { mutableStateOf(...) }`
  @override
  State<HistoryPage> createState() => _HistoryPageState();
}

/// HistoryPage 的状态类（私有的，因为文件名前缀 _）
///
/// 下划线开头 = Dart 私有（≈ Kotlin private class）
class _HistoryPageState extends State<HistoryPage> {
  // ===== 本地状态 =====
  List<PomodoroRecord> _records = [];  // 历史记录列表
  bool _loading = true;               // 加载中标志

  /// initState = Widget 挂载到树时执行一次
  /// ≈ Compose 的 LaunchedEffect(Unit) { ... }
  @override
  void initState() {
    super.initState();  // 必须调用父类
    _loadRecords();     // 开始加载数据
  }

  /// 从数据库异步加载记录
  ///
  /// async/await = Kotlin suspend/coroutine
  /// setState(() {}) = 通知 Flutter 重新调用 build()
  ///   ≈ Kotlin: _records = newList; _loading = false (mutableStateOf 自动触发重组)
  Future<void> _loadRecords() async {
    // await = 等待异步操作完成（≈ Kotlin 的 await 或不写 await 的协程）
    final records = await HistoryDatabase.instance.getAllRecords();

    // setState = 更新状态 + 触发 build() 重建
    // 必须在 setState 里改状态值
    setState(() {
      _records = records;
      _loading = false;
    });
  }

  /// build = Widget 绘制入口（状态变化时重新执行）
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('番茄历史')),
      body: _loading
          // ===== 加载中 → 转圈 =====
          ? const Center(child: CircularProgressIndicator())
          // ===== 空数据 → 提示文字 =====
          : _records.isEmpty
              ? const Center(
                  child: Text(
                    '还没有番茄记录\n开始你的第一个番茄吧！',
                    textAlign: TextAlign.center,
                    style: TextStyle(fontSize: 16, color: Colors.grey),
                  ),
                )
              // ===== 有数据 → 列表展示 =====
              // ListView.builder = 懒加载列表（≈ RecyclerView / LazyColumn）
              //   只构建屏幕上可见的 item，性能好
              : ListView.builder(
                  padding: const EdgeInsets.all(16),
                  itemCount: _records.length,  // 列表总条数
                  // itemBuilder = item 工厂（对标 Adapter.onBindViewHolder / LazyColumn 的 items {}）
                  itemBuilder: (context, index) {
                    final record = _records[index];

                    // 格式化日期字符串
                    // Dart 字符串拼接: 相邻字符串自动拼接，不需要 + 号
                    final dateStr =
                        '${record.startTime.month}/${record.startTime.day} '
                        '${record.startTime.hour}:${record.startTime.minute.toString().padLeft(2, '0')}';

                    // Card = 卡片容器（≈ Material Card Composable）
                    // ListTile = 标准列表项（≈ ListItem）
                    return Card(
                      child: ListTile(
                        // leading = 左侧图标（番茄钟图标，红色）
                        leading: const Icon(Icons.timer,
                            color: Color(0xFFE53935)),
                        title: Text('${record.durationMinutes} 分钟'),
                        subtitle: Text(dateStr),
                        // trailing = 右侧图标（绿色对勾 = 已完成）
                        trailing: const Icon(Icons.check_circle,
                            color: Colors.green, size: 20),
                      ),
                    );
                  },
                ),
    );
  }
}
