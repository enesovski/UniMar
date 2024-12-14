package com.x_force.unimar.login;

import com.x_force.unimar.profile.Profile;

public class User {

    private String userId;
    public Profile profile;

    public User(String email, String password) {

        profile = new Profile(userId);
    }

    public String getEmail() {
        return profile.getEmail();
    }

    public String getPassword() {
        return profile.getPassword();
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
