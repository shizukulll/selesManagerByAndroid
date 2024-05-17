package com.example.selesmanager.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.selesmanager.R;
import com.example.selesmanager.dto.LoginCredentials;
import com.example.selesmanager.dto.LoginResponse;
import com.example.selesmanager.util.Post;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    EditText mobile;
    EditText pwd;
    Button login,signup;

    androidx.appcompat.widget.Toolbar toolbar;

    String loginUrl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        mobile = findViewById(R.id.editTextPhone);
        pwd = findViewById(R.id.editTextTextPassword);
        login = findViewById(R.id.loginButton);
        signup = findViewById(R.id.registerButton);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        toolbar.inflateMenu(R.menu.toolbar_menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                        Intent intent = new Intent(LoginActivity.this, SettingActivity.class);
                        startActivity(intent);
                        return true;
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile_ = mobile.getText().toString();
                String pwd_ = pwd.getText().toString();

                LoginCredentials credentials = new LoginCredentials(mobile_, pwd_);

                // 创建一个新的线程执行网络请求
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            SharedPreferences prefs = getSharedPreferences("IPCFG", MODE_PRIVATE);

                            loginUrl = "http://" + prefs.getString("ip","49.232.17.181") + ":" + prefs.getString("port","8080") + "/mobile/user/login";



                            final LoginResponse response = new Post().login(loginUrl, credentials);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if ("SUCCESS".equals(response.getStatus())) {
                                        SharedPreferences.Editor editor = getSharedPreferences("USER", MODE_PRIVATE).edit();
                                        editor.putString("id",response.getUserId());
                                        editor.apply();

                                        Toast.makeText(LoginActivity.this, "Login successful! User ID: " + response.getUserId(), Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "Error occurred during login.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

}