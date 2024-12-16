package com.x_force.unimar.profile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.x_force.unimar.R;

import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private TextView profileNameTextView;
    private TextView profileEmailTextView;
    private TextView profilePointsTextView;
    private TextView profileRatingTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize UI components
        ImageView profileImageView = findViewById(R.id.profileImageView);
        TextView profileNameTextView = findViewById(R.id.profileNameTextView);
        TextView profileEmailTextView = findViewById(R.id.profileEmailTextView);
        TextView profilePointsTextView = findViewById(R.id.profilePointsTextView);
        TextView profileRatingTextView = findViewById(R.id.profileRatingTextView);
        ProgressBar ratingProgressBar = findViewById(R.id.ratingProgressBar);
        ImageButton editProfileButton = findViewById(R.id.editProfileButton);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Fetch and display profile data
            ProfileHandler.getUserProfile(userId, new ProfileHandler.ProfileResultCallback() {
                @Override
                public void onSuccess(Map<String, Object> profileData) {
                    String name = profileData.get("name") != null ? (String) profileData.get("name") : "N/A";
                    String email = profileData.get("email") != null ? (String) profileData.get("email") : "N/A";
                    String profileImage = profileData.get("profileImage") != null ? (String) profileData.get("profileImage") : null;
                    int maxPoints = profileData.get("maxPoints") != null ? ((Long) profileData.get("maxPoints")).intValue() : 0;
                    int totalPoints = profileData.get("totalPoints") != null ? ((Long) profileData.get("totalPoints")).intValue() : 0;

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
                }

                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(ProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });

            // Edit Profile Name Listener
            editProfileButton.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Edit Profile");

                // Input for Name
                final EditText input = new EditText(this);
                input.setHint("Enter new name");
                builder.setView(input);

                builder.setPositiveButton("Save", (dialog, which) -> {
                    String newName = input.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        profileNameTextView.setText("Name: " + newName);
                        // Update Firestore
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
            // Handle case where no user is logged in
            Toast.makeText(this, "No user is logged in.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private int calculateRating(int maxPoints, int totalPoints) {
        if (totalPoints == 0) return 0;
        return (int) (((double) maxPoints / totalPoints) * 100);
    }

}
