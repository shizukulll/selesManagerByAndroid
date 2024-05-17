package com.example.selesmanager.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.selesmanager.Activity.INFOActivity;
import com.example.selesmanager.Activity.UpdateActivity;
import com.example.selesmanager.R;
import com.example.selesmanager.pojo.Product;
import com.example.selesmanager.util.Get;
import com.example.selesmanager.pojo.Adapter.ProductAdapter;

import java.io.IOException;
import java.util.List;

import androidx.recyclerview.widget.ItemTouchHelper;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ProductAdapter(getContext(), new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showItemDetails(position);
            }
        });
        recyclerView.setAdapter(adapter);

        // 获取产品并设置到 RecyclerView 中
        getAllProducts();

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // 获取被滑动的项目的位置
                int position = viewHolder.getAdapterPosition();
                int productId = adapter.getProductIdAtPosition(position);

                // 获取滑动的方向
                if (direction == ItemTouchHelper.LEFT) {
                    // 左滑操作，例如删除项目

                    SharedPreferences prefs = getActivity().getSharedPreferences("IPCFG", MODE_PRIVATE);
                    String apiUrl = "http://" + prefs.getString("ip", "49.232.17.181") + ":" + prefs.getString("port", "8080") + "/mobile/product/delete";

                    // 在子线程中执行网络请求
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Get get = new Get();
                            final String str = get.delete(apiUrl, productId);
                            // 在主线程中处理网络请求结果
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if ("true".equals(str)) {
                                        adapter.deleteProduct(position); // 删除适配器中的对应项目
                                        Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                    } else if ("false".equals(str)) {
                                        Toast.makeText(getContext(), "删除失败，服务器可能出了点问题", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "没有预期的错误，服务器可能出了点问题", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).start();
                }
                if (direction == ItemTouchHelper.RIGHT){
                    Toast.makeText(getContext(), "隐藏成功", Toast.LENGTH_SHORT).show();
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    private void getAllProducts() {
        // 在新线程中获取产品数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences prefs = getActivity().getSharedPreferences("IPCFG", MODE_PRIVATE);
                    String apiUrl = "http://" + prefs.getString("ip", "49.232.17.181") + ":" + prefs.getString("port", "8080") + "/mobile/product/readAll";

                    List<Product> productList = new Get().getAllProducts(apiUrl);

                    // 在UI线程更新UI
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 设置产品列表到RecyclerView适配器
                            adapter.setProducts(productList);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 显示错误信息
                            Toast.makeText(getContext(), "Error occurred while getting products.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void showItemDetails(int position) {
        // 显示项目详细信息的逻辑，例如弹出对话框或导航到详细信息页面
        int productId = adapter.getProductIdAtPosition(position);
        // 这里可以实现显示详细信息的逻辑
//        Toast.makeText(getContext(), "显示项目详细信息: " + productId, Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor editor =  getActivity().getSharedPreferences("PRODUCTID", MODE_PRIVATE).edit();
        editor.putInt("id",productId);
        editor.apply();
        Intent intent = new Intent(getActivity(), UpdateActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
