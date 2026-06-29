package com.pomodoro.legacy.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.pomodoro.legacy.R;

/**
 * 第 3 个 Tab：设置页
 *
 * 目前是空白占位 Fragment，用于演示底部导航切换。
 * 实际项目中可以在这里放番茄钟时长、提醒音、主题等设置项。
 */
public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
}
