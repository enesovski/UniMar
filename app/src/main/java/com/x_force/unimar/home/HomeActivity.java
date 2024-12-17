package com.x_force.unimar.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.x_force.unimar.ProductListingActivity;
import com.x_force.unimar.R;
import com.x_force.unimar.chat.SearchUserActivity;
import com.x_force.unimar.profile.ProfileActivity;
import com.x_force.unimar.profile.ProfileHandler;

import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private TextView welcomeTextView;
    private ImageView profileImageView;
    private Button profileButton, chatButton;

    private Button productButton, tutoringButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        productButton = findViewById(R.id.customButton1);
        tutoringButton = findViewById(R.id.customButton2);

        productButton.setOnClickListener(e -> {Intent intent = new Intent(HomeActivity.this, ProductListingActivity.class);
            startActivity(intent);
        });

        tutoringButton.setOnClickListener(e -> {Intent intent = new Intent(HomeActivity.this, ProductListingActivity.class);
            startActivity(intent);
        });


        // Initialize UI components
        welcomeTextView = findViewById(R.id.welcomeTextView);
        profileImageView = findViewById(R.id.profileImageView);
        profileButton = findViewById(R.id.profileButton);
        chatButton = findViewById(R.id.chatButton);

        // Fetch and display profile information
        fetchAndDisplayProfileData();

        // Set button click listeners
        profileButton.setOnClickListener(v -> openProfileActivity());
        chatButton.setOnClickListener(v -> openChatActivity());
    }

    private void fetchAndDisplayProfileData() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            ProfileHandler.getUserProfile(userId, new ProfileHandler.ProfileResultCallback() {
                @Override
                public void onSuccess(Map<String, Object> profileData) {
                    // Extract user profile details with fallback values
                    String name = profileData.getOrDefault("name", "User").toString();
                    String profileImage = profileData.getOrDefault("profileImage", "").toString();

                    // Update UI components
                    welcomeTextView.setText("Welcome, " + name);

                    if (!profileImage.isEmpty()) {
                        // Decode the Base64 string to a Bitmap
                        byte[] decodedBytes = android.util.Base64.decode(profileImage, android.util.Base64.DEFAULT);
                        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                        // Set the Bitmap to the ImageView
                        profileImageView.setImageBitmap(decodedBitmap);
                    }
                    else{
                        profileImageView.setImageResource(R.drawable.ic_placeholder);
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    // Display error message if profile fetch fails
                    Toast.makeText(HomeActivity.this, "Failed to load profile: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // If no user is logged in
            Toast.makeText(this, "No user is logged in.", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
        }
    }

    private void openProfileActivity() {
        Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    private void openChatActivity() {
        Intent intent = new Intent(HomeActivity.this, SearchUserActivity.class);
        startActivity(intent);
    }
}
