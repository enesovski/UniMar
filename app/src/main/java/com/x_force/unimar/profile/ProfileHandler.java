package com.x_force.unimar.profile;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileHandler {
    private static final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public static void createUserProfile(String uid, String email, String name,
                                         String profileImage, String university,
                                         String department, ProfileResultCallback callback) {
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("userId", uid);
        profileData.put("email", email);
        profileData.put("name", name);
        profileData.put("profileImage", profileImage);
        profileData.put("university", university);
        profileData.put("department", department);
        profileData.put("maxPoints", 0);
        profileData.put("totalPoints", 0);

        firestore.collection("users").document(uid)
                .set(profileData)
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public static void getUserProfile(String uid, ProfileResultCallback callback) {
        firestore.collection("users").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Pass profile data to the callback
                        callback.onSuccess(documentSnapshot.getData());
                    } else {
                        callback.onFailure("Profile not found.");
                    }
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public interface ProfileResultCallback {
        void onSuccess(Map<String, Object> profileData);
        void onSuccess();
        void onFailure(String errorMessage);
    }
}
