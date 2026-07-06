package com.pomodoro.jetpack.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pomodoro.jetpack.ui.comment.CommentScreen
import com.pomodoro.jetpack.ui.detail.DetailScreen
import com.pomodoro.jetpack.ui.home.HomeScreen
import com.pomodoro.jetpack.ui.list.ListScreen
import com.pomodoro.jetpack.ui.main.MainTabScreen

/**
 * ═══════════════════════════════════════════════════════════
 * 导航图 — NavHost 路由表
 * ═══════════════════════════════════════════════════════════
 *
 * 路由结构：
 *   "main"                     → MainTabScreen（4 个 Tab：番茄钟/文件/设置/传输）
 *   "home"                     → HomeScreen（4 级导航演示 第1级）
 *   "list"                     → ListScreen（第2级）
 *   "detail/{itemId}"          → DetailScreen（第3级，带参数）
 *   "comment/{itemId}"         → CommentScreen（第4级，带参数）
 *
 * 注入变化：
 *   之前：AppNavHost 手动创建 DAO 并传给 ViewModel 构造函数
 *   现在：ViewModel 用 @HiltViewModel + @Inject，Hilt 自动注入
 *        AppNavHost 不再需要创建 DAO 实例
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    onExitApp: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        // ═══════════ 首页 Tab 容器 ═══════════
        composable("main") {
            BackHandler { onExitApp() }

            // ViewModel 由 Hilt 自动注入，不再手动传 DAO
            MainTabScreen(
                navController = navController,
                onExitApp = onExitApp
            )
        }

        // ═══════════ 4 级导航演示 第 1 级: 首页 ═══════════
        composable("home") {
            HomeScreen(
                onNavigate = { route -> navController.navigate(route) },
                onBack = { navController.popBackStack() }
            )
        }

        // ═══════════ 第 2 级: 列表页 ═══════════
        composable("list") {
            ListScreen(
                onNavigate = { route -> navController.navigate(route) },
                onBack = { navController.popBackStack() }
            )
        }

        // ═══════════ 第 3 级: 详情页（带参数 itemId） ═══════════
        composable(
            route = "detail/{itemId}",
            arguments = listOf(
                navArgument("itemId") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
            DetailScreen(
                itemId = itemId,
                onNavigate = { route -> navController.navigate(route) },
                onBack = { navController.popBackStack() }
            )
        }

        // ═══════════ 第 4 级: 总结页（带参数 itemId） ═══════════
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
                onBackToHome = {
                    navController.popBackStack("main", false)
                }
            )
        }
    }
}