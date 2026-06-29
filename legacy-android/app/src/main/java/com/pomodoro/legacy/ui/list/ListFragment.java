package com.pomodoro.legacy.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.pomodoro.legacy.R;
import java.util.ArrayList;
import java.util.List;

/**
 * 第 2 级：列表页 Fragment
 *
 * 【传统 Java 思维 — 带参数跳转】
 *
 * 从列表跳详情，需要传 itemId:
 *   传统 Java: Bundle args = new Bundle();
 *              args.putString("itemId", itemId);
 *              navController.navigate(R.id.action_list_to_detail, args);
 *
 * 对比 Compose: navController.navigate("detail/$itemId")
 *   ← 字符串拼接路由，不用 Bundle
 *
 * 对比 Flutter: Navigator.push(context, MaterialPageRoute(
 *                 builder: (_) => DetailPage(itemId: itemId),
 *               ));
 *   ← 构造函数传参
 */
public class ListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);
        ListView listView = view.findViewById(R.id.list_view);

        // 模拟 5 条数据
        List<String> items = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            items.add("条目 " + i);
        }

        // ArrayAdapter = 最简单的列表适配器
        // 对标 Compose: LazyColumn { items(list) { ListItem(it) } }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        // 点击列表项 → 跳转到详情页（带参数）
        // 对标 Compose: onNavigate("detail/$itemId")
        listView.setOnItemClickListener((parent, v, position, id) -> {
            String itemId = String.valueOf(position + 1);

            // ═══════════ 关键：带参数跳转 ═══════════
            //
            // 传统 Java 用 Bundle 传参:
            //   Bundle args = new Bundle();
            //   args.putString("itemId", itemId);
            //   navController.navigate(R.id.action_list_to_detail, args);
            //
            // 对比 Compose:
            //   navController.navigate("detail/$itemId")
            //   ← 路由字符串拼接，NavHost 自动提取参数
            //
            // 对比 Flutter:
            //   Navigator.push(context, MaterialPageRoute(
            //     builder: (_) => DetailPage(itemId: itemId),
            //   ));
            //   ← 构造函数传参

            // 用 Bundle 传参（最通用的传统方式）
            Bundle args = new Bundle();
            args.putString("itemId", itemId);
            navController.navigate(R.id.action_list_to_detail, args);
        });
    }
}
