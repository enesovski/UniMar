package com.x_force.unimar.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;
import com.x_force.unimar.MainActivity;
import com.x_force.unimar.R;
import com.x_force.unimar.home.HomeActivity;
import com.x_force.unimar.chat.SearchUserActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoginActivity extends AppCompatActivity implements IAuthCallback {
    private EditText emailEditText, passwordEditText;
    private AuthHandler authHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authHandler = new AuthHandler();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);
        TextView forgotPasswordText = findViewById(R.id.forgotPasswordText);

        loginButton.setOnClickListener(view -> handleLogin());
        registerButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        forgotPasswordText.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(LoginActivity.this, "Please enter your email.", Toast.LENGTH_SHORT).show();
            } else {
                authHandler.resetPassword(email, new IAuthCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void handleLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Email and password are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        authHandler.loginUser(email, password, this);
    }

    @Override
    public void onSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish(); // Prevents navigating back to login or register screens
    }

    @Override
    public void onFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    public void checkExistenceOfDocument(User user, Callback<AtomicBoolean> callback){

        AtomicBoolean isExists = new AtomicBoolean(false);
        db.collection("users")
                .whereEqualTo("email", user.getEmail())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Set the existence flag based on query results
                    isExists.set(!queryDocumentSnapshots.isEmpty());
                    Log.d("DENEY", "User exists: method içi " + isExists.get());
                    callback.onComplete(isExists);
                })
                .addOnFailureListener(e -> {
                    // Log the error for debugging
                    Log.e("DENEY", "Error checking document existence", e);
                });
        Log.d("DENEY", "User exists: method sonu " + isExists.get());
    }

    public interface Callback<T> {
        void onComplete(T result);
    }


}
