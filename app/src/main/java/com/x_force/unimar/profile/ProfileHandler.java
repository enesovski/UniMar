package com.x_force.unimar.profile;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileHandler {
    private static final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    public static void createUserProfile(String uid, String email, String name,
                                         String profileImage, String university,
                                         String department, ProfileResultCallback callback) {
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("userId",uid);
        profileData.put("email", email);
        profileData.put("name", name);
        profileData.put("profileImage", profileImage == null ? "" : profileImage);
        profileData.put("university", university);
        profileData.put("department", department);

        firestore.collection("users").document(uid)
                .set(profileData)
                .addOnSuccessListener(unused -> callback.onSuccess(profileData))
                .addOnFailureListener(e -> callback.onFailure("Failed to save profile: " + e.getMessage()));
    }

    public static void getUserProfile(String uid, ProfileResultCallback callback) {
        firestore.collection("users").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        callback.onSuccess(documentSnapshot.getData());
                    } else {
                        callback.onFailure("Profile not found.");
                    }
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public interface ProfileResultCallback {
        void onSuccess(Map<String, Object> profileData);
        void onFailure(String errorMessage);
    }
}
