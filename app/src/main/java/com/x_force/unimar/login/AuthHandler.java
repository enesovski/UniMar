package com.x_force.unimar.login;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.x_force.unimar.profile.ProfileHandler;

import java.util.Map;

public class AuthHandler {
    private final FirebaseAuth mAuth;

    public AuthHandler() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    public void loginUser(String email, String password, IAuthCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess("Login successful!");
                    } else {
                        callback.onFailure("Authentication failed.");
                    }
                });
    }

    public void registerUser(String email, String password, String name, String profileImage,
                             String university, String department, IAuthCallback callback) {
        if (password.length() < 6) {
            callback.onFailure("Password must be at least 6 characters.");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser fuser = mAuth.getCurrentUser();

                        if (fuser != null) {
                            String userId = fuser.getUid();

                            ProfileHandler.createUserProfile(userId, email, name, profileImage,
                                    university, department, new ProfileHandler.ProfileResultCallback() {
                                        @Override
                                        public void onSuccess(Map<String, Object> profileData) {
                                            callback.onSuccess("Registration successful!");
                                        }

                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onFailure(String errorMessage) {
                                            callback.onFailure("Profile creation failed: " + errorMessage);
                                        }
                                    });
                        }
                    } else {
                        callback.onFailure("Registration failed: " + (task.getException() != null
                                ? task.getException().getMessage()
                                : "Unknown error"));
                    }
                });
    }
}
