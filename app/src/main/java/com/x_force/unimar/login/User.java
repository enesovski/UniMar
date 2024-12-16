package com.x_force.unimar.login;

import com.x_force.unimar.profile.Profile;

public class User {
    private String userId;
    private String email;


    private Profile profile;

    public User() {}

    public User(String userId, String email) {
        this.userId = userId;
        this.email = email;
        this.profile = new Profile(userId, email);
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getEmail() {
        return email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
