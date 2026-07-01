package com.pomodoro.jetpack.ui.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pomodoro.jetpack.viewmodel.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * ═══════════════════════════════════════════════════════════
 * 历史记录页面 — Jetpack Compose + Room Flow
 * ═══════════════════════════════════════════════════════════
 *
 * 【传统 Java 思维 → Compose 完整映射】
 *
 * 传统 Java 写法（HistoryActivity.java）：
 *   - ListView + SimpleAdapter
 *   - onCreate 里同步查数据库
 *   - 手动构造 List<Map<String, String>> 填充
 *   - setAdapter() 一次性设置
 *
 * Jetpack Compose 写法（本文件）：
 *   - LazyColumn（对标 RecyclerView / Flutter ListView.builder）
 *   - Room 返回 Flow<List>，数据变化自动更新 UI
 *   - 不需要手动刷新
 *
 * 对比 Flutter 版（history_page.dart）：
 *   - ListView.builder + itemCount + itemBuilder
 *   - Future + setState 异步加载
 *   - 概念几乎一样
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(

    /**
     * 「返回」按钮的回调
     *
     * 【🔥 声明式 UI 的核心概念 🔥】
     *
     * 这不是「点击返回按钮→关闭页面」的动作，而是「点击返回按钮→通知调用者」的回调。
     * 谁调用 HistoryScreen，谁就传 onBack 进来，由调用者决定"返回"意味着什么。
     *
     * 传统 Java 写法：
     *   btnBack.setOnClickListener {
     *       finish()  ← 在 HistoryActivity 内部直接关闭自己
     *   }
     *   → Activity 自己决定怎么关闭，调用者无感
     *
     * Jetpack Compose 写法：
     *   HistoryScreen(onBack = { showHistory = false })
     *   ← onBack 是个 Lambda，由 AppRoot 传入
     *   ← 点击返回按钮 → 调用 onBack() → AppRoot 里改 showHistory = false
     *   ← Compose 自动重组 → if (showHistory) 走 else 分支 → 回到 TimerScreen
     *
     * 为什么要这样设计？
     *   1. HistoryScreen 自己不知道「返回」该做什么（可能切页面、可能弹窗、可能退出 App）
     *   2. 把这个决定权交给上层（AppRoot），HistoryScreen 只负责"通知一声"
     *   3. 这叫"控制反转"（IoC），和 Hilt 的 @Inject 一个思路
     *
     * 对比 Flutter 版：
     *   Flutter 的 HistoryPage 通过 Navigator.pop(context) 返回
     *   ← Flutter 走的是命令式导航，所以能在页面内部直接 pop
     *   ← 本项目 Compose 版用状态切换做导航，所以必须回调
     */
    onBack: () -> Unit,//Unit这个参数是一个没有输入、没有输出的回调函数

    /**
     * ViewModel 实例
     * 默认值 viewModel() = 通过 ViewModelProvider 获取
     * AppRoot 传入自定义实例是为了注入 DAO
     */
    viewModel: HistoryViewModel = viewModel()
) {
    // ═══════════════════════════════════════════════════════════
    // 订阅 Flow，数据库一变 UI 自动更新
    // ═══════════════════════════════════════════════════════════
    //
    // 传统 Java:
    //   private void loadHistory() {
    //       List<PomodoroRecord> records = DatabaseHelper.getInstance(this).getAllRecords();
    //       SimpleAdapter adapter = new SimpleAdapter(...);
    //       listView.setAdapter(adapter);
    //   }
    //   问题：数据库变了不会自动刷新，要手动再调 loadHistory()
    //
    // Compose:
    //   val records by viewModel.records.collectAsState()
    //   ← collectAsState() = 订阅 StateFlow，值变化时自动重组本 Composable
    //   ← Room 数据库一变 → Flow 发射新 List → collectAsState 收到 → 重组 HistoryScreen
    //
    // by = Kotlin 属性委托，让 StateFlow<List> 用起来像普通 List
    // 读 records = 获取最新值，值变化时整个 HistoryScreen() 重新执行
    val records by viewModel.records.collectAsState()

    // ═══════════════════════════════════════════════════════════
    // Scaffold — Material3 页面脚手架
    // ═══════════════════════════════════════════════════════════
    //
    // 【Scaffold 是什么？】
    // Scaffold 是 Material3 提供的「标准页面框架」，自动帮你处理：
    //   1. 顶部栏（TopAppBar）
    //   2. 底部栏（BottomBar）
    //   3. 浮动按钮（FloatingActionButton）
    //   4. 内容区域（自动避开上述 UI 元素）
    //   5. Snackbar 弹窗
    //
    // 对标传统 Java:
    //   CoordinatorLayout + AppBarLayout + Toolbar + Content
    //   <androidx.coordinatorlayout.widget.CoordinatorLayout>
    //     <com.google.android.material.appbar.AppBarLayout>
    //       <androidx.appcompat.widget.Toolbar/>
    //     </AppBarLayout>
    //     <FrameLayout app:layout_behavior="...">  ← 内容区
    //       ...
    //     </FrameLayout>
    //   </CoordinatorLayout>
    //
    // 对比 Flutter 版:
    //   Scaffold(appBar: AppBar(...), body: ...)
    //   ← 完全一样的概念，Flutter 也叫 Scaffold
    //
    // 【Scaffold 的参数】
    // topBar = { ... }       顶部栏内容（本例是 TopAppBar）
    // bottomBar = { ... }    底部栏（可选，本项目不用）
    // content = { padding -> ... }  内容区域（核心！见下方 padding 解释）
    Scaffold(
        topBar = {

            // ═══════════ TopAppBar — 顶部导航栏 ═══════════
            //
            // 对标传统 Java:
            //   <com.google.android.material.appbar.MaterialToolbar
            //     android:title="番茄历史"
            //     app:navigationIcon="@drawable/ic_arrow_back"
            //     app:navigationOnClickListener="..."/>
            //
            // 对比 Flutter:
            //   AppBar(title: Text("番茄历史"), leading: IconButton(...))
            TopAppBar(

                // title = 标题
                // { Text("番茄历史") } 是个 Composable lambda
                // 可以放任意 Composable（Text/Image/甚至 Row 都行）
                title = { Text("番茄历史") },

                // navigationIcon = 左侧导航图标（通常是返回箭头）
                // 对标传统 Java: app:navigationIcon + setNavigationOnClickListener
                navigationIcon = {

                    // IconButton = 可点击的图标按钮
                    // 对标传统 Java: ImageButton 或 ImageView + setOnClickListener
                    //
                    // onClick = onBack
                    //   ← 这里就是 onBack 回调被触发的地方！
                    //   ← 用户点返回箭头 → 调用 onBack()
                    //   ← AppRoot 里 onBack = { showHistory = false }
                    //   ← showHistory 变 false → Compose 重组 → 回到 TimerScreen
                    IconButton(onClick = onBack) {

                        // Icon = 图标
                        // Icons.AutoMirrored.Filled.ArrowBack = Material 自带的返回箭头
                        //   AutoMirrored = 自动适配 RTL（阿拉伯语等从右到左语言，箭头方向自动翻转）
                        //   Filled = 实心样式
                        //
                        // contentDescription = 无障碍描述（给屏幕阅读器用，盲人用户会听到"返回"）
                        // 对标传统 Java: android:contentDescription="返回"
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                }
            )
        }
    ) { padding ->

        // ═══════════════════════════════════════════════════════════
        // padding — Scaffold 自动计算的内容区域内边距
        // ═══════════════════════════════════════════════════════════
        //
        // 【🔥 你的核心疑问：padding 在哪赋值的？🔥】
        //
        // 答案：padding 是 Scaffold 的 content lambda 的「参数」，
        //       由 Scaffold 内部自动计算并传入，你不用手动赋值！
        //
        // 看这个签名：
        //   Scaffold(topBar = {...}) { padding ->
        //     //              ↑↑↑↑↑↑↑
        //     //   Scaffold 自动算好 TopAppBar 占了多少空间，
        //     //   把「内容区应该用的 padding」通过这个参数传给你
        //   }
        //
        // 具体来说，padding 是个 PaddingValues 对象，包含 4 个值：
        //   padding.top    = TopAppBar 的高度（比如 56dp）
        //   padding.bottom = BottomBar 的高度（本项目没 BottomBar，所以是 0）
        //   padding.start  = 左侧 inset（通常 0）
        //   padding.end    = 右侧 inset（通常 0）
        //
        // 对标传统 Java:
        //   CoordinatorLayout 会自动让 FrameLayout 的 layout_behavior
        //   避开 AppBarLayout，本质上也是"框架算好 inset 给你"
        //   传统写法：用 fitsSystemWindows="true" + behavior 自动处理
        //   Compose 写法：Scaffold 显式把 padding 传给你，你手动应用
        //
        // 【为什么必须手动应用 .padding(padding)？】
        // 因为 Compose 是声明式，不会偷偷改你的布局。
        // Scaffold 算好了 padding 给你，但你不用它就不会生效。
        // 这和传统 Android 的 behavior 自动避让不同——Compose 要求显式。
        //
        // 如果不写 .padding(padding)，会发生什么？
        //   → 列表内容会被 TopAppBar 盖住！因为列表从屏幕顶部 (y=0) 开始绘制
        //   → TopAppBar 在 z 轴上层，盖住了列表第一行
        //   → 加了 .padding(padding) 后，列表从 TopAppBar 下方开始绘制
        //
        // 【列表 item 之间的间距来自哪里？】
        // 你看到的 item 间距来自两个地方：
        //   1. ListItem 这个 Material3 组件自带内边距（每个 item 上下有 padding）
        //   2. contentPadding = PaddingValues(16.dp) 给整个列表加了 16dp 外边距
        //   注意：contentPadding 是 LazyColumn 的「外边距」，不是 item 间距
        //         item 之间的细线是 HorizontalDivider() 画的，不是间距

        // SimpleDateFormat = 传统 Java 日期格式化
    // remember {} = 只在首次重组时创建，后续复用（避免 items 里每次 new）
    val sdf = remember { SimpleDateFormat("MM/dd HH:mm", Locale.getDefault()) }
        if (records.isEmpty()) {
            // ═══════════ 空状态 ═══════════
            //
            // Box = FrameLayout（层叠布局）
            // fillMaxSize() = match_parent（占满整个内容区）
            // .padding(padding) = 应用 Scaffold 计算的内边距（避开 TopAppBar）
            // contentAlignment = Alignment.Center = 内容居中
            //
            // 对标传统 Java:
            //   <FrameLayout android:layout_width="match_parent"
            //                android:layout_height="match_parent"
            //                android:paddingTop="?attr/actionBarSize">
            //     <TextView android:layout_gravity="center" .../>
            //   </FrameLayout>
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),  // ← 关键！避开 TopAppBar
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "还没有番茄记录\n开始你的第一个番茄吧！",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {

            // ═══════════ 列表 ═══════════
            //
            // LazyColumn = RecyclerView / Flutter ListView.builder
            //
            // 为什么叫 Lazy（懒）？
            //   只渲染屏幕上可见的 item，滑动时才创建新的
            //   对比传统 ListView（一次性创建所有 item，性能差）
            //
            // 对标传统 Java:
            //   <androidx.recyclerview.widget.RecyclerView
            //     android:layout_width="match_parent"
            //     android:layout_height="match_parent"/>
            //
            // 对比 Flutter:
            //   ListView.builder(itemCount: ..., itemBuilder: (ctx, i) => ...)
            //
            // 【参数详解】
            // modifier = Modifier.fillMaxSize().padding(padding)
            //   ← fillMaxSize 占满内容区
            //   ← padding(padding) 避开 TopAppBar
            //
            // contentPadding = PaddingValues(16.dp)
            //   ← 列表的「内边距」（不是 item 间距！）
            //   ← 相当于 RecyclerView 的 android:padding="16dp"
            //   ← 列表内容上下左右各空 16dp
            //   ← 滑动时 item 会划过这个 padding 区域
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),  // ← 关键！避开 TopAppBar
                contentPadding = PaddingValues(16.dp)  // ← 列表四周内边距
            ) {

                // ═══════════ items() — 自动遍历列表生成 item ═══════════
                //
                // items(records) { record -> ... }
                //   等价于传统 Java:
                //   adapter = new Adapter() {
                //     onBindViewHolder(holder, position) {
                //       record = records.get(position);
                //       holder.bind(record);
                //     }
                //   }
                //
                // 等价于 Flutter:
                //   ListView.builder(
                //     itemCount: records.length,
                //     itemBuilder: (ctx, i) => HistoryItem(records[i]),
                //   )
                items(records) { record ->

                    // ═══════════ 单个记录项 ═══════════
                    //
                    // sdf 已通过 remember {} 提到 items 外层复用

                    // ListItem = Material3 提供的列表项组件
                    // 自带 Material Design 规范的 padding、字体、颜色
                    //
                    // 对标传统 Java:
                    //   <com.google.android.material.listitem.ListItem
                    //     android:title="..."
                    //     android:subtitle="..."/>
                    //   或自己写 LinearLayout + 3 个 TextView
                    //
                    // 【参数详解】
                    // headlineContent = 主标题（粗体，对应 record.durationMinutes）
                    //   对标传统: android:textAppearance="?attr/textAppearanceTitleMedium"
                    // supportingContent = 副标题（灰色，对应 endTime）
                    //   对标传统: android:textAppearance="?attr/textAppearanceBodySmall"
                    // leadingContent = 左侧图标/头像
                    //   对标传统: <ImageView android:layout_gravity="start|center_vertical"/>
                    //
                    // 【item 之间的间距哪来的？】
                    // ListItem 内部自带 padding（上下各 8dp 左右 16dp）
                    // 你看到的 item 间距其实是 ListItem 的内边距 + 分割线
                    ListItem(
                        headlineContent = { Text("${record.durationMinutes} 分钟") },
                        supportingContent = { Text(sdf.format(Date(record.endTime))) },
                        leadingContent = { Text("🍅") }  // 用 emoji 当图标（简化）
                    )

                    // HorizontalDivider = 分割线
                    // 对标传统 Java: <View android:layout_height="1dp" android:background="#E0E0E0"/>
                    // 这才是 item 之间那条灰线的来源
                    HorizontalDivider()
                }
            }
        }
    }
}
