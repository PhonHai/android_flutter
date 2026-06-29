package com.pomodoro.jetpack.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController

/**
 * ═══════════════════════════════════════════════════════════
 * 主 Activity — NavHost 宿主
 * ═══════════════════════════════════════════════════════════
 *
 * 【对比之前的 if/else 方案】
 *
 * 之前（if/else）:
 *   setContent {
 *     var page by remember { mutableStateOf("timer") }
 *     when(page) {
 *       "timer" -> TimerScreen(onViewHistory = { page = "history" })
 *       "history" -> HistoryScreen(onBack = { page = "timer" })
 *     }
 *   }
 *
 * 现在（NavHost）:
 *   setContent {
 *     val navController = rememberNavController()
 *     AppNavHost(navController, onExitApp = { finish() })
 *   }
 *
 * 区别:
 *   1. 不再需要手动维护 page 变量 → NavController 自动管理
 *   2. 系统返回键自动 popBackStack → if/else 方案做不到
 *   3. 路由参数自动传递 → "detail/{itemId}" 优雅传值
 *   4. 路由表集中在 AppNavHost.kt → 结构清晰
 *
 * 【传统 Java 思维 → Compose 映射】
 *
 * 传统 Java:
 *   public class MainActivity extends AppCompatActivity {
 *     protected void onCreate(...) {
 *       setContentView(R.layout.activity_main);
 *       // activity_main.xml 里有个 NavHostFragment
 *       // <fragment android:name="...NavHostFragment"
 *       //           app:navGraph="@navigation/nav_graph"/>
 *       NavController nav = findNavController(R.id.nav_host);
 *     }
 *   }
 *
 * Jetpack Compose:
 *   class MainActivity : ComponentActivity() {
 *     override fun onCreate(...) {
 *       setContent {
 *         val navController = rememberNavController()  // ← 创建 NavController
 *         AppNavHost(navController, ...)               // ← 装载路由图
 *       }
 *     }
 *   }
 *
 * 对比 Flutter:
 *   MaterialApp(
 *     home: HomePage(),
 *     onGenerateRoute: (settings) {
 *       switch (settings.name) {
 *         case 'list': return MaterialPageRoute(builder: (_) => ListPage());
 *         case 'detail': return MaterialPageRoute(builder: (_) => DetailPage(settings.arguments));
 *       }
 *     },
 *   )
 */
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
