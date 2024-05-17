package com.example.selesmanager.dto;

public class SignupCredentials {
    private String name;
    private String mobile;
    private String pwd;

    // Constructors
    public SignupCredentials(String name, String mobile, String pwd) {
        this.name = name;
        this.mobile = mobile;
        this.pwd = pwd;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
}
