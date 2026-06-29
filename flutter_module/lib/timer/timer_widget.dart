import 'dart:math';  // min(), pi
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'timer_notifier.dart';
import 'timer_state.dart';

/// ═══════════════════════════════════════════════════════════
/// 番茄钟计时页面 — CustomPainter 画圆环 + Riverpod 消费状态
/// ═══════════════════════════════════════════════════════════
///
/// ===== Android 概念映射 =====
/// 这个文件 ≈ Android 的一个 Composable 屏幕（全 Compose UI）
///
/// | Flutter                        | Android 对应                         | 说明                     |
/// |--------------------------------|--------------------------------------|--------------------------|
/// | `ConsumerWidget`               | `@Composable fun Screen(vm: VM)`    | 消费 ViewModel 的 Widget  |
/// | `ref.watch(timerProvider)`     | `val state by vm.uiState.collectAsState()` | 订阅状态变化          |
/// | `ref.read(provider.notifier)`  | `viewModel` 实例                     | 获取 ViewModel 调用方法    |
/// | `CustomPaint + CustomPainter`  | `Canvas.drawArc()`                   | 自定义绘制                |
/// | `Column`                       | `Column { }` (Compose)               | 垂直排列                  |
/// | `Row`                          | `Row { }` (Compose)                  | 水平排列                  |
/// | `Stack`                        | `Box { }` (Compose)                  | 层叠布局                  |
/// | `ChoiceChip`                   | `FilterChip`                         | 选择芯片                  |
/// | `SizedBox`                     | `Box(modifier = Modifier.size())`    | 固定尺寸容器              |
///
/// ===== ConsumerWidget vs StatelessWidget =====
/// ConsumerWidget = Riverpod 专有的 Widget 基类
///   提供 WidgetRef 参数（相当于拿到 Hilt 注入的 ViewModel）
/// StatelessWidget = 普通无状态 Widget
/// StatefulWidget   = 有本地状态的 Widget
///
/// ===== 面试话术 =====
/// "TimerWidget 用 ConsumerWidget 消费 Riverpod 的状态，
///  对标 Compose 中通过 collectAsState() 消费 StateFlow。
///  环形进度用 CustomPainter 画（对标 Android Canvas.drawArc），
///  每秒状态更新触发 Widget 树重建，但 Flutter 的 diff 算法
///  只重绘变化的部分，性能接近原生。"
class TimerWidget extends ConsumerWidget {
  const TimerWidget({super.key});

  /// build(WidgetRef ref) — Widget 绘制入口
  ///
  /// WidgetRef = Riverpod 的依赖引用
  ///   ref.watch(provider) = 订阅状态（≈ collectAsState）
  ///   ref.read(provider.notifier) = 获取 Notifier 实例（≈ viewModel）
  ///
  /// 每次 state 变化，这个 build 方法会被重新调用
  /// 但是 Flutter 框架会比较新旧 Widget 树，只更新实际变化的部分
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    // 订阅状态 — 状态一变，整个 build 重新执行
    // ≈ val uiState by viewModel.uiState.collectAsState()
    final state = ref.watch(timerProvider);

    // 获取 Notifier（ViewModel）实例 — 用于调用方法
    // ≈ val viewModel: PomodoroViewModel = hiltViewModel()
    final notifier = ref.read(timerProvider.notifier);

