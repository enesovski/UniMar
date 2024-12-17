package com.x_force.unimar.profile;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile {

    private  FirebaseFirestore db;
    private String email;
    private String userId;
    private String password;

    public String name;
    public ProfileState profileState;

    public Profile(){

    }

    public Profile(String userId,String email,String name){
        db = FirebaseFirestore.getInstance();
        this.userId = userId;
        this.email=email;
        this.name=name;
        setupData();

    }

    public Profile(String userId, String email)
    {
        db = FirebaseFirestore.getInstance();
        this.userId = userId;
        setupData();
    }

    private void setupData() {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        email = documentSnapshot.getString("email");
                        name = documentSnapshot.getString("name");
                        maxPoints = documentSnapshot.contains("maxPoints") ? documentSnapshot.getLong("maxPoints").intValue() : 0;
                        totalPoints = documentSnapshot.contains("totalPoints") ? documentSnapshot.getLong("totalPoints").intValue() : 0;
                        profileState = ProfileState.Normal; // Update state if necessary
                    } else {
                        System.out.println("Profile not found.");
                    }
                })
                .addOnFailureListener(e -> System.err.println("Error fetching profile: " + e.getMessage()));
    }

    public int maxPoints;
    public int totalPoints;

    public int getRatingPercentage()
    {
        double percentage = (double)maxPoints / totalPoints;
        return (int)(percentage * 100);
    }

    public String getName() {
        return name;
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
