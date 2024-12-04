package com.x_force.unimar.login;

public class User {
    private  String email;
    private  String password;


    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public User(){

    }
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
