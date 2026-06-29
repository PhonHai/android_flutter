package com.pomodoro.legacy.ui.comment;

import android.os.Bundle;
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

/**
 * 第 4 级：总结页 — 导航链条终点
 *
 * 【为什么第 4 级是总结页？】
 * 导航演示首页 → 列表 → 详情 → 总结页 ← 链条终点
 * 避免「番茄钟 Tab → 点 4 级 → 又是番茄钟」的循环。
 *
 * 【返回栈演示】
 *   - 「返回详情页」→ navController.popBackStack() 回到详情
 *   - 「回到首页」→ navController.popBackStack(R.id.tabContainerFragment, false)
 *                  清空 home/list/detail/comment，回到 TabContainerFragment（首页）
 *   - 系统返回键 → 逐级 pop
 */
public class CommentFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 接收参数
        String itemId = "";
        if (getArguments() != null) {
            itemId = getArguments().getString("itemId", "");
        }

        // 显示总结信息
        TextView tvTitle = view.findViewById(R.id.tv_summary_title);
        tvTitle.setText("条目 " + itemId + " 已完成");

        NavController navController = Navigation.findNavController(view);

        // 「返回详情页」按钮 — popBackStack 回到上一页
        Button btnBack = view.findViewById(R.id.btn_back_to_detail);
        btnBack.setOnClickListener(v -> navController.popBackStack());

        // 「回到首页」按钮 — popBackStack 到 TabContainerFragment，清空中间所有页面
        // 对标 Compose: navController.popBackStack("tabContainer", false)
        Button btnHome = view.findViewById(R.id.btn_back_to_home);
        btnHome.setOnClickListener(v -> {
            // popBackStack(id, false) = 回到指定页面，不 pop 它自己
            // 页面栈: tabContainer → home → list → detail → comment
            // 执行后: tabContainer（home/list/detail/comment 都被 pop 掉）
            navController.popBackStack(R.id.tabContainerFragment, false);
        });
    }
}
