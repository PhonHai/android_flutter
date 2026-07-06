package com.pomodoro.jetpack.ui.timer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.pomodoro.jetpack.viewmodel.TimerEvent
import com.pomodoro.jetpack.viewmodel.TimerStatus
import com.pomodoro.jetpack.viewmodel.TimerViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.min

/**
 * 番茄钟计时页面 — Jetpack Compose 声明式 UI
 *
 * ═══════════════════════════════════════════════════════════
 * 【🔥 传统 Java 思维 → Compose 完整映射 🔥】
 * ═══════════════════════════════════════════════════════════
 *
 * ┌────────────────────────────┬────────────────────────────────────────┐
 * │ 传统 Java (activity_main)  │ Compose (本文件)                       │
 * ├────────────────────────────┼────────────────────────────────────────┤
 * │ <LinearLayout vertical>    │ Column { }                             │
 * │ <TextView text="25:00"/>   │ Text(text = state.formattedTime)       │
 * │ <Button onClick="start"/>  │ Button(onClick = { vm.start() })       │
 * │ findViewById(R.id.tv_time) │ val state by vm.uiState.collectAsState│
 * │ tvTime.setText("24:59")    │ state 变化 → Compose 自动重组          │
 * │ XML 写布局 + Java 写逻辑   │ 全部 Kotlin 代码写                     │
 * └────────────────────────────┴────────────────────────────────────────┘
 *
 * 对比 Flutter 版（timer_widget.dart）：
 *   ConsumerWidget → Composable 函数
 *   ref.watch(provider) → collectAsState()
 *   Column / Text / Button → 完全一样的概念
 *   CustomPainter → Canvas + drawArc
 *
 * ═══════════════════════════════════════════════════════════
 */
