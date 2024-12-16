package com.x_force.unimar.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.x_force.unimar.R;
import com.x_force.unimar.chat.SearchUserActivity;
import com.x_force.unimar.login.LoginActivity;
import com.x_force.unimar.profile.ProfileActivity;
import com.x_force.unimar.profile.ProfileHandler;

import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private TextView welcomeTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        profileImageView = findViewById(R.id.profileImageView);
        welcomeTextView = findViewById(R.id.welcomeTextView);
        Button profileButton = findViewById(R.id.profileButton);
        Button chatButton = findViewById(R.id.chatButton);

        chatButton.setOnClickListener(e -> {Intent intent = new Intent(HomeActivity.this, SearchUserActivity.class);
        startActivity(intent);});

        ConstraintLayout productButton = findViewById(R.id.customButton1);
        ConstraintLayout tutoringButton = findViewById(R.id.customButton2);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "No user is logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        String userId = currentUser.getUid();

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
                    byte[] decodedBytes = android.util.Base64.decode(profileImage, android.util.Base64.DEFAULT);
                    Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    profileImageView.setImageBitmap(decodedBitmap);
                } else {
                    profileImageView.setImageResource(R.drawable.ic_placeholder);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(HomeActivity.this, "Error fetching profile: " + errorMessage, Toast.LENGTH_SHORT).show();
                Log.e("HomeActivity", "Profile Fetch Failure: " + errorMessage);
            }

        });

        profileButton.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        //BURAYA TUTORING VE PRODUCT SAYFALARI INTENTI
        productButton.setOnClickListener(view -> {
            Toast.makeText(this, "Lecture Materials clicked!", Toast.LENGTH_SHORT).show();
        });

        tutoringButton.setOnClickListener(view -> {
            Toast.makeText(this, "Tutoring clicked!", Toast.LENGTH_SHORT).show();
        });
    }
}
