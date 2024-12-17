package com.x_force.unimar.Views;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.x_force.unimar.R;

public class ProductView extends AppCompatActivity {
    TextView productName;
    TextView cost;
    TextView category;
    TextView description;
    TextView seller;
    TextView sellerRating;
    Button sendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        productName = findViewById(R.id.productNameText);
        cost = findViewById(R.id.costText);
        category = findViewById(R.id.categoryText);
        description = findViewById(R.id.descriptionText);
        seller = findViewById(R.id.sellerNameText);
        sellerRating = findViewById(R.id.ratingTextView);
        sendMessage = findViewById(R.id.startChatButton);

        productName.setText(getIntent().getStringExtra("Name"));
        cost.setText(getIntent().getStringExtra("Cost"));
        category.setText(getIntent().getStringExtra("Category"));
        description.setText(getIntent().getStringExtra("Description"));


    }
}