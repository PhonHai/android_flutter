package com.pomodoro.jetpack.ui

import android.os.Bundle
import android.view.Surface
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint

/**
 * ═══════════════════════════════════════════════════════════
 * 主 Activity — NavHost 宿主 + Hilt 注入入口
 * ═══════════════════════════════════════════════════════════
 *
 * @AndroidEntryPoint: Hilt 注入标记，使 Activity 可被 Hilt 管理。
 * 所有 @HiltViewModel 标记的 ViewModel 从此 Activity 的容器获取依赖。
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {

                    // ═══════════ rememberNavController ═══════════
                    //
                    // 创建 NavController，用 remember 包裹：
                    //   1. Activity 重建（如旋转屏幕）时复用同一个实例
                    //   2. 绑定到当前 Composable 生命周期
                    //
                    // 对标传统 Java:
                    //   val navController = findNavController(R.id.nav_host)
                    //   ← 从 XML 找 NavHostFragment，本质一样
                    //
                    // 对比 Flutter:
                    //   Flutter 没有全局 NavController 概念
                    //   每个 Navigator.push 调用都隐式管理栈

                    // 管理导航状态的“大脑”
                    // 1.记录导航历史（你从哪个页面来，现在在哪个页面）
                    // 2.控制页面跳转（前进、后退、替换）
                    // 3.传递页面参数
                    val navController = rememberNavController()

                    // ═══════════ AppNavHost ═══════════
                    //
                    // 装载路由图，传入 NavController
                    //
                    // onExitApp = { finish() }
                    //   ← 首页按返回键时退出 App
                    //   ← 对标传统: Activity.onBackPressed() → super.onBackPressed() → finish()
                    AppNavHost(
                        navController = navController,
                        onExitApp = { finish() }
                    )
                }
            }
        }
    }
}
