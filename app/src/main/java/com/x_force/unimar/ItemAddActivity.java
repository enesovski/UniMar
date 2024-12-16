package com.x_force.unimar;

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

import com.x_force.unimar.Item.Product;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;

import kotlinx.coroutines.Waiter;

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

                setContentView(R.layout.activity_add_category);

                CheckBox checkBoxMath = findViewById(R.id.checkBox_math);
                CheckBox checkBoxPhys = findViewById(R.id.checkBox_phys);
                CheckBox checkBoxChem = findViewById(R.id.checkBox_chem);
                CheckBox checkBoxEee = findViewById(R.id.checkBox_eee);
                CheckBox checkBoxHist = findViewById(R.id.checkBox_hist);
                CheckBox checkBoxMbg = findViewById(R.id.checkBox_mbg);
                CheckBox checkBoxEng = findViewById(R.id.checkBox_eng);
                CheckBox checkBoxCs = findViewById(R.id.checkBox_cs);
                CheckBox checkBoxPhil = findViewById(R.id.checkBox_phil);
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
                    if(checkBoxEng.isChecked()){
                        category.add("eng");
                    }
                    if(checkBoxCs.isChecked()){
                        category.add("cs");
                    }
                    if(checkBoxPhil.isChecked()){
                        category.add("phil");
                    }
                    if(category.size() == 0){
                        Toast.makeText(this, "Please select at least one category", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(this, "Product added", Toast.LENGTH_SHORT).show();
                        Product temp = new Product(newItemNameText.getText().toString(),
                                newItemDescText.getText().toString(),
                                category,
                                Integer.parseInt(newItemCostText.getText().toString()));;
                    }
                });
            }

            else{
                Toast.makeText(this, "All fields should be filled", Toast.LENGTH_SHORT).show();
            }
        });
    }
}