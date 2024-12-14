package com.x_force.unimar.profile;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile {

    private final FirebaseFirestore db;

    private String email;
    private String password;
    private String userID;

    public ProfileRating rating;
    public ProfileState profileState;

    public Profile(String userID)
    {
        db = FirebaseFirestore.getInstance();
        this.userID = userID;
        setupData();
    }

    private void setupData()
    {
        db.collection("profiles").document(userID).get();

        this.email = email;
        this.password = password;

        //datayı aktar
        rating = new ProfileRating();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
