package com.example.selesmanager.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.selesmanager.R;
import com.example.selesmanager.pojo.Product;
import com.example.selesmanager.util.Get;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

public class UpdateActivity extends AppCompatActivity {
//    private EditText editTextId;
    private EditText editTextName;
    private EditText editTextPrice;
    private EditText editTextCount;
    private EditText editTextChang;
    private Button buttonSubmit;
    private FloatingActionButton backButton;
    Product product1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update);

//        editTextId = findViewById(R.id.upid);
        editTextName = findViewById(R.id.upname);
        editTextPrice = findViewById(R.id.upprice);
        editTextCount = findViewById(R.id.upcount);
        editTextChang = findViewById(R.id.upchang);
        buttonSubmit = findViewById(R.id.button7);
        backButton = findViewById(R.id.backToHome);
        product1 = new Product();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences prefs = getSharedPreferences("IPCFG", MODE_PRIVATE);
        String searchUrl = "http://" + prefs.getString("ip", "49.232.17.181") + ":" + prefs.getString("port", "8080") + "/mobile/product/readById";
        SharedPreferences prefs1 = getSharedPreferences("PRODUCTID", MODE_PRIVATE);
        int id = prefs1.getInt("id",0);
        if(id == 0){
            Toast.makeText(this, "id获取错误", Toast.LENGTH_SHORT).show();
        }

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
//                            editTextId.setText(String.valueOf(product.getId()));
                            editTextName.setText(product.getName());
                            editTextPrice.setText(String.valueOf(product.getPrice()));
                            editTextCount.setText(String.valueOf(product.getCount()));
                            editTextChang.setText(product.getBrand());
                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();


        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 从 EditText 中获取文本
//        String idText = editTextId.getText().toString().trim();
                String nameText = editTextName.getText().toString().trim();
                String priceText = editTextPrice.getText().toString().trim();
                String countText = editTextCount.getText().toString().trim();
                String brandText = editTextChang.getText().toString().trim();

                // 检查 EditText 是否为空
                if (nameText.isEmpty() || priceText.isEmpty() || countText.isEmpty() || brandText.isEmpty()) {
                    // 处理空输入的情况，例如显示错误消息或禁止提交

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UpdateActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                try {
                    // 将价格和数量文本转换为浮点数和整数
                    float price = Float.parseFloat(priceText);
                    int count = Integer.parseInt(countText);

                    // 创建 Product 对象

                    product1.setId(id);
                    product1.setName(nameText);
                    product1.setPrice(price);
                    product1.setCount(count);
                    product1.setBrand(brandText);

                    // 现在你可以使用这个 product 对象进行其他操作，例如更新 RecyclerView 或提交到服务器
                } catch (NumberFormatException e) {
                    // 处理无法解析为数字的文本的情况，例如显示错误消息或清除 EditText
                    e.printStackTrace();
                }
                String UpdateUrl = "http://" + prefs.getString("ip", "49.232.17.181") + ":" + prefs.getString("port", "8080") + "/mobile/product/update";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Get get = new Get();
                        try {
                            if (get.update(UpdateUrl, product1)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(UpdateActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(UpdateActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(UpdateActivity.this, "可能失败了，请检查输入", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UpdateActivity.this, HomeActivity.class);
        startActivity(intent);
        finish(); // 结束当前活动，以确保用户无法返回到当前活动
    }
}