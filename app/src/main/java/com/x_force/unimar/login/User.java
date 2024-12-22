package com.x_force.unimar.login;

import com.x_force.unimar.profile.Profile;

public class User {
    private String userId;
    private String email;
    private String name;
    private int totalRating;

    private double Rating;
    private String department;

    private String porfileImage;
    private String university;

    private Profile profile;

    public User() {}

    public User(String UserId, String department, String email, String name, String profileImage, String university) {
        this.userId=UserId;
        this.porfileImage = profileImage;
        this.university = university;
        this.department = department;
        this.profile = new Profile(userId, email, name);
    }

    public User(String userId,String email,String name){
        this.userId = userId;
        this.email = email;
        this.profile = new Profile(userId, email,name);
    }

    public User(String userId, String email) {
        this.userId = userId;
        this.email = email;
        this.profile = new Profile(userId, email);
    }

    public void increment_rating(double rating){
        this.Rating+=rating;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
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
    public double getRating(){
        return Rating;
    }
    public void incrementTotalRating(){
        totalRating+=5;
    }
    public int getTotalRating(){
        return totalRating;
    }

}
