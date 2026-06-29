package com.pomodoro.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.pomodoro.app.ui.AppNavHost

/**
 * 主 Activity — 首页 Tab 容器 + 4 级导航宿主
 *
 * 【对标 legacy-android 的 MainActivity】
 * legacy-android: AppCompatActivity + NavHostFragment (XML)
 * native-android: ComponentActivity + NavHost (Compose)
 *
 * 【对标 jetpack-android 的 MainActivity】
 * 完全一样的结构：setContent → MaterialTheme → Surface → AppNavHost
 *
 * 【与之前版本的差异】
 * 之前: 简单的两个按钮页面（启动 Flutter / 历史记录）
 * 现在: 底部 4 Tab（番茄钟/统计/设置/关于）
 *   - Tab 1: 启动 Flutter 番茄钟 + 4 级导航演示入口
 *   - Tab 2-4: 空白占位
 *   - 4 级导航: Compose NavHost（home → list → detail → comment）
 *
 * 【Flutter 模块仍然可用】
 * 点击「启动 Flutter 番茄钟」→ startActivity(FlutterContainerActivity)
 * Flutter 模块的计时器 UI 不变，只是入口换到了 Tab 1 里面。
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    AppNavHost(
                        navController = navController,
                        onExitApp = { finish() }
                    )
                }
            }
        }
    }
}
