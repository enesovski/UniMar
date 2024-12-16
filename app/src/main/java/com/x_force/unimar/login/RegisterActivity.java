package com.x_force.unimar.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.x_force.unimar.R;
import com.x_force.unimar.home.HomeActivity;

import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private Spinner universitySpinner, departmentSpinner;
    private EditText nameEditText, emailEditText, passwordEditText;
    private ImageView profileImageView;
    private Button selectProfileImageButton;

    private Uri profileImageUri; // To store the selected image URI
    private StorageReference storageReference;

    private AuthHandler authHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authHandler = new AuthHandler();
        storageReference = FirebaseStorage.getInstance().getReference();

        // Initialize UI components
        universitySpinner = findViewById(R.id.universitySpinner);
        departmentSpinner = findViewById(R.id.departmentSpinner);
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        profileImageView = findViewById(R.id.profileImageView);
        selectProfileImageButton = findViewById(R.id.selectProfileImageButton);

        // Populate spinners
        ArrayAdapter<CharSequence> universityAdapter = ArrayAdapter.createFromResource(
                this, R.array.universities_array, android.R.layout.simple_spinner_item);
        universityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        universitySpinner.setAdapter(universityAdapter);

        ArrayAdapter<CharSequence> departmentAdapter = ArrayAdapter.createFromResource(
                this, R.array.departments_array, android.R.layout.simple_spinner_item);
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(departmentAdapter);

        // Handle profile image selection
        selectProfileImageButton.setOnClickListener(v -> openImagePicker());

        findViewById(R.id.registerButton).setOnClickListener(v -> handleRegistration());
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    profileImageUri = result.getData().getData();
                    profileImageView.setImageURI(profileImageUri);
                } else {
                    Toast.makeText(this, "Image selection canceled.", Toast.LENGTH_SHORT).show();
                }
            }
    );

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void handleRegistration() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String university = universitySpinner.getSelectedItem().toString();
        String department = departmentSpinner.getSelectedItem().toString();

        // Validate input
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (university.equals("Select University") || department.equals("Select Department")) {
            Toast.makeText(this, "Please select valid university and department.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (profileImageUri == null) {
            Toast.makeText(this, "Please upload a profile picture.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Upload profile image to Firebase Storage
        String imageName = "profile_images/" + UUID.randomUUID().toString();
        StorageReference imageRef = storageReference.child(imageName);

        imageRef.putFile(profileImageUri)
                .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String profileImageUrl = uri.toString();
                    registerUser(email, password, name, profileImageUrl, university, department);
                }))
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void registerUser(String email, String password, String name, String profileImageUrl, String university, String department) {
        authHandler.registerUser(email, password, name, profileImageUrl, university, department, new IAuthCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                navigateToHome();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToHome() {
        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
        startActivity(intent);
        finish(); // Prevent returning to RegisterActivity
    }
}
