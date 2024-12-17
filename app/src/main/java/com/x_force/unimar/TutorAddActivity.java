package com.x_force.unimar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TutorAddActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> imagePickerLauncher;

//    private Uri productImage;
//    private String image;
    ImageView addPhotoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_tutoring);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main3), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        addPhotoView = findViewById(R.id.add_picture_icon);
//        // Initialize the ActivityResultLauncher
//        imagePickerLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//                        productImage = result.getData().getData();
//                        addPhotoView.setImageURI(productImage);
//                        try {
//                            image= convertImageToBase64(productImage);
//                        } catch (Exception e) {
//                            throw new RuntimeException(e);
//                        }
//
//                    }
//                }
//        );

        setButtonInteractions();
    }

    public void setButtonInteractions(){
        Button chooseCategoryButton = findViewById(R.id.button_select_category);
        EditText newItemNameText = findViewById(R.id.new_item_name_text);
        EditText newItemDescText = findViewById(R.id.new_item_desc_text);
        EditText newItemCostText = findViewById(R.id.new_item_cost_text);


        chooseCategoryButton.setOnClickListener(e -> {
            if(newItemNameText.getText().length() != 0
                    && newItemDescText.getText().length() != 0
                    && newItemCostText.getText().length() != 0){

                Intent intent = new Intent(this, TutorCategorySelectionActivity.class);
                intent.putExtra("name",newItemNameText.getText().toString());
                intent.putExtra("desc",newItemDescText.getText().toString());
                intent.putExtra("cost",newItemCostText.getText().toString());
//                intent.putExtra("image",image);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "All fields should be filled", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void openImagePicker() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        imagePickerLauncher.launch(intent);
//    }
//    private String convertImageToBase64(Uri imageUri) throws Exception {
//        InputStream inputStream = getContentResolver().openInputStream(imageUri);
//        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream); // Compress to 50%
//        byte[] imageBytes = byteArrayOutputStream.toByteArray();
//        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
//    }
}
