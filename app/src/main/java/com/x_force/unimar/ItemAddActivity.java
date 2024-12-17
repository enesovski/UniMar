package com.x_force.unimar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ItemAddActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main3), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the ActivityResultLauncher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        // TODO: Use the selectedImageUri to display the selected image or upload it.
                    }
                }
        );

        setButtonInteractions();
    }

    public void setButtonInteractions(){
        Button chooseCategoryButton = findViewById(R.id.button_select_category);
        Button uploadPhotoButton = findViewById(R.id.button_upload_picture);
        EditText newItemNameText = findViewById(R.id.new_item_name_text);
        EditText newItemDescText = findViewById(R.id.new_item_desc_text);
        EditText newItemCostText = findViewById(R.id.new_item_cost_text);

        uploadPhotoButton.setOnClickListener(v -> openImagePicker());

        chooseCategoryButton.setOnClickListener(e -> {
            if(newItemNameText.getText().length() != 0
                    && newItemDescText.getText().length() != 0
                    && newItemCostText.getText().length() != 0){

                Intent intent = new Intent(this, CategorySelectionActivity.class);
                intent.putExtra("name",newItemNameText.getText().toString());
                intent.putExtra("desc",newItemDescText.getText().toString());
                intent.putExtra("cost",newItemCostText.getText().toString());
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "All fields should be filled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }
}
