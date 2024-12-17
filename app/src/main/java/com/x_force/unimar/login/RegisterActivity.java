package com.x_force.unimar.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.x_force.unimar.R;
import com.x_force.unimar.home.HomeActivity;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private Spinner universitySpinner, departmentSpinner;
    private EditText nameEditText, emailEditText, passwordEditText;
    private ImageView profileImageView;
    private Button selectProfileImageButton;
    private Uri profileImageUri;
    private AuthHandler authHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authHandler = new AuthHandler();

        universitySpinner = findViewById(R.id.universitySpinner);
        departmentSpinner = findViewById(R.id.departmentSpinner);
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        profileImageView = findViewById(R.id.profileImageView);
        selectProfileImageButton = findViewById(R.id.selectProfileImageButton);

        ArrayAdapter<CharSequence> universityAdapter = ArrayAdapter.createFromResource(
                this, R.array.universities_array, android.R.layout.simple_spinner_item);
        universityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        universitySpinner.setAdapter(universityAdapter);

        ArrayAdapter<CharSequence> departmentAdapter = ArrayAdapter.createFromResource(
                this, R.array.departments_array, android.R.layout.simple_spinner_item);
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(departmentAdapter);

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

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (university.equals("Select University") || department.equals("Select Department")) {
            Toast.makeText(this, "Please select valid university and department.", Toast.LENGTH_SHORT).show();
            return;
        }

        String base64Image="";
        if(profileImageUri!=null){
            try {
                 base64Image = convertImageToBase64(profileImageUri);
                 registerUser(email, password, name, base64Image, university, department);
            } catch (Exception e) {
                Log.e(TAG, "Error converting image to Base64: " + e.getMessage());
                Toast.makeText(this, "Error processing image.", Toast.LENGTH_SHORT).show();


            }

        }else{
            registerUser(email, password, name, base64Image, university, department);
        }

    }

    private String convertImageToBase64(Uri imageUri) throws Exception {
        InputStream inputStream = getContentResolver().openInputStream(imageUri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream); // Compress to 50%
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void registerUser(String email, String password, String name, String base64Image, String university, String department) {
        authHandler.registerUser(email, password, name, base64Image, university, department, new IAuthCallback() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
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
        finish();
    }
}