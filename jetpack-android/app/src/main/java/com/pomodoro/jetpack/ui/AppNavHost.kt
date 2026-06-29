package com.pomodoro.jetpack.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pomodoro.jetpack.data.PomodoroDatabase
import com.pomodoro.jetpack.ui.comment.CommentScreen
import com.pomodoro.jetpack.ui.detail.DetailScreen
import com.pomodoro.jetpack.ui.home.HomeScreen
import com.pomodoro.jetpack.ui.list.ListScreen
import com.pomodoro.jetpack.ui.main.MainTabScreen
import com.pomodoro.jetpack.viewmodel.TimerViewModel

/**
 * ═══════════════════════════════════════════════════════════
 * 4 级导航图 — NavHost 路由表
 * ═══════════════════════════════════════════════════════════
 *
 * 【🔥 这是你最需要理解的一个文件 🔥】
 *
 * 本项目演示 4 级页面导航：
 *   第 1 级: HomeScreen    (首页，入口)
 *   第 2 级: ListScreen    (列表页)
 *   第 3 级: DetailScreen  (详情页，带参数 id)
 *   第 4 级: CommentScreen (评论页，带参数 id)
 *
 * ═══════════════════════════════════════════════════════════
 * 【传统 Java 思维 → Jetpack Navigation 映射】
 * ═══════════════════════════════════════════════════════════
 *
 * 传统 Java Android 用 Navigation Component (XML 版):
 *
 *   res/navigation/nav_graph.xml:
 *   <navigation startDestination="@id/homeFragment">
 *     <fragment android:id="@+id/homeFragment"
 *               android:name=".HomeFragment">
 *       <action android:id="@+id/to_list"
 *               app:destination="@id/listFragment"/>
 *     </fragment>
 *     <fragment android:id="@+id/listFragment"
 *               android:name=".ListFragment">
 *       <action android:id="@+id/to_detail"
 *               app:destination="@id/detailFragment"/>
 *     </fragment>
 *     <fragment android:id="@+id/detailFragment"
 *               android:name=".DetailFragment"
 *               android:label="详情">
 *       <argument android:name="itemId"
 *                  app:argType="string"/>
 *     </fragment>
 *   </navigation>
 *
 *   Activity 里:
 *   val navController = findNavController(R.id.nav_host)
 *   btnList.setOnClickListener {
 *       navController.navigate(R.id.to_list)
 *   }
 *
 * Jetpack Compose 用 Navigation Compose (代码版):
 *
 *   NavHost(navController, startDestination = "home") {
 *     composable("home") { HomeScreen(onNavigate = { navController.navigate(it) }) }
 *     composable("list") { ListScreen(onNavigate = { navController.navigate(it) }) }
 *     composable("detail/{itemId}") { DetailScreen(it.arguments?.getString("itemId")) }
 *     composable("comment/{itemId}") { CommentScreen(it.arguments?.getString("itemId")) }
 *   }
 *
 *   页面里:
 *   Button(onClick = { navController.navigate("list") })
 *
 * 对比 Flutter Navigator:
 *   Navigator.push(context, MaterialPageRoute(builder: (_) => ListPage()))
 *   Navigator.pop(context)
 *
 * ═══════════════════════════════════════════════════════════
 * 【NavHost 核心概念】
 * ═══════════════════════════════════════════════════════════
 *
 * 1. NavHost = 路由容器
 *    对标传统: NavHostFragment (XML 里的 FragmentContainerView)
 *    作用: 根据 NavController 当前 destination 渲染对应 Composable
 *
 * 2. NavController = 路由控制器
 *    对标传统: NavController (findNavController 拿到的对象)
 *    作用: navigate("route") 前进, popBackStack() 返回
 *    生命周期: 绑定到 Activity/Composable，自动管理返回栈
 *
 * 3. composable("route") = 注册一个目的地
 *    对标传统: <fragment android:id="@+id/xxx">
 *    作用: 把字符串路由和 Composable 绑定
 *
 * 4. 路由参数 {itemId}
 *    对标传统: <argument android:name="itemId">
 *    作用: 传递数据给下一级页面
 *    语法: "detail/{itemId}" + navArgument("itemId") 声明类型
 *
 * 5. 返回栈自动管理
 *    对标传统: FragmentTransaction.addToBackStack(null)
 *    作用: 系统返回键自动 pop，不需要手动处理
 *
 * ═══════════════════════════════════════════════════════════
 * 【为什么用 NavHost 而不是 if/else？】
 * ═══════════════════════════════════════════════════════════
 *
 * 之前 AppRoot 用 if/else 切页面:
 *   var page by remember { mutableStateOf("timer") }
 *   when(page) {
 *     "timer" -> TimerScreen(...)
 *     "history" -> HistoryScreen(...)
 *   }
 *
 * 问题:
 *   1. 没有返回栈 → 系统返回键直接退出 App，不能回到上一页
 *   2. 没有参数传递 → 只能用闭包传值，复杂场景难维护
 *   3. 没有深链接 → 外部跳转只能进首页，不能直达详情页
 *   4. 状态管理乱 → 页面多了 when 分支爆炸
 *
 * NavHost 解决:
 *   1. ✅ 自动返回栈 → 系统返回键自动 pop
 *   2. ✅ 路由参数 → "detail/{itemId}" 优雅传值
 *   3. ✅ 深链接 → pendingIntent 能直达任意页面
 *   4. ✅ 路由表清晰 → 所有页面在一个地方注册
 *
 * ═══════════════════════════════════════════════════════════
 */
