package com.x_force.unimar.profile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.x_force.unimar.R;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImageView;
    private TextView profileNameTextView;
    private TextView profileEmailTextView;
    private TextView profilePointsTextView;
    private TextView profileRatingTextView;
    private Switch notificationSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize UI components
        profileImageView = findViewById(R.id.profileImageView);
        profileNameTextView = findViewById(R.id.profileNameTextView);
        profileEmailTextView = findViewById(R.id.profileEmailTextView);
        profilePointsTextView = findViewById(R.id.profilePointsTextView);
        profileRatingTextView = findViewById(R.id.profileRatingTextView);
        TextView profileUniversityTextView = findViewById(R.id.profileUniversityTextView);
        TextView profileDepartmentTextView = findViewById(R.id.profileDepartmentTextView);
        ProgressBar ratingProgressBar = findViewById(R.id.ratingProgressBar);
        Button editProfileButton = findViewById(R.id.editProfileButton);
        notificationSwitch = findViewById(R.id.notificationSwitch);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            ProfileHandler.getUserProfile(userId, new ProfileHandler.ProfileResultCallback() {
                @Override
                public void onSuccess(Map<String, Object> profileData) {
                    String name = profileData.get("name") != null ? (String) profileData.get("name") : "N/A";
                    String email = profileData.get("email") != null ? (String) profileData.get("email") : "N/A";
                    String profileImage = profileData.get("profileImage") != null ? (String) profileData.get("profileImage") : null;
                    int maxPoints = profileData.get("maxPoints") != null ? ((Long) profileData.get("maxPoints")).intValue() : 0;
                    int totalPoints = profileData.get("totalPoints") != null ? ((Long) profileData.get("totalPoints")).intValue() : 0;

                    String university = profileData.get("university") != null ? (String) profileData.get("university") : "N/A";
                    String department = profileData.get("department") != null ? (String) profileData.get("department") : "N/A";

                    boolean notificationsEnabled = profileData.get("notificationsEnabled") != null &&
                            (boolean) profileData.get("notificationsEnabled");

                    profileUniversityTextView.setText("University: " + university);
                    profileDepartmentTextView.setText("Department: " + department);

                    profileNameTextView.setText("Name: " + name);
                    profileEmailTextView.setText("Email: " + email);
                    profilePointsTextView.setText("Total Points: " + totalPoints);
                    int rating = calculateRating(maxPoints, totalPoints);
                    profileRatingTextView.setText("Rating: " + rating + "%");
                    ratingProgressBar.setProgress(rating);

                    if (profileImage != null && !profileImage.isEmpty()) {
                        Glide.with(ProfileActivity.this)
                                .load(profileImage)
                                .placeholder(R.drawable.ic_placeholder)
                                .into(profileImageView);
                    }

                    notificationSwitch.setChecked(notificationsEnabled);
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(ProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });

            profileImageView.setOnClickListener(v -> openImagePicker());
            notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(userId)
                        .update("notificationsEnabled", isChecked)
                        .addOnSuccessListener(unused -> {
                            String message = isChecked ? "Notifications enabled" : "Notifications disabled";
                            Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Failed to update settings", Toast.LENGTH_SHORT).show());
            });

            editProfileButton.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Edit Profile");

                final EditText input = new EditText(this);
                input.setHint("Enter new name");
                builder.setView(input);

                builder.setPositiveButton("Save", (dialog, which) -> {
                    String newName = input.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        profileNameTextView.setText("Name: " + newName);
                        FirebaseFirestore.getInstance()
                                .collection("users")
                                .document(currentUser.getUid())
                                .update("name", newName)
                                .addOnSuccessListener(unused -> Toast.makeText(this, "Name updated", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update name", Toast.LENGTH_SHORT).show());
                    }
                });

                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                builder.show();
            });
        } else {
            Toast.makeText(this, "No user is logged in.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private int calculateRating(int maxPoints, int totalPoints) {
        if (totalPoints == 0) return 0;
        return (int) (((double) totalPoints / maxPoints) * 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
        }
    }
}
