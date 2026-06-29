package com.pomodoro.legacy.ui.stats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.pomodoro.legacy.R;

/**
 * 第 2 个 Tab：统计页
 *
 * 目前是空白占位 Fragment，用于演示底部导航切换。
 * 实际项目中可以在这里放番茄钟完成次数、专注时长等统计图表。
 *
 * 传统 Java 思维：
 *   - 和 TimerFragment 一样 extends Fragment
 *   - 通过 onCreateView 返回 XML 布局
 *   - 通过 findViewById 找控件
 */
public class StatsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }
}