    // Column = 垂直 LinearLayout
    // MainAxisAlignment.center = gravity: center
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        // ===== 环形进度区域 =====
        // Stack = 层叠布局（≈ FrameLayout / Box）
        // 第1层: CustomPaint 画圆环
        // 第2层: Column 居中显示时间和番茄数
        SizedBox(
          width: 260,   // ≈ layout_width="260dp"
          height: 260,
          child: Stack(
            alignment: Alignment.center,  // 子元素居中
            children: [
              // ===== 层1: 环形进度（CustomPainter） =====
              // CustomPaint = Android 的 Canvas 绘制容器
              // painter: 传入自定义 Painter（≈ 自定义 View.onDraw）
              CustomPaint(
                size: const Size(260, 260),
                // _ProgressPainter = 自定义绘制器（下面定义）
                // 画灰色背景圆环 + 红色进度弧
                painter: _ProgressPainter(progress: state.progress),
              ),

              // ===== 层2: 时间 + 番茄数文字 =====
              Column(
                mainAxisSize: MainAxisSize.min,  // 取最小高度（≈ wrap_content）
                children: [
                  // 计时数字 — 等宽字体，48sp
                  Text(
                    state.formattedTime,  // "25:00" 格式
                    style: const TextStyle(
                      fontSize: 48,
                      fontWeight: FontWeight.w300,  // 细体
                      fontFamily: 'monospace',      // 等宽字体（数字对齐）
                    ),
                  ),
                  const SizedBox(height: 8),  // 间距 8dp

                  // 已完成番茄数
                  Text(
                    '已完成 ${state.completedSessions} 个番茄',
                    style: TextStyle(
                      fontSize: 14,
                      color: Colors.grey[600],  // 灰色文字
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),

        const SizedBox(height: 40),  // 间距 40dp

        // ===== 控制按钮行 =====
        // Row = 水平 LinearLayout
        Row(
          mainAxisAlignment: MainAxisAlignment.center,  // 居中
          children: [
            // [重置] 按钮 — 只在非 idle 状态可点击
            _ControlButton(
              icon: Icons.refresh,     // Material 图标
              label: '重置',
              onPressed: state.status != TimerStatus.idle
                  ? () => notifier.reset()  // 调用 ViewModel.reset()
                  : null,  // null = 禁用状态（灰色不可点击）
            ),
            const SizedBox(width: 24),

            // [开始/暂停/继续] 主按钮 — 状态驱动的文字和图标
            _MainButton(state: state, notifier: notifier),

            const SizedBox(width: 24),

            // [跳过] 按钮 — 只在 running 状态可点击
            _ControlButton(
              icon: Icons.skip_next,
              label: '跳过',
              onPressed: state.status == TimerStatus.running
                  ? () => notifier.reset()
                  : null,
            ),
          ],
        ),

        const SizedBox(height: 40),

        // ===== 时长选择行（Chip 组） =====
        // ChoiceChip = Material 单选 Chip
        // 对标 Android 的 ChipGroup + Chip
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [15, 25, 45].map((minutes) {
            final isSelected = state.totalSeconds == minutes * 60;
            return Padding(
              padding: const EdgeInsets.symmetric(horizontal: 8),
              child: ChoiceChip(
                label: Text('$minutes 分钟'),
                selected: isSelected,  // 高亮当前选中
                // onSelected: 用户点击回调
                // 只在 idle/finished 时可切换（运行中不可改时长）
                onSelected: (_) {
                  if (state.status == TimerStatus.idle ||
                      state.status == TimerStatus.finished) {
                    notifier.setDuration(minutes);
                  }
                },
              ),
            );
          }).toList(),
        ),
      ],
    );
  }
}

// ═══════════════════════════════════════════════════════════
// 子 Widget：主按钮（开始/暂停/继续）
// ═══════════════════════════════════════════════════════════

/// 主按钮 — 根据 TimerStatus 切换文字/图标/行为
///
/// 对标 Android:
/// ```kotlin
/// @Composable
/// fun MainButton(state: TimerUiState, onStart: () -> Unit, ...) {
///   val (icon, label, action) = when(state.status) {
///     IDLE, FINISHED -> Triple(Icons.PlayArrow, "开始", onStart)
///     RUNNING -> Triple(Icons.Pause, "暂停", onPause)
///     PAUSED -> Triple(Icons.PlayArrow, "继续", onResume)
///   }
///   ElevatedButton(onClick = action, icon = icon, text = label)
/// }
/// ```
class _MainButton extends StatelessWidget {
  final TimerState state;
  final TimerNotifier notifier;

  const _MainButton({required this.state, required this.notifier});

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);  // 获取当前主题

    IconData icon;
    String label;
    VoidCallback? onPressed;  // VoidCallback = () -> Unit

    // Dart switch = Kotlin when (但不需要写完整枚举)
    switch (state.status) {
      case TimerStatus.idle:
      case TimerStatus.finished:
        // 空闲/已完成 → 显示「开始」按钮
        icon = Icons.play_arrow;
        label = '开始';
        onPressed = () => notifier.start();
        break;
      case TimerStatus.running:
        // 运行中 → 显示「暂停」按钮
        icon = Icons.pause;
        label = '暂停';
        onPressed = () => notifier.pause();
        break;
      case TimerStatus.paused:
        // 已暂停 → 显示「继续」按钮
        icon = Icons.play_arrow;
        label = '继续';
        onPressed = () => notifier.start();  // 注意: 暂停后继续也用 start()
        break;
    }

    // ElevatedButton.icon = Material 凸起按钮 + 前面的图标
    return ElevatedButton.icon(
      onPressed: onPressed,
      icon: Icon(icon, size: 28),
      label: Text(label, style: const TextStyle(fontSize: 18)),
      style: ElevatedButton.styleFrom(
        padding: const EdgeInsets.symmetric(horizontal: 32, vertical: 16),
        backgroundColor: theme.colorScheme.primary,
        foregroundColor: theme.colorScheme.onPrimary,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(16),  // 圆角
        ),
      ),
    );
  }
}

