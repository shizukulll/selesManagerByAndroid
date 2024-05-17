package com.example.selesmanager.Activity;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.selesmanager.R;
import com.example.selesmanager.pojo.Adapter.ProductAdapter;
import com.example.selesmanager.pojo.Product;
import com.example.selesmanager.util.Get;

import java.io.IOException;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements ProductAdapter.OnItemClickListener{


    RecyclerView recyclerView;
    ProductAdapter adapter;
    EditText editText;
    RadioButton radioButton,radioButton2;
    Button searchButton ,backButton;
    String searchUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);

        searchButton = findViewById(R.id.button4);
        backButton = findViewById(R.id.backButton);

        editText = findViewById(R.id.editTextText);

        radioButton = findViewById(R.id.radioButton);
        radioButton2 = findViewById(R.id.radioButton2);

        radioButton.setChecked(true); // 默认选择“使用id”单选按钮

        recyclerView = findViewById(R.id.seach_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        adapter = new ProductAdapter(SearchActivity.this, this);
        recyclerView.setAdapter(adapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 设置“使用id”单选按钮为选中状态
                radioButton.setChecked(true);
                // 取消选中“使用名称”单选按钮
                radioButton2.setChecked(false);
                // 在这里实现根据ID使用的逻辑
                // 例如，更新你的 getProductById 方法以使用ID
                // getProductById(url, id);
            }
        });

        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                radioButton2.setChecked(true);

                radioButton.setChecked(false);

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioButton.isChecked()){
                    SharedPreferences prefs = getSharedPreferences("IPCFG", MODE_PRIVATE);
                    searchUrl = "http://" + prefs.getString("ip","49.232.17.181") + ":" + prefs.getString("port","8080") + "/mobile/product/readById";

                    if(isValidInteger(editText.getText().toString())){
                        int id = Integer.parseInt(editText.getText().toString());

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Get get = new Get();
                                try {
                                    Product product = get.getProductById(searchUrl,id);
                                    // 在主线程上更新 RecyclerView 数据
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.setProduct(product);
                                        }
                                    });
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }).start();
                    }
                } else if(radioButton2.isChecked()){
                    SharedPreferences prefs = getSharedPreferences("IPCFG", MODE_PRIVATE);
                    searchUrl = "http://" + prefs.getString("ip","49.232.17.181") + ":" + prefs.getString("port","8080") + "/mobile/product/readByName";
                    String name = editText.getText().toString();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Get get = new Get();
                            try {
                                List<Product> productList = get.getProductByName(searchUrl,name);
                                // 在主线程上更新 RecyclerView 数据
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.setProducts(productList);
                                    }
                                });
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }).start();
                }
            }
        });


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

                    SharedPreferences prefs = getSharedPreferences("IPCFG", Context.MODE_PRIVATE);
                    String apiUrl = "http://" + prefs.getString("ip", "49.232.17.181") + ":" + prefs.getString("port", "8080") + "/mobile/product/delete";

                    // 在子线程中执行网络请求
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Get get = new Get();
                            final String str = get.delete(apiUrl, productId);
                            // 在主线程中处理网络请求结果
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if ("true".equals(str)) {
                                        adapter.deleteProduct(position); // 删除适配器中的对应项目
                                        Toast.makeText(SearchActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                    } else if ("false".equals(str))
                                        Toast.makeText(SearchActivity.this, "删除失败，服务器可能出了点问题", Toast.LENGTH_SHORT).show();
                                    else {
                                        Toast.makeText(SearchActivity.this, "没有预期的错误，服务器可能出了点问题", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).start();
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public boolean isValidInteger(String input) {
        return input.matches("^-?\\d+$");
    }

    @Override
    public void onItemClick(int position) {
        int productId = adapter.getProductIdAtPosition(position);
        SharedPreferences.Editor editor = getSharedPreferences("PRODUCTID", MODE_PRIVATE).edit();
        editor.putInt("id", productId);
        editor.apply();
        Intent intent = new Intent(SearchActivity.this, UpdateActivity.class);
        startActivity(intent);
        finish();
    }
}