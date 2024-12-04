package com.x_force.unimar.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.WriteResult;
import com.x_force.unimar.R;
import com.x_force.unimar.chat.ChatActivity;
import com.x_force.unimar.chat.SearchUserActivity;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements IAuthCallback {

    private EditText emailEditText, passwordEditText;
    private AuthHandler authHandler;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authHandler = new AuthHandler();

        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(view -> handleLogin());
        registerButton.setOnClickListener(view -> handleRegistration());

    }

    private void handleLogin() {
        User user = getUserInput();
        if (validateInput(user)) {
            authHandler.loginUser(user.getEmail(), user.getPassword(), this);
            Intent intent = new Intent(LoginActivity.this, SearchUserActivity.class);
            startActivity(intent);
        }
    }

    private void handleRegistration() {
        User user = getUserInput();
        if (validateInput(user)) {
            authHandler.registerUser(user.getEmail(), user.getPassword(), this);
            //CHAT denemesi için
            Map<String, Object> data = new HashMap<>();
            data.put("email", user.getEmail());
            data.put("password", user.getPassword());
            CollectionReference collectionRef = db.collection("apps");
            db.collection("users").add(data);
            //CHAT denemesi için
        }
    }

    private User getUserInput() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        return new User(email, password);
    }

    private boolean validateInput(User user) {
        boolean isValid = true;

        if (TextUtils.isEmpty(user.getEmail())) {
            emailEditText.setError("Email is required.");
            isValid = false;
        }
        if (TextUtils.isEmpty(user.getPassword())) {
            passwordEditText.setError("Password is required.");
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void onSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        // Buraya kayıt olmanın bir sonraki aşamaları
    }

    @Override
    public void onFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
