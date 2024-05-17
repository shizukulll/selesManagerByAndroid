package com.example.selesmanager.util;

import com.example.selesmanager.dto.LoginCredentials;
import com.example.selesmanager.dto.LoginResponse;
import com.example.selesmanager.dto.SignupCredentials;
import com.example.selesmanager.dto.SignupResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Post {

    final int CONNECT_TIMEOUT = 30;
    final int READ_TIMEOUT = 30;
    final int WRITE_TIMEOUT = 30;

    public LoginResponse login(String url, LoginCredentials json) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");


        Gson gson = new Gson();
        String jsonString = gson.toJson(json);
        RequestBody body = RequestBody.create(JSON, jsonString);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json;charset=utf-8")
                .addHeader("Accept", "application/json;charset=utf-8")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String responseBody = response.body().string();

            // 使用Gson解析JSON字符串为LoginResponse对象
            LoginResponse loginResponse = gson.fromJson(responseBody, LoginResponse.class);

            // 返回解析后的对象
            return loginResponse;
        }
    }

    public SignupResponse signup(String url, SignupCredentials json) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");


        Gson gson = new Gson();
        String jsonString = gson.toJson(json);
        RequestBody body = RequestBody.create(JSON, jsonString);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json;charset=utf-8")
                .addHeader("Accept", "application/json;charset=utf-8")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String responseBody = response.body().string();

            // 使用Gson解析JSON字符串为LoginResponse对象
            SignupResponse loginResponse = gson.fromJson(responseBody, SignupResponse.class);

            // 返回解析后的对象
            return loginResponse;
        }
    }
}