// ═══════════════════════════════════════════════════════════
// 子 Widget：辅助按钮（重置/跳过）
// ═══════════════════════════════════════════════════════════

/// 图标 + 文字组合的辅按钮
///
/// IconButton = Android ImageButton（无文字）
/// Column 包裹实现图标+文字组合
class _ControlButton extends StatelessWidget {
  final IconData icon;
  final String label;
  final VoidCallback? onPressed;  // ? 可空 = 可禁用

  const _ControlButton({
    required this.icon,
    required this.label,
    this.onPressed,
  });

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisSize: MainAxisSize.min,  // wrap_content
      children: [
        IconButton(
          onPressed: onPressed,  // null = 自动变灰色禁用
          icon: Icon(icon),
          iconSize: 28,
        ),
        Text(label, style: const TextStyle(fontSize: 12)),
      ],
    );
  }
}

// ═══════════════════════════════════════════════════════════
// 自定义环形进度绘制器
// ═══════════════════════════════════════════════════════════

/// CustomPainter — 对标 Android Canvas 自定义绘制
///
/// 对标 Android:
/// ```kotlin
/// class ProgressDrawable : Drawable() {
///   override fun onDraw(canvas: Canvas) {
///     canvas.drawCircle(...)   // 背景圆环
///     canvas.drawArc(...)      // 进度弧
///   }
/// }
/// ```
///
/// ===== 面试话术 =====
/// "CustomPainter 是 Flutter 的自定义绘制 API，对标 Android Canvas.drawArc。
///  同样的 API 理念：Paint（画笔）+ Canvas（画布）+ Rect（绘制区域）。
///  shouldRepaint 判断是否需要重绘，避免不必要的绘制开销。"
class _ProgressPainter extends CustomPainter {
  final double progress;  // [0.0, 1.0] 进度值

  _ProgressPainter({required this.progress});

  /// paint = 执行绘制（≈ Canvas.onDraw）
  ///
  /// canvas = 画布对象，和 Android Canvas API 几乎一样
  /// size   = 绘制区域大小（CustomPaint 传入的 260x260）
  @override
  void paint(Canvas canvas, Size size) {
    // 圆心坐标
    final center = Offset(size.width / 2, size.height / 2);
    // 半径 = 短边一半 - 12px padding（防止线条被裁切）
    final radius = min(size.width, size.height) / 2 - 12;

    // ═══════ 步骤 1: 画灰色背景圆环 ═══════
    final bgPaint = Paint()
      ..color = Colors.grey[200]!      // 浅灰色
      ..style = PaintingStyle.stroke   // 描边（不是填充）
      ..strokeWidth = 12               // 线宽
      ..strokeCap = StrokeCap.round;   // 圆角端点
    canvas.drawCircle(center, radius, bgPaint);

    // ═══════ 步骤 2: 画番茄红进度弧 ═══════
    final progressPaint = Paint()
      ..color = const Color(0xFFE53935)  // 番茄红
      ..style = PaintingStyle.stroke
      ..strokeWidth = 12
      ..strokeCap = StrokeCap.round;

    // drawArc — 和 Android Canvas.drawArc 一样
    // Rect.fromCircle = 构建圆的外接矩形
    // -pi/2 = 从 12 点钟方向开始（-90度）
    // 2 * pi * progress = 弧线扫过的角度
    // false = 只画弧线，不连到圆心（不要扇形）
    canvas.drawArc(
      Rect.fromCircle(center: center, radius: radius),
      -pi / 2,          // 起始角度: 12点方向（≈ Android -90°）
      2 * pi * progress, // 扫过角度: [0, 360°]
      false,            // useCenter = false（不画扇形连线）
      progressPaint,
    );
  }

  /// 判断是否需要重绘
  ///
  /// progress 变了 → 重绘（否则跳过，性能优化）
  /// ≈ RecyclerView 的 DiffUtil
  @override
  bool shouldRepaint(_ProgressPainter oldDelegate) =>
      progress != oldDelegate.progress;
}
