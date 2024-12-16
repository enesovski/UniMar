package com.x_force.unimar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.x_force.unimar.Item.ItemManager;
import com.x_force.unimar.Item.Product;

import java.util.ArrayList;


public class ItemAddActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main3), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setButtonInteractions();
    }

    public void setButtonInteractions(){
        Button ChooseCategoryButton = findViewById(R.id.button_select_category);
        EditText newItemNameText = findViewById(R.id.new_item_name_text);
        EditText newItemDescText = findViewById(R.id.new_item_desc_text);
        EditText newItemCostText = findViewById(R.id.new_item_cost_text);

        ChooseCategoryButton.setOnClickListener(e ->{
            if(newItemNameText.getText().length() != 0
                    && newItemDescText.getText().length() != 0
                    && newItemCostText.getText().length() != 0){

                Intent intent = new Intent(this, CategorySelectionActivity.class);
                intent.putExtra("name",newItemNameText.getText().toString());
                intent.putExtra("desc",newItemDescText.getText().toString());
                intent.putExtra("cost",newItemCostText.getText().toString());
                startActivity(intent);
                finish();
            }

            else{
                Toast.makeText(this, "All fields should be filled", Toast.LENGTH_SHORT).show();
            }
        });
    }
}