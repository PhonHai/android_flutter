package com.pomodoro.legacy.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.pomodoro.legacy.R;
import com.pomodoro.legacy.ui.about.AboutFragment;
import com.pomodoro.legacy.ui.settings.SettingsFragment;
import com.pomodoro.legacy.ui.stats.StatsFragment;

/**
 * 首页 Tab 容器 — 传统底部导航栏切换 Fragment
 *
 * 【为什么需要这个 Fragment？】
 * 用户要求首页改成"4 个 Fragment 可以互相切换"的传统界面。
 * 但项目已经用 Navigation Component 做好了四级导航。
 * 为了"改动最小"，我们把 Tab 容器本身作为一个 Navigation 的 startDestination。
 * 这样：
 *   - 启动 App → 看到 TabContainerFragment（底部 4 个 Tab）
 *   - 在"番茄钟"Tab 里点击"进入 4 级导航演示" → NavController 把 TabContainerFragment 整个替换为四级导航
 *   - 四级导航结束 → popBackStack 回到 TabContainerFragment
 *
 * 【传统 Java 思维】
 * 这个 Fragment 内部就是经典写法：
 *   - getChildFragmentManager() 管理子 Fragment
 *   - FragmentTransaction 的 add/hide/show 切换 Tab
 *   - 底部 4 个 TextView 当作 Tab 按钮
 *
 * 对比 Jetpack Compose 版：
 *   Compose 会用 Scaffold + BottomNavigation + NavHost 组合
 *   但这里用传统 XML + FragmentTransaction 手动管理，更直观。
 */
public class TabContainerFragment extends Fragment {

    private static final int TAB_POMODORO = 0;   // 番茄钟
    private static final int TAB_STATS = 1;      // 统计
    private static final int TAB_SETTINGS = 2;   // 设置
    private static final int TAB_ABOUT = 3;    // 关于

    // 底部 4 个 Tab 按钮
    private TextView tvTabPomodoro;
    private TextView tvTabStats;
    private TextView tvTabSettings;
    private TextView tvTabAbout;

    // 4 个 Tab 对应的 Fragment
    private TimerFragment timerFragment;
    private StatsFragment statsFragment;
    private SettingsFragment settingsFragment;
    private AboutFragment aboutFragment;

    // 当前显示的 Tab 索引和 Fragment（用于屏幕旋转后恢复）
    private int currentTabIndex = TAB_POMODORO;
    private Fragment currentFragment;

    private static final String KEY_CURRENT_TAB = "current_tab_index";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_container, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 恢复屏幕旋转前选中的 Tab
        if (savedInstanceState != null) {
            currentTabIndex = savedInstanceState.getInt(KEY_CURRENT_TAB, TAB_POMODORO);
        }

        initViews(view);
        initTabs(savedInstanceState);

