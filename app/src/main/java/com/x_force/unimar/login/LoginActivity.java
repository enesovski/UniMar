package com.x_force.unimar.login;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.x_force.unimar.ActivityManager;
import com.x_force.unimar.MainActivity;
import com.x_force.unimar.R;

public class LoginActivity extends AppCompatActivity implements IAuthCallback {

    private EditText emailEditText, passwordEditText;
    private AuthHandler authHandler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("login","creat");
        authHandler = new AuthHandler();

        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(view -> handleLogin());
        //registerButton.setOnClickListener(view -> handleRegistration());

        registerButton.setOnClickListener(view -> ActivityManager.getInstance().Switch(this, MainActivity.class));


    }

    private void handleLogin() {
        User user = getUserInput();
        if (validateInput(user)) {
            authHandler.loginUser(user.getEmail(), user.getPassword(), this);
        }
    }

    private void handleRegistration() {
        User user = getUserInput();
        if (validateInput(user)) {
            authHandler.registerUser(user.getEmail(), user.getPassword(), this);
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
