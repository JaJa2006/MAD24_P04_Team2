package com.example.main_activity;

import java.util.ArrayList;

public class User {
    public String email;
    public String name;
    public String password;
    public void setName(String username){this.name = username;}
    public void setPassword(String password){this.password = password;}
    public void setEmail(String email){this.email = email;}
    public String getName(){return name;}
    public String getPassword(){return password;}
    public String getEmail(){return email;}
    public User(String name, String password, String email){
        this.name = name;
        this.password = password;
        this.email = email;
    }
}
