package com.example.selesmanager.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.selesmanager.Fragment.AddFragment;
import com.example.selesmanager.Fragment.HomeFragment;
import com.example.selesmanager.Fragment.MyFragment;
import com.example.selesmanager.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    private static final int MENU_HOME = R.id.navigation_home;
    private static final int MENU_DASHBOARD = R.id.navigation_dashboard;
    private static final int MENU_NOTIFICATIONS = R.id.navigation_notifications;



    private Fragment homeFragment;
    private Fragment addFragment;
    private Fragment myFragment;
    private Fragment currentFragment;

    ImageView search;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // 初始化 Fragment
        homeFragment = new HomeFragment();
        addFragment = new AddFragment();
        myFragment = new MyFragment();

        switchFragment(homeFragment);

        ImageView toolbarImage = findViewById(R.id.toolbar_image);
        toolbarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                 startActivity(intent);
            }
        });
        // 找到底部导航栏
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        // 设置底部导航栏点击事件监听器
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            if(id == R.id.navigation_home){
                switchFragment(homeFragment);
                return true;
            }else if(id == R.id.navigation_dashboard){
                switchFragment(addFragment);
                return true;
            }else if(id == R.id.navigation_notifications){
                switchFragment(myFragment);
                return  true;
            }else{
                switchFragment(homeFragment);
                return true;
            }
        });


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // 如果当前是主页，按两次返回键退出应用

                    switchFragment(homeFragment);

            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void switchFragment(Fragment fragment) {
        if (currentFragment != fragment) {
            currentFragment = fragment;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    }


}