package com.x_force.unimar.profile;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class ProfileHandler {
    private final FirebaseFirestore firestore;

    public ProfileHandler() {
        this.firestore = FirebaseFirestore.getInstance();
    }

    public void createUserProfile(String uid, String email, String name, IProfileCallback callback) {

        // Map dictionary gibi bir key içeriyor ve o keye ait datayı içeriyor
        // dictionaryden farkı unique key içermesi
        Map<String, Object> profile = new HashMap<>();
        profile.put("email", email);
        profile.put("name", name);

        firestore.collection("users").document(uid)
                .set(profile)
                .addOnSuccessListener(unused -> callback.onSuccess("Profile created!"))
                .addOnFailureListener(e -> callback.onFailure("Profile creation failed: " + e.getMessage()));
    }

    public void getUserProfile(String uid, IProfileCallback callback) {

        //uid her userda var. uid ye göre profil datalarına erişilebilir
        firestore.collection("users").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        callback.onProfileLoaded(documentSnapshot.getData());
                    } else {
                        callback.onFailure("Profile not found.");
                    }
                })
                .addOnFailureListener(e -> callback.onFailure("Failed to load profile: " + e.getMessage()));
    }

    public void updateUserProfile(String uid, Map<String, Object> updates, IProfileCallback callback) {
        firestore.collection("users").document(uid)
                .update(updates)
                .addOnSuccessListener(unused -> callback.onSuccess("Profile updated!"))
                .addOnFailureListener(e -> callback.onFailure("Profile update failed: " + e.getMessage()));
    }
}
