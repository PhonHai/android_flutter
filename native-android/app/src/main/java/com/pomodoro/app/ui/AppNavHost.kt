package com.pomodoro.app.ui

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pomodoro.app.FlutterContainerActivity
import com.pomodoro.app.ui.comment.CommentScreen
import com.pomodoro.app.ui.detail.DetailScreen
import com.pomodoro.app.ui.home.HomeScreen
import com.pomodoro.app.ui.list.ListScreen
import com.pomodoro.app.ui.main.MainTabScreen

/**
 * 4 级导航图 + 首页 Tab — 原生壳版本
 *
 * 对标 jetpack-android 的 AppNavHost
 *
 * 导航链路:
 *   main (底部 4 Tab) → home → list → detail/{id} → comment/{id}
 *
 * Tab 1 的内容:
 *   - 「启动 Flutter 番茄钟」按钮 → startActivity(FlutterContainerActivity)
 *   - 「进入 4 级导航演示」按钮 → navController.navigate("home")
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
        // 首页 Tab 容器
        composable("main") {
            val ctx = LocalContext.current
            BackHandler { onExitApp() }

            MainTabScreen(
                onStartFlutter = {
                    // 启动 Flutter 番茄钟 Activity
                    ctx.startActivity(Intent(ctx, FlutterContainerActivity::class.java))
                },
                onEnterNavDemo = {
                    navController.navigate("home")
                }
            )
        }

        // 第 2 级: 导航演示首页
        composable("home") {
            HomeScreen(
                onNavigate = { route -> navController.navigate(route) },
                onBack = { navController.popBackStack() }
            )
        }

        // 第 3 级: 列表页
        composable("list") {
            ListScreen(
                onNavigate = { route -> navController.navigate(route) },
                onBack = { navController.popBackStack() }
            )
        }

        // 第 4 级: 详情页（带参数 itemId）
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

        // 第 5 级: 总结页
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
