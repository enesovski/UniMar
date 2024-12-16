package com.x_force.unimar.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.x_force.unimar.R;
import com.x_force.unimar.login.LoginActivity;
import com.x_force.unimar.profile.ProfileActivity;
import com.x_force.unimar.profile.ProfileHandler;

import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private TextView welcomeTextView;
    private Button profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize UI components
        profileImageView = findViewById(R.id.profileImageView);
        welcomeTextView = findViewById(R.id.welcomeTextView);
        profileButton = findViewById(R.id.profileButton);
        ConstraintLayout customButton1 = findViewById(R.id.customButton1);
        ConstraintLayout customButton2 = findViewById(R.id.customButton2);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "No user is logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        String userId = currentUser.getUid();

        // Fetch user profile
        ProfileHandler.getUserProfile(userId, new ProfileHandler.ProfileResultCallback() {
            @Override
            public void onSuccess(Map<String, Object> profileData) {
                if (profileData == null) {
                    Toast.makeText(HomeActivity.this, "Profile data is missing.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String name = profileData.get("name") != null ? (String) profileData.get("name") : "User";
                String profileImage = profileData.get("profileImage") != null ? (String) profileData.get("profileImage") : null;

                welcomeTextView.setText("Welcome, " + name);

                if (profileImage != null && !profileImage.isEmpty()) {
                    Glide.with(HomeActivity.this)
                            .load(profileImage)
                            .placeholder(R.drawable.ic_placeholder)
                            .into(profileImageView);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(HomeActivity.this, "Error fetching profile: " + errorMessage, Toast.LENGTH_SHORT).show();
                Log.e("HomeActivity", "Profile Fetch Failure: " + errorMessage);
            }

            @Override
            public void onSuccess() {
                // Optional override
            }
        });

        profileButton.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        customButton1.setOnClickListener(view -> {
            Toast.makeText(this, "Lecture Materials clicked!", Toast.LENGTH_SHORT).show();
            // Add intent to navigate to the respective activity
        });

        customButton2.setOnClickListener(view -> {
            Toast.makeText(this, "Tutoring clicked!", Toast.LENGTH_SHORT).show();
            // Add intent to navigate to the respective activity
        });
    }
}
