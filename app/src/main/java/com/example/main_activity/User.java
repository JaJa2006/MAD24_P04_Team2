package com.example.main_activity;

public class User {
    public String name;
    public String password;
    public void setName(String username){this.name = username;}
    public void setPassword(String password){this.name = password;}
    public String getName(){return name;}
    public String getPassword(){return password;}
    public User(String name, String password){
        this.name = name;
        this.password = password;
    }
}
