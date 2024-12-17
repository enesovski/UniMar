package com.x_force.unimar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.x_force.unimar.Item.Product;

import java.util.ArrayList;

public class CategorySelectionActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.addCategory), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CheckBox checkBoxMath = findViewById(R.id.checkBox_math);
        CheckBox checkBoxPhys = findViewById(R.id.checkBox_phys);
        CheckBox checkBoxChem = findViewById(R.id.checkBox_chem);
        CheckBox checkBoxEee = findViewById(R.id.checkBox_eee);
        CheckBox checkBoxHist = findViewById(R.id.checkBox_hist);
        CheckBox checkBoxMbg = findViewById(R.id.checkBox_mbg);
        CheckBox checkBoxEng = findViewById(R.id.checkBox_eng);
        CheckBox checkBoxCs = findViewById(R.id.checkBox_cs);
        CheckBox checkBoxPhil = findViewById(R.id.checkBox_phil);
        CheckBox checkBoxMe = findViewById(R.id.checkBox_me);
        CheckBox checkBoxIe = findViewById(R.id.checkBox_ie);
        Button AddProductButton = findViewById(R.id.button_add_product);

        ArrayList<String> category = new ArrayList<>();

        AddProductButton.setOnClickListener(z -> {
            if(checkBoxMath.isChecked()){
                category.add("math");
                Log.d("Checkbox", "Math");
            }
            if(checkBoxPhys.isChecked()){
                category.add("phys");
            }
            if(checkBoxChem.isChecked()){
                category.add("chem");
            }
            if(checkBoxEee.isChecked()){
                category.add("eee");
            }
            if(checkBoxHist.isChecked()){
                category.add("hist");
            }
            if(checkBoxMbg.isChecked()){
                category.add("mbg");
            }
            if(checkBoxMe.isChecked()){
                category.add("me");
            }
            if(checkBoxEng.isChecked()){
                category.add("eng");
            }
            if(checkBoxCs.isChecked()){
                category.add("cs");
            }
            if(checkBoxPhil.isChecked()){
                category.add("phil");
            }
            if(checkBoxIe.isChecked()){
                category.add("ie");
            }
            if(category.isEmpty()){
                Toast.makeText(this, "Please select at least one category", Toast.LENGTH_SHORT).show();
            }
            else{
                String name = getIntent().getStringExtra("name");
                String desc = getIntent().getStringExtra("desc");
                String cost = getIntent().getStringExtra("cost");
                String image = getIntent().getStringExtra("image");
                int intCost = Integer.parseInt(cost);
                Product temp = new Product(name, desc, category, intCost,image);
                Toast.makeText(this, "Product added", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ProductListingActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
