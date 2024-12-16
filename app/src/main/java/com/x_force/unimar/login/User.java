package com.x_force.unimar.login;

import com.x_force.unimar.profile.Profile;
import com.x_force.unimar.profile.ProfileHandler;

import java.util.Map;

public class User {

    private String userId;
    public Profile profile;

    public User(String email, String password) {
        profile = new Profile(userId, email);
    }
    public User(){
        ProfileHandler.getUserProfile(userId, new ProfileHandler.ProfileResultCallback() {
            @Override
            public void onSuccess(Map<String, Object> profileData) {
                
            }

            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
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
