package com.example.selesmanager.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.selesmanager.dto.LoginResponse;
import com.example.selesmanager.dto.SignupCredentials;
import com.example.selesmanager.dto.SignupResponse;
import com.example.selesmanager.util.Post;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.selesmanager.databinding.ActivitySignupBinding;

import com.example.selesmanager.R;

import java.io.IOException;

public class SignupActivity extends AppCompatActivity {

    EditText name,phone,pwd;
    Button btn;
    String signupUrl;
    SharedPreferences prefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.signupNameET);
        phone = findViewById(R.id.signupPhoneET);
        pwd = findViewById(R.id.signupPwdET);

        btn = findViewById(R.id.signupbutton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_ = name.getText().toString();
                String phone_ = phone.getText().toString();
                String pwd_ = pwd.getText().toString();

                if (!isNumeric(phone_)) {
                    Toast.makeText(SignupActivity.this, "手机号只能包含数字", Toast.LENGTH_SHORT).show();
                    return; // 不执行注册操作
                }
                if (phone_.length() != 11) {
                    Toast.makeText(SignupActivity.this, "手机号长度必须为11位", Toast.LENGTH_SHORT).show();
                    return; // 不执行注册操作
                }

                SignupCredentials credentials = new SignupCredentials(name_,phone_,pwd_);

                prefs = getSharedPreferences("IPCFG", MODE_PRIVATE);
                signupUrl = "http://" + prefs.getString("ip","49.232.17.181") + ":" + prefs.getString("port","8080") + "/mobile/user/signup";

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final SignupResponse response = new Post().signup(signupUrl, credentials);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if ("SUCCESS".equals(response.getStatus())) {
                                        Toast.makeText(SignupActivity.this, "Signup successful! User ID: " + response.getId(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SignupActivity.this, "Signup failed.", Toast.LENGTH_SHORT).show();
                                    }
                                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                            });
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start(); // 启动线程
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    }
