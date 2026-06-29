package com.pomodoro.legacy.ui.detail;

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
 * 第 3 级：详情页 Fragment — 接收参数 + 跳转评论页
 *
 * 【传统 Java 思维 — 接收参数】
 *
 * 接收上一页传来的参数:
 *   String itemId = getArguments().getString("itemId");
 *
 * 对比 Compose:
 *   fun DetailScreen(itemId: String, ...) { ... }
 *   ← 参数直接在函数签名里，不用 getArguments()
 *
 * 对比 Flutter:
 *   class DetailPage extends StatelessWidget {
 *     final String itemId;
 *     DetailPage({required this.itemId});  ← 构造函数接收
 *   }
 */
public class DetailFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvItemId = view.findViewById(R.id.tv_item_id);
        Button btnComment = view.findViewById(R.id.btn_to_comment);
        NavController navController = Navigation.findNavController(view);

        // ═══════════ 接收参数 ═══════════
        //
        // getArguments() = 获取上一页传来的 Bundle
        // getString("itemId") = 取出 itemId 参数
        //
        // 对比 Compose:
        //   val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
        //   ← NavHost 自动提取路由参数
        //
        // 对比 Flutter:
        //   final String itemId;  ← 构造函数字段，直接用
        String itemId = "";
        if (getArguments() != null) {
            itemId = getArguments().getString("itemId", "");
        }
        tvItemId.setText("条目 ID: " + itemId);

        // 必须用 final 变量才能在 lambda 里引用
        final String finalItemId = itemId;

        // 跳转到评论页，把 itemId 传过去
        btnComment.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("itemId", finalItemId);
            navController.navigate(R.id.action_detail_to_comment, args);
        });
    }
}
