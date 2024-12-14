package com.x_force.unimar.login;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
                        FirebaseUser fuser = mAuth.getCurrentUser();

                        if(fuser != null)
                        {
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    callback.onFailure("Mail is sent.");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    callback.onFailure("Mail cannot sent.");
                                }
                            });
                        }

                        callback.onSuccess("Registration successful!");
                    } else {
                        callback.onFailure("Registration failed.");
                    }
                });
    }

    public String getCurrentUserUid() {
        return (mAuth.getCurrentUser() != null) ? mAuth.getCurrentUser().getUid() : null;
    }

    public void logoutUser() {
        mAuth.signOut();
    }
}
