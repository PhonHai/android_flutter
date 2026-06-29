package com.pomodoro.legacy.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.pomodoro.legacy.R;

/**
 * 第 1 级：首页 Fragment
 *
 * 【传统 Java 思维 — Fragment 写法】
 *
 * Fragment 是 Android 传统的页面单元（在 Compose 之前）:
 *   - onCreateView() 创建布局
 *   - onViewCreated() 初始化控件
 *   - 通过 NavController 跳转
 *
 * 对比 Jetpack Compose 版:
 *   @Composable
 *   fun HomeScreen(onNavigate: (String) -> Unit) {
 *     Button(onClick = { onNavigate("list") }) { ... }
 *   }
 *
 * 对比 Flutter 版:
 *   class HomePage extends StatelessWidget {
 *     Widget build(context) {
 *       return ElevatedButton(
 *         onPressed: () => Navigator.push(context, ...),
 *       );
 *     }
 *   }
 */
public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // 加载布局 fragment_home.xml
        // 对标 Compose: 不需要 XML，直接写 Composable
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 找到按钮
        // 对标 Compose: 不需要 findViewById，直接在 Composable 里写
        Button btnList = view.findViewById(R.id.btn_to_list);

        // 获取 NavController
        // Navigation.findNavController(view) = 从 View 找到所属的 NavHostFragment
        // 对标 Compose: val navController = rememberNavController()
        NavController navController = Navigation.findNavController(view);

        // 设置点击事件 → 跳转到列表页
        // navController.navigate(R.id.action_home_to_list)
        //   = 执行 nav_graph.xml 里定义的 action
        //   对标 Compose: navController.navigate("list")
        btnList.setOnClickListener(v -> {
            navController.navigate(R.id.action_home_to_list);
        });
    }
}
