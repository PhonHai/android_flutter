package com.pomodoro.legacy.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import com.pomodoro.legacy.R;

/**
 * 主 Activity — NavHost 宿主
 *
 * 【传统 Java 思维 → Navigation Component】
 *
 * 这个 Activity 只做一件事: 装载 NavHostFragment
 * 所有页面切换由 NavController 管理（不需要手动 FragmentTransaction）
 *
 * 【⚠️ 重要：为什么不能在 onCreate 里拿 NavController？】
 *
 * Navigation.findNavController(view) 要求 NavHostFragment 已经完成初始化。
 * NavHostFragment 在 setContentView 之后异步加载，onCreate 执行时
 * FragmentContainerView 里的 NavHostFragment 还没准备好。
 *
 * 正确的做法：在需要的时候（onSupportNavigateUp / Fragment 里）再获取。
 *
 * 对比 Jetpack Compose 版:
 *   val navController = rememberNavController()
 *   ← rememberNavController 是惰性的，第一次取时才创建
 *   ← 所以 Compose 版没有这个时序问题
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ⚠️ 不能在这里调用 Navigation.findNavController！
        // NavHostFragment 此时还未加载完成，会抛出 IllegalStateException。
        // NavController 会在 Fragment 的 onViewCreated 里获取。
        // 对应 Compose: rememberNavController() 是惰性的，第一次 build 才创建
    }

    /**
     * 拦截返回键 — 让 NavController 自动处理
     *
     * ⚠️ 迟延获取 NavController，确保 NavHostFragment 已加载完成
     * 
     * NavController 默认支持返回栈:
     *   番茄钟(根) → 导航演示首页 → 列表 → 详情 → 总结
     *   系统返回键: 逐级 popBackStack
     *   在番茄钟(根)按返回: finish() 退出 App
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation
            .findNavController(this, R.id.nav_host_container);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}
