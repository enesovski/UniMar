package com.x_force.unimar.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.x_force.unimar.R;
import com.x_force.unimar.home.HomeActivity;

public class RegisterActivity extends AppCompatActivity implements IAuthCallback {
    private EditText emailEditText, passwordEditText, nameEditText;
    private EditText universityEditText, departmentEditText;
    private Button registerButton, selectProfileImageButton;
    private ImageView profileImageView;
    private String uploadedImageUrl = "default_profile_image"; // Default image URL

    private static final int IMAGE_PICK_REQUEST = 1;

    private AuthHandler authHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authHandler = new AuthHandler();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        nameEditText = findViewById(R.id.nameEditText);
        profileImageView = findViewById(R.id.profileImageView);
        selectProfileImageButton = findViewById(R.id.selectProfileImageButton);
        universityEditText = findViewById(R.id.universityEditText);
        departmentEditText = findViewById(R.id.departmentEditText);


        registerButton = findViewById(R.id.registerButton);

        selectProfileImageButton.setOnClickListener(view -> openImagePicker());
        registerButton.setOnClickListener(view -> handleRegistration());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            profileImageView.setImageURI(selectedImageUri);

            // Upload to Firebase Storage and store URL
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference storageRef = FirebaseStorage.getInstance().getReference("profile_images/" + userId);

            storageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> uploadedImageUrl = uri.toString())
                            .addOnFailureListener(e -> Log.e("Image Upload", "Error: " + e.getMessage())));
        }
    }

    private void handleRegistration() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();
        String university = universityEditText.getText().toString().trim();
        String department = departmentEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name) ||
                TextUtils.isEmpty(university) || TextUtils.isEmpty(department)) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Pass the data to AuthHandler
        authHandler.registerUser(email, password, name, uploadedImageUrl, university, department, this);
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
}