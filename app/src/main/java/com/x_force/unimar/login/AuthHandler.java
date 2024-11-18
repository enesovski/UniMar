package com.x_force.unimar.login;

import com.google.firebase.auth.FirebaseAuth;

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

    public void registerUser(String email, String password, IAuthCallback callback) {
        if (password.length() < 6) {
            callback.onFailure("Password must be at least 6 characters.");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess("Registration successful!");
                    } else {
                        callback.onFailure("Registration failed.");
                    }
                });
    }

    public String getCurrentUserUid() {
        return mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
    }

    public void logoutUser() {
        mAuth.signOut();
    }
}
