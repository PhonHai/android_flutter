package com.pomodoro.legacy.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.pomodoro.legacy.R;
import java.util.Locale;

/**
 * 首页第 1 个 Tab：番茄钟 Fragment
 *
 * 【位置变化】
 * 原来它是 Navigation Component 的 startDestination，App 启动直接显示它。
 * 现在首页改成 TabContainerFragment（底部 4 个 Tab），
 * TimerFragment 变成 TabContainerFragment 的第一个子 Tab Fragment。
 *
 * 【传统 Java 思维 — Handler + Runnable 倒计时】
 *
 * 这是你 8 年经验最熟悉的写法：
 *   - findViewById 找控件
 *   - setOnClickListener 设点击事件
 *   - Handler + Runnable 做循环计时
 *   - tvTime.setText() 手动更新 UI
 *   - handler.removeCallbacks() 停止计时
 *
 * 对比 Compose 版 (TimerScreen.kt):
 *   - 没有 XML：全部用 @Composable 代码写 UI
 *   - 没有 findViewById：状态变自动重组
 *   - 没有 Handler：用 Coroutines delay
 *   - 没有 setText：改 _uiState.value → Compose 自动更新
 *
 * 对比 Flutter 版 (timer_widget.dart):
 *   - 没有 XML：全部用 Widget 代码写
 *   - 没有 Handler：用 Timer.periodic
 *   - 没有 setText：改 state = copyWith() → Widget 自动重绘
 */
public class TimerFragment extends Fragment {

    // ===== UI 控件（通过 findViewById 绑定）=====
    private TextView tvTime;
    private TextView tvSessionCount;
    private Button btnStart;
    private Button btnReset;
    private Button btn15, btn25, btn45;

    // ===== 计时状态（散落在 Fragment 里的成员变量）=====
    private int totalSeconds = 25 * 60;
    private int remainingSeconds = 25 * 60;
    private int completedSessions = 0;
    private boolean isRunning = false;

    // ===== Handler + Runnable 倒计时 =====
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable tickRunnable = new Runnable() {
        @Override
        public void run() {
            if (remainingSeconds > 0) {
                remainingSeconds--;
                updateUI();
                handler.postDelayed(this, 1000);
            } else {
                onFinished();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ===== findViewById 绑定控件 =====
        tvTime = view.findViewById(R.id.tv_time);
        tvSessionCount = view.findViewById(R.id.tv_session_count);
        btnStart = view.findViewById(R.id.btn_start);
        btnReset = view.findViewById(R.id.btn_reset);
        btn15 = view.findViewById(R.id.btn_15min);
        btn25 = view.findViewById(R.id.btn_25min);
        btn45 = view.findViewById(R.id.btn_45min);

        final NavController navController = Navigation.findNavController(view);

        // ===== 计时按钮 =====
        btnStart.setOnClickListener(v -> {
            if (isRunning) pauseTimer();
            else startTimer();
        });
        btnReset.setOnClickListener(v -> resetTimer());
        btn15.setOnClickListener(v -> setDuration(15));
        btn25.setOnClickListener(v -> setDuration(25));
        btn45.setOnClickListener(v -> setDuration(45));

        // ===== 「进入 4 级导航演示」入口按钮 =====
        // 导航: TimerFragment(在 TabContainer 内) → homeFragment
        // 注意：TimerFragment 现在是 TabContainerFragment 的子 Fragment，
        // 但 Navigation.findNavController(view) 会沿着父 Fragment 链找到 NavHostFragment 的 NavController。
        // 直接 navigate 到 R.id.homeFragment，NavController 会把 TabContainerFragment
        // 整个替换为 homeFragment，实现全屏四级导航。
        // 对应 Compose: navController.navigate("home")
        // 对应 Flutter: Navigator.pushNamed(context, '/nav_home')
        Button btnNavDemo = view.findViewById(R.id.btn_nav_demo);
        btnNavDemo.setOnClickListener(v -> {
            navController.navigate(R.id.homeFragment);
        });

        updateUI();
    }

    // ===== 计时逻辑 =====
    private void startTimer() {
        if (remainingSeconds <= 0) remainingSeconds = totalSeconds;
        isRunning = true;
        handler.postDelayed(tickRunnable, 1000);
        updateButtonStates();
    }

    private void pauseTimer() {
        isRunning = false;
        handler.removeCallbacks(tickRunnable);
        updateButtonStates();
    }

    private void resetTimer() {
        isRunning = false;
        handler.removeCallbacks(tickRunnable);
        remainingSeconds = totalSeconds;
        updateUI();
        updateButtonStates();
    }

    private void setDuration(int minutes) {
        if (isRunning) return;
        totalSeconds = minutes * 60;
        remainingSeconds = totalSeconds;
        updateUI();
    }

    private void onFinished() {
        isRunning = false;
        completedSessions++;
        updateUI();
        updateButtonStates();
    }

    // ===== UI 更新 =====
    private void updateUI() {
        int m = remainingSeconds / 60;
        int s = remainingSeconds % 60;
        tvTime.setText(String.format(Locale.getDefault(), "%02d:%02d", m, s));
        tvSessionCount.setText(String.format(Locale.getDefault(),
                "已完成 %d 个番茄", completedSessions));
    }

    private void updateButtonStates() {
        btnStart.setText(isRunning ? "暂停" : (remainingSeconds <= 0 ? "开始" : "继续"));
        btnReset.setEnabled(!isRunning && remainingSeconds < totalSeconds);
        btn15.setEnabled(!isRunning);
        btn25.setEnabled(!isRunning);
        btn45.setEnabled(!isRunning);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(tickRunnable);
    }
}
