package com.pomodoro.jetpack.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 第 1 级：首页 — App 入口
 *
 * 【传统 Java 思维 → Compose 映射】
 *
 * 传统 Java:
 *   public class HomeFragment extends Fragment {
 *     public View onCreateView(...) {
 *       View root = inflate(R.layout.fragment_home);
 *       Button btnList = root.findViewById(R.id.btn_list);
 *       btnList.setOnClickListener(v -> {
 *         NavController nav = Navigation.findNavController(v);
 *         nav.navigate(R.id.to_list);  ← 跳转到 ListFragment
 *       });
 *     }
 *   }
 *
 * Jetpack Compose:
 *   @Composable
 *   fun HomeScreen(onNavigate: (String) -> Unit) {
 *     Button(onClick = { onNavigate("list") }) { ... }
 *   }
 *
 * 对比 Flutter:
 *   class HomePage extends StatelessWidget {
 *     Widget build(context) {
 *       return ElevatedButton(
 *         onPressed: () => Navigator.push(context,
 *           MaterialPageRoute(builder: (_) => ListPage())),
 *         child: Text('查看列表'),
 *       );
 *     }
 *   }
 *
 * 【参数说明】
 * @param onNavigate 跳转回调，传入路由字符串（如 "list"）
 * @param onBack 返回回调（首页按返回键 = 退出 App）
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit,
    onBack: () -> Unit
) {
    // BackHandler = 拦截系统返回键
    // 对标传统 Java: Activity.onBackPressed()
    // NavHost 会自动 popBackStack，这里手动覆盖以防万一
    // ⚠️ 注意: NavHost 默认会处理返回键，如果 NavHost 没处理才会到这里
    androidx.activity.compose.BackHandler { onBack() }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("首页 (第 1 级)") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("4 级导航演示", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("点击下方按钮进入下一级", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(48.dp))

            // ═══════════ 按钮：跳转到列表页 ═══════════
            //
            // onNavigate("list") = 调用回调，传入路由字符串
            // AppNavHost 里 HomeScreen(onNavigate = { route -> navController.navigate(route) })
            //   → navController.navigate("list") = 跳转到 ListScreen
            //
            // 对标传统 Java:
            //   navController.navigate(R.id.to_list)
            //
            // 对比 Flutter:
            //   Navigator.push(context, MaterialPageRoute(builder: (_) => ListPage()))
            Button(
                onClick = { onNavigate("list") },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("进入列表 (第 2 级)", fontSize = 18.sp)
            }
        }
    }
}
