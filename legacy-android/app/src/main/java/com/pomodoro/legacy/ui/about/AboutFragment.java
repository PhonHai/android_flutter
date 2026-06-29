package com.pomodoro.legacy.ui.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.pomodoro.legacy.R;

/**
 * 第 4 个 Tab：关于页
 *
 * 目前是空白占位 Fragment，用于演示底部导航切换。
 * 实际项目中可以在这里放应用版本、开发者信息、隐私政策等。
 */
public class AboutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }
}
