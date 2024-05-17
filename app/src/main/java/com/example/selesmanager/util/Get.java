package com.example.selesmanager.util;

import com.example.selesmanager.pojo.Product;
import com.example.selesmanager.pojo.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Get {

    final int CONNECT_TIMEOUT = 30;
    final int READ_TIMEOUT = 30;
    final int WRITE_TIMEOUT = 30;

    public List<Product> getAllProducts(String url) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String responseBody = response.body().string();

            // 使用Gson解析JSON字符串为Product对象列表  
            Gson gson = new Gson();
            List<Product> productList = gson.fromJson(responseBody, new TypeToken<List<Product>>() {}.getType());

            // 或者，如果你不想使用TypeToken，可以像下面这样使用泛型方法  
            // List<Product> productList = gson.fromJson(responseBody, new TypeReference<List<Product>>() {}.getType());

            // 返回解析后的对象列表  
            return productList;
        }
    }

    public String delete(String url,int id){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url)
                .newBuilder()
                .addQueryParameter("id", String.valueOf(id)); // 将 id 转换为字符串添加到查询参数中

        String finalUrl = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(finalUrl) // 使用带有 id 查询参数的最终 URL
                .build();


        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUserById(String url, String id) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS) // 替换 CONNECT_TIMEOUT
                .readTimeout(10, TimeUnit.SECONDS)    // 替换 READ_TIMEOUT
                .writeTimeout(10, TimeUnit.SECONDS)   // 替换 WRITE_TIMEOUT
                .retryOnConnectionFailure(true)
                .build();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url)
                .newBuilder()
                .addQueryParameter("id", id);

        String finalUrl = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String responseBody = response.body().string();
            Gson gson = new Gson();
            return gson.fromJson(responseBody, User.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Product getProductById(String url, int id) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url)
                .newBuilder()
                .addQueryParameter("id", String.valueOf(id));

        String finalUrl = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        String jsonResponse = response.body().string();

        Gson gson = new Gson();
        return gson.fromJson(jsonResponse,Product.class);
    }

    public List<Product> getProductByName(String url, String name) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url)
                .newBuilder()
                .addQueryParameter("name", name);

        String finalUrl = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        String jsonResponse = response.body().string();

        // Use Gson to parse JSON into a list of Product objects
        Gson gson = new Gson();
        Type productListType = new TypeToken<List<Product>>() {}.getType();

        return gson.fromJson(jsonResponse, productListType);
    }

    public boolean insert(String url, Product product) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        // 将产品对象转换为JSON字符串
        Gson gson = new Gson();
        String jsonProduct = gson.toJson(product);

        // 创建请求体，使用JSON数据作为请求体内容
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonProduct);

        // 构建请求，将请求体添加到请求中
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody) // 使用POST方法发送数据
                .build();

        // 执行请求
        Response response = client.newCall(request).execute();

        // 检查响应是否成功
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        // 从服务器响应中获取内容
        String jsonResponse = response.body().string();

        // 在这里您可以根据服务器的响应内容判断是否插入成功
        // 如果服务器返回的是"true"字符串，则表示插入成功，返回true；否则返回false
        return "true".equals(jsonResponse);
    }

    public boolean update(String url , Product product) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url)
                .newBuilder()
                .addQueryParameter("id", String.valueOf(product.getId()))
                .addQueryParameter("name", product.getName())
                .addQueryParameter("price", String.valueOf(product.getPrice()))
                .addQueryParameter("count", String.valueOf(product.getCount()))
                .addQueryParameter("brand", product.getBrand());

        String finalUrl = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            return false;
        }

        // 解析服务器返回的 JSON 数据
        Gson gson = new Gson();
        Product updatedProduct = gson.fromJson(response.body().string(), Product.class);

        // 检查返回的 JSON 数据是否符合成功更新的条件
        return updatedProduct != null && updatedProduct.getId() == product.getId();
    }

}