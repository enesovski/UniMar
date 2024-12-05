package com.x_force.unimar.login;

public class User {
    private  String userId;
    private  String email;
    private  String password;

    public User(){}
    public User(String userId, String email, String password) {
        this.userId = userId;
        this.email = email;
        this.password = password;
    }
    public User(String userId){

        this.userId = userId;
    }
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUserId() { return userId; }

    public void setUserId(String uid) {
        this.userId = uid;
    }
}
