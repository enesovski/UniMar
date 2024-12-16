package com.x_force.unimar.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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
import com.x_force.unimar.chat.SearchUserActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

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
            user.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
            checkExistenceOfDocument(user,isExists -> {
                if(!isExists.get()){
                    Map<String, Object> data = new HashMap<>();
                    data.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    data.put("email", user.getEmail());
                    data.put("password", user.getPassword());
                    db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(data);
                }
            });
            Intent intent = new Intent(LoginActivity.this, SearchUserActivity.class);
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                System.out.println("Fetching FCM registration token failed");
                                return;
                            }

                            // Get new FCM registration token
                            String token = task.getResult();

                            // Log and toast

                            System.out.println( token);
                            Toast.makeText(LoginActivity.this,"registiration token is:"+ token, Toast.LENGTH_SHORT).show();
                        }
                    });
            startActivity(intent);

        }
    }

    private void handleRegistration() {
        User user = getUserInput();
        if (validateInput(user)) {
            authHandler.registerUser(user.getEmail(), user.getPassword(), this);
            //CHAT denemesi için
            /*Map<String, Object> data = new HashMap<>();
            data.put("userId", user.getUserId());
            data.put("email", user.getEmail());
            data.put("password", user.getPassword());*/
            //db.collection("users").add(data);
            //CHAT denemesi için
        }
    }

    private User getUserInput() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        return new User(FirebaseAuth.getInstance().getUid(),email, password);
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
