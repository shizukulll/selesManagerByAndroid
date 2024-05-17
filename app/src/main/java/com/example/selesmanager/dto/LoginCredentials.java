package com.example.selesmanager.dto;

public class LoginCredentials {
    private String mobile;
    private String pwd;

    // Getters and setters
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public LoginCredentials(String mobile, String pwd) {
        this.mobile = mobile;
        this.pwd = pwd;
    }
}