@Composable
fun TimerScreen(
    /**
     * NavController — 导航控制器
     *
     * TimerScreen 在 NavHost 内部，通过参数传入 navController 以执行页面跳转。
     * 对标传统 Java: findNavController()
     */
    navController: NavController,

    /**
     * ViewModel 实例 — 由 Hilt 注入（外部传入）
     */
    viewModel: TimerViewModel,

    /**
     * 布局修饰符
     */
    modifier: Modifier = Modifier
) {
    // ===== 订阅 ViewModel 状态 =====
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // ===== Channel 一次性事件：计时完成 → Snackbar =====
    // 【面试题44】Channel 不粘性，旋转屏幕不会重复弹 Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is TimerEvent.Finished -> {
                    snackbarHostState.showSnackbar(
                        message = "计时完成！已完成 ${state.completedSessions} 个番茄",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    // Column = 垂直 LinearLayout
    // 对标 Flutter: Column(mainAxisAlignment: center)
    Column(
        modifier = modifier
            .fillMaxSize()                    // ≈ match_parent
            .padding(horizontal = 24.dp),     // ≈ android:paddingHorizontal
        horizontalAlignment = Alignment.CenterHorizontally,  // ≈ gravity center_horizontal
        verticalArrangement = Arrangement.Center             // ≈ layout_gravity center_vertical
    ) {
        // ===== 环形进度 + 时间显示 =====
        // Box = FrameLayout / Stack（层叠布局）
        // 对标 Flutter: Stack(alignment: Alignment.center, children: [...])
        Box(
            modifier = Modifier.size(260.dp),    // ≈ width=height=260dp
            contentAlignment = Alignment.Center
        ) {
            // Canvas 绘制环形进度
            // 对标 Flutter: CustomPaint(painter: _ProgressPainter)
            // 对标传统 Java: 自定义 View.onDraw(Canvas canvas) { canvas.drawArc() }
            Canvas(modifier = Modifier.size(260.dp)) {
                val strokeWidth = 12.dp.toPx()
                val diameter = min(size.width, size.height) - strokeWidth
                val topLeft = Offset(
                    (size.width - diameter) / 2,
                    (size.height - diameter) / 2
                )
                val arcSize = Size(diameter, diameter)

                // 背景圆环（灰色）
                // 对标传统 Java: mPaint.setColor(Color.LTGRAY); canvas.drawCircle(...)
                drawArc(
                    color = Color(0xFFE0E0E0),
                    startAngle = -90f,           // 从 12 点方向开始
                    sweepAngle = 360f,           // 画整圈
                    useCenter = false,           // 不画扇形（只要弧线）
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth)  // 描边模式
                )

                // 进度弧线（番茄红）
                // sweepAngle = 360 * progress（对标 Flutter 2 * pi * progress）
                // 对标传统 Java: canvas.drawArc(rect, -90, 360 * progress, false, paint)
                drawArc(
                    color = Color(0xFFE53935),
                    startAngle = -90f,
                    sweepAngle = 360f * state.progress,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth)
                )
            }

            // 时间 + 番茄数文字（叠在 Canvas 上面）
            // Box 会把后面的子元素叠在前面子元素之上（z-order）
            // 对标传统 Java: <TextView> 在 <FrameLayout> 里居中
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 时间数字
                // state.formattedTime 是 TimerUiState 的计算属性（"25:00"）
                // 对标传统 Java: tvTime.setText(state.formattedTime)
                // Compose 不需要 setText，状态变 → Text 自动重渲染
                Text(
                    text = state.formattedTime,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Light,  // 细体
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace  // 等宽
                )

                // 间距 8dp
                // 对标传统 Java: <Space android:layout_height="8dp" />
                // 或 android:layout_marginTop="8dp"
                Spacer(modifier = Modifier.height(8.dp))

                // 已完成番茄数
                // 对标传统 Java: tvSessionCount.setText("已完成 N 个番茄")
                Text(
                    text = "已完成 ${state.completedSessions} 个番茄",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 间距 40dp
        Spacer(modifier = Modifier.height(40.dp))

        // ===== 控制按钮 =====
        // Row = 水平 LinearLayout
        // 对标 Flutter: Row(mainAxisAlignment: center, children: [...])
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // 重置按钮 — 只在非 idle 状态可点击
            // enabled = false 时按钮变灰
            // 对标传统 Java: btnReset.setEnabled(!isIdle)
            TextButton(
                onClick = { viewModel.reset() },
                enabled = state.status != TimerStatus.IDLE
            ) {
                Text("重置")
            }

            Spacer(modifier = Modifier.width(24.dp))

            // 主按钮：根据状态显示不同文字
            // 传统 Java: btnStart.setText(if running "暂停" else "开始")
            // Compose: 声明式，状态变自动更新文字
            Button(
                onClick = {
                    // 点击逻辑：运行中→暂停，其他→开始
                    if (state.status == TimerStatus.RUNNING) {
                        viewModel.pause()
                    } else {
                        viewModel.start()
                    }
                },
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                // when 是 Kotlin 的 switch（增强版）
                // 状态变 → when 自动重新计算 → Text 自动更新
                Text(
                    text = when (state.status) {
                        TimerStatus.IDLE, TimerStatus.FINISHED -> "开始"
                        TimerStatus.RUNNING -> "暂停"
                        TimerStatus.PAUSED -> "继续"
                    },
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            // 跳过按钮（和重置一样调 reset，简化处理）
            TextButton(onClick = { viewModel.reset() }) {
                Text("跳过")
            }
        }

        // 间距 40dp
        Spacer(modifier = Modifier.height(40.dp))

        // ===== 时长选择 =====
        // 传统 Java: 3 个 Button + setOnClickListener + 手动改 totalSeconds
        // Compose: FilterChip + selected 状态绑定
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // listOf(15, 25, 45).forEach { ... } 遍历生成 3 个 Chip
            // 对标传统 Java: for (int min : new int[]{15, 25, 45}) { ... }
            listOf(1, 2, 3).forEach { minutes ->
                FilterChip(
                    selected = state.totalSeconds == minutes * 10,
                    onClick = {
                        // 只在非运行状态时允许改时长
                        if (state.status == TimerStatus.IDLE ||
                            state.status == TimerStatus.FINISHED) {
                            viewModel.setDuration(minutes)
                        }
                    },
                    label = { Text("${minutes*10} 秒") },
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }

        // 间距 32dp（把历史按钮推到底部）
        Spacer(modifier = Modifier.height(32.dp))

        // ===== 4 级导航演示入口按钮 =====
        //
        // 对标 legacy-android TimerFragment 底部「进入 4 级导航演示」
        // 对标传统 Java:
        //   btnNavDemo.setOnClickListener {
        //       navController.navigate(R.id.homeFragment)
        //   }
        //
        // navController 通过参数传入，直接在点击时调用 navigate()
        OutlinedButton(
            onClick = { navController.navigate("home") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("进入 4 级导航演示")
        }


    }
}