/**
 * 4 级导航图
 *
 * @param navController 路由控制器（由调用者传入，生命周期绑定到 Activity）
 * @param onExitApp 在首页按返回键时调用（退出 App）
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    onExitApp: () -> Unit // ()是无参数  Unit是无返回值（类似 void)
) {
    // ═══════════════════════════════════════════════════════════
    // NavHost — 路由容器
    // ═══════════════════════════════════════════════════════════
    //
    // 参数:
    //   navController = 路由控制器（管理页面栈）
    //   startDestination = 启动页面路由（"home"）
    //
    // 对标传统 Java:
    //   <fragment
    //     android:id="@+id/nav_host"
    //     android:name="androidx.navigation.fragment.NavHostFragment"
    //     app:defaultNavHost="true"
    //     app:navGraph="@navigation/nav_graph"/>
    //
    // 对比 Flutter:
    //   MaterialApp(home: HomeScreen(), onGenerateRoute: ...)
    NavHost(
        navController = navController,
        startDestination = "main"  // ← 启动即显示底部 4 Tab 首页
    ) {

        // ═══════════════════════════════════════════════════════════
        // 第 0 级: 首页 Tab 容器（startDestination）
        // ═══════════════════════════════════════════════════════════
        //
        // MainTabScreen 包含底部 4 个 Tab：番茄钟 | 统计 | 设置 | 关于
        // 对标 legacy-android: TabContainerFragment
        composable("main") {
            val ctx = androidx.compose.ui.platform.LocalContext.current
            val database = PomodoroDatabase.getInstance(ctx)

            // 首页按返回键 → 退出 App
            // 对标 legacy: defaultNavHost="true" 自动拦截返回键
            // 对标 Flutter: WillPopScope
            androidx.activity.compose.BackHandler { onExitApp() }

            MainTabScreen(
                onEnterNavDemo = { navController.navigate("home") },  // 进入四级导航
                timerViewModel = viewModel {
                    TimerViewModel(database.pomodoroDao())
                }
            )
        }

        // ═══════════════════════════════════════════════════════════
        // 导航演示 第 1 级: 首页
        // ═══════════════════════════════════════════════════════════
        composable("home") {
            HomeScreen(
                onNavigate = { route -> navController.navigate(route) },
                onBack = { navController.popBackStack() }  // 回到番茄钟
            )
        }

        // ═══════════════════════════════════════════════════════════
        // 第 2 级: 列表页
        // ═══════════════════════════════════════════════════════════
        //
        // 纯字符串路由 "list"，不带参数
        // 对标传统: <fragment android:id="@+id/listFragment">
        composable("list") {
            ListScreen(
                onNavigate = { route -> navController.navigate(route) },
                onBack = { navController.popBackStack() }
                //            ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
            )
        }

        // ═══════════════════════════════════════════════════════════
        // 第 3 级: 详情页（带路由参数 itemId）
        // ═══════════════════════════════════════════════════════════
        //
        // 路由 "detail/{itemId}" = 带参数的路由
        // {itemId} = 占位符，调用时传 "detail/123" → itemId = "123"
        //
        // 对标传统 Java:
        //   <fragment android:id="@+id/detailFragment">
        //     <argument android:name="itemId" app:argType="string"/>
        //   </fragment>
        //   navController.navigate(R.id.to_detail, bundleOf("itemId" to "123"))
        //
        // navArgument = 声明参数类型
        //   NavType.StringType = 字符串类型（也有 IntType, LongType 等）
        //   nullable = false = 不能为 null
        composable(
            route = "detail/{itemId}",
            arguments = listOf(
                navArgument("itemId") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            // backStackEntry = 当前页面的路由栈条目
            // arguments?.getString("itemId") = 取出路由参数
            // 对标传统: val itemId = arguments?.getString("itemId")
            val itemId = backStackEntry.arguments?.getString("itemId") ?: ""

            DetailScreen(
                itemId = itemId,
                onNavigate = { route -> navController.navigate(route) },
                onBack = { navController.popBackStack() }
            )
        }

        // ═══════════════════════════════════════════════════════════
        // 第 4 级: 总结页（带路由参数 itemId）— 导航链条终点
        // ═══════════════════════════════════════════════════════════
        //
        // 总结页是 4 级导航的终点，不是番茄钟。
        // 番茄钟在首页（第0级的页面里），避免了循环。
        composable(
            route = "comment/{itemId}",
            arguments = listOf(
                navArgument("itemId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: ""

            CommentScreen(
                itemId = itemId,
                onBack = { navController.popBackStack() },
                // onBackToHome: popBackStack 到 "main" 路由，清空中间所有页面
                // inclusive = false: 保留 main 页面（回到首页 Tab 容器）
                onBackToHome = {
                    navController.popBackStack("main", false)
                }
            )
        }
    }
}
