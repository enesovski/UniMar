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

                            // Save profile to Firestore
                            ProfileHandler.createUserProfile(userId, email, name, profileImage,
                                    university, department, new ProfileHandler.ProfileResultCallback() {
                                        @Override
                                        public void onSuccess(Map<String, Object> profileData) {
                                            callback.onSuccess("Registration successful!");
                                        }

                                        @Override
                                        public void onSuccess() {
                                            // Optional override
                                        }

                                        @Override
                                        public void onFailure(String errorMessage) {
                                            callback.onFailure("Profile creation failed: " + errorMessage);
                                        }
                                    });
                        } else {
                            callback.onFailure("User creation successful but user object is null.");
                        }
                    } else {
                        callback.onFailure("Registration failed: " +
                                (task.getException() != null ? task.getException().getMessage() : "Unknown error"));
                    }
                });
    }

    public void resetPassword(String email, IAuthCallback callback) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess("Password reset email sent!");
                    } else {
                        callback.onFailure("Failed to send password reset email.");
                    }
                });
    }

}
