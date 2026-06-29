package com.pomodoro.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 原生 Compose 番茄历史列表 — 展示原生 + Flutter 混合能力
 *
 * ===== 为什么用原生 Compose 而不是 Flutter 写？ =====
 * 1. 演示混合架构: 原生页面和 Flutter 页面之间的导航
 * 2. 未来接入 Room + Paging3 分页（原生能力更强）
 * 3. 对标绿联云: 核心列表用原生，复杂 UI 用 Flutter
 *
 * ===== Android 概念速查 =====
 * ComponentActivity = Compose 宿主 Activity（不用 Fragment）
 * TopAppBar         ≈ Toolbar / ActionBar
 * Scaffold          ≈ CoordinatorLayout（自动处理 TopBar + Body 布局）
 * IconButton        ≈ ImageButton
 * finish()          ≈ 关闭当前 Activity，返回上一页
 *
 * ===== 面试话术 =====
 * "历史列表用原生 Compose 写，未来可以接入 Room + Paging3 做分页加载。
 *  数据源是 Flutter 通过 MethodChannel 写入的原生 Room 数据库。
 *  同时演示了原生页面和 Flutter 页面之间的混合导航，
 *  用户可以从 Flutter 计时页返回原生首页，再进入原生历史列表。
 *  对标绿联云中文件列表用原生，影视播放用 Flutter 的混合架构。"
 */
class PomodoroListActivity : ComponentActivity() {

    /**
     * @OptIn(ExperimentalMaterial3Api) = 声明使用实验性 API
     * TopAppBar 在 Material3 中目前还是实验性的（Kotlin 2.1.20 时代）
     */
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                // Scaffold = 标准 Material 页面脚手架
                // 提供 TopAppBar（顶部栏）+ 内容区（padding 后的 body）
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("番茄历史 (原生 Compose)") },
                            // ===== 返回按钮 =====
                            // Icons.AutoMirrored.Filled.ArrowBack:
                            //   AutoMirrored = 自动适配 RTL（阿拉伯语等从右到左语言）
                            //   在 LTR 语言下是 ←，在 RTL 下是 →
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    // finish() = 关闭 Activity，回到上一个页面
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "返回"  // 无障碍描述
                                    )
                                }
                            }
                        )
                    }
                ) { padding ->
                    // padding = Scaffold 自动计算的内容区内边距
                    // （扣除 TopAppBar 高度后的剩余空间）

                    // ===== TODO: Week 2 接入 Room + Paging3 =====
                    // 当前是占位 Composable，Week 2 替换为:
                    //   val historyFlow = database.pomodoroDao().getAllFlow()
                    //   val items = historyFlow.collectAsState(initial = emptyList())
                    //   LazyColumn { items(items) { HistoryItem(it) } }
                    //
                    // ===== 面试话术 =====
                    // "这里用 Room + Paging3 做分页加载，
                    //  数据源是 Flutter 通过 MethodChannel 写入的原生数据库，
                    //  对标绿联云中用 Room 管理文件列表的方案。"
                    HistoryListPlaceholder(
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}

/**
 * 历史列表占位（Week 1 MVP）
 *
 * 面试时可以说: "目前是占位，下一阶段会接入 Room + Paging3，
 * 数据流是 Flutter MethodChannel → Room → Compose 列表"
 */
@Composable
fun HistoryListPlaceholder(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "暂无番茄记录",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "在 Flutter 页面完成番茄后\n通过 MethodChannel 写入原生 Room DB",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
    }
}