        // 显示当前 Tab
        switchTab(currentTabIndex);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // 保存当前选中的 Tab，屏幕旋转后恢复
        outState.putInt(KEY_CURRENT_TAB, currentTabIndex);
    }

    /**
     * 初始化 Tab 按钮点击事件
     */
    private void initViews(View view) {
        tvTabPomodoro = view.findViewById(R.id.tab_pomodoro);
        tvTabStats = view.findViewById(R.id.tab_stats);
        tvTabSettings = view.findViewById(R.id.tab_settings);
        tvTabAbout = view.findViewById(R.id.tab_about);

        tvTabPomodoro.setOnClickListener(v -> switchTab(TAB_POMODORO));
        tvTabStats.setOnClickListener(v -> switchTab(TAB_STATS));
        tvTabSettings.setOnClickListener(v -> switchTab(TAB_SETTINGS));
        tvTabAbout.setOnClickListener(v -> switchTab(TAB_ABOUT));
    }

    /**
     * 初始化 4 个 Tab Fragment
     *
     * 传统写法：add 所有 Fragment 到容器，默认只 show 第一个，其他 hide。
     * 这样切换时只需要 hide/show，不需要重新创建和 inflate 布局，性能更好。
     *
     * 注意：这里用 getChildFragmentManager() 而不是 getParentFragmentManager()
     * 因为这些 Fragment 是嵌套在 TabContainerFragment 内部的。
     * 对标 Activity：Activity 用 getSupportFragmentManager()，这里用 getChildFragmentManager()
     */
    /**
     * 初始化 4 个 Tab Fragment
     *
     * 【崩溃修复】
     * 之前的逻辑用 savedInstanceState 来判断是否需要 add Fragment。
     * 但 popBackStack 回来时 savedInstanceState == null，而 ChildFragmentManager 里
     * 已经保留了旧的 Fragment 实例，导致 "Fragment already added" 崩溃。
     *
     * 修复方案：始终先用 findFragmentByTag 查找已存在的实例。
     * 只有当所有 4 个 Fragment 都找不到时，才 new + add。
     * 这样可以同时兼容：
     *   - 首次创建（都没有 → new + add）
     *   - 屏幕旋转后恢复（FragmentManager 自动恢复 → findFragmentByTag 能找到）
     *   - popBackStack 回来（ChildFragmentManager 保留子 Fragment → findFragmentByTag 能找到）
     */
    private void initTabs(@Nullable Bundle savedInstanceState) {
        FragmentManager childFm = getChildFragmentManager();

        // 始终先尝试从 FragmentManager 里找已存在的实例
        // 场景 1：屏幕旋转恢复 → savedInstanceState != null，能通过 tag 找到
        // 场景 2：popBackStack 回来 → savedInstanceState == null，但 ChildFragmentManager 保留了子 Fragment
        timerFragment = (TimerFragment) childFm.findFragmentByTag("timer");
        statsFragment = (StatsFragment) childFm.findFragmentByTag("stats");
        settingsFragment = (SettingsFragment) childFm.findFragmentByTag("settings");
        aboutFragment = (AboutFragment) childFm.findFragmentByTag("about");

        // 只有全部都找不到时，才创建新的（首次启动）
        if (timerFragment == null && statsFragment == null
                && settingsFragment == null && aboutFragment == null) {
            FragmentTransaction ft = childFm.beginTransaction();

            timerFragment = new TimerFragment();
            statsFragment = new StatsFragment();
            settingsFragment = new SettingsFragment();
            aboutFragment = new AboutFragment();

            ft.add(R.id.child_container, timerFragment, "timer");
            ft.add(R.id.child_container, statsFragment, "stats");
            ft.add(R.id.child_container, settingsFragment, "settings");
            ft.add(R.id.child_container, aboutFragment, "about");

            // 首次创建时默认只显示番茄钟，其他隐藏
            ft.hide(statsFragment);
            ft.hide(settingsFragment);
            ft.hide(aboutFragment);

            ft.commit();
        }
    }

    /**
     * 切换 Tab
     *
     * 传统 Java 做法：FragmentTransaction.hide(当前) + show(目标)
     * 比 replace 更高效，因为不会重新创建 Fragment。
     */
    private void switchTab(int index) {
        Fragment target;
        switch (index) {
            case TAB_POMODORO:
                target = timerFragment;
                break;
            case TAB_STATS:
                target = statsFragment;
                break;
            case TAB_SETTINGS:
                target = settingsFragment;
                break;
            case TAB_ABOUT:
                target = aboutFragment;
                break;
            default:
                target = timerFragment;
        }

        // 点同一个 Tab 不用切换
        if (target == currentFragment) return;

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        if (currentFragment != null) {
            ft.hide(currentFragment);
        }
        ft.show(target);
        ft.commit();

        currentFragment = target;
        currentTabIndex = index;
        updateTabStyle(index);
    }

    /**
     * 更新底部 Tab 样式：选中黑色加粗，未选中灰色
     */
    private void updateTabStyle(int selectedIndex) {
        tvTabPomodoro.setTextColor(getColor(selectedIndex == TAB_POMODORO));
        tvTabPomodoro.setTypeface(Typeface.DEFAULT, selectedIndex == TAB_POMODORO ? Typeface.BOLD : Typeface.NORMAL);

        tvTabStats.setTextColor(getColor(selectedIndex == TAB_STATS));
        tvTabStats.setTypeface(Typeface.DEFAULT, selectedIndex == TAB_STATS ? Typeface.BOLD : Typeface.NORMAL);

        tvTabSettings.setTextColor(getColor(selectedIndex == TAB_SETTINGS));
        tvTabSettings.setTypeface(Typeface.DEFAULT, selectedIndex == TAB_SETTINGS ? Typeface.BOLD : Typeface.NORMAL);

        tvTabAbout.setTextColor(getColor(selectedIndex == TAB_ABOUT));
        tvTabAbout.setTypeface(Typeface.DEFAULT, selectedIndex == TAB_ABOUT ? Typeface.BOLD : Typeface.NORMAL);
    }

    private int getColor(boolean selected) {
        return selected ? 0xFF000000 : 0xFF888888;
    }
}
