package com.x_force.unimar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.x_force.unimar.Item.Item;
import com.x_force.unimar.Item.ItemAdapter;
import com.x_force.unimar.Item.ItemManager;
import com.x_force.unimar.Item.Product;
import com.x_force.unimar.Views.ProductView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class ProductListingActivity extends AppCompatActivity {
    static FirebaseFirestore db;
    GridView itemList;
    List<Item> items;
    static ItemAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_productlisting);
        db = FirebaseFirestore.getInstance();;
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
//        ArrayList<Item> items = new ArrayList<>();
//        items.add(new Product("Basys","cokiyi","elektrik",100));
//        items.add(new Product("bilgisayar","cokiyi","elektrik",100));
//        items.add(new Product("OLDU LAN", "HARİKA", "LAYLAYLAY", 200));

        showList();
        setButtonInteractions();
    }

    //itemlist öğesini ne biçimde dolduracağımızı belirleyen ItemAdapteri initialize ediyoruz
    public void showList(){

        itemList = findViewById(R.id.product_list);

        items = ItemManager.sortProductList('m');

        adapter = new ItemAdapter(this,items);

        ItemManager.adapter = adapter;

        /*itemList.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProductView.class);
            intent.putExtra("category","math");
            intent.putExtra("name","yavuz");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplication().startActivity(intent);
        });*/

        itemList.setAdapter(adapter);
    }

    public void setButtonInteractions(){

        Button GeneralSortButton = findViewById(R.id.sort_button);
        Button SortAscendingButton = findViewById(R.id.button_sort_ascending);
        Button SortDescendingButton = findViewById(R.id.button_sort_descending);
        Button FilterButton = findViewById(R.id.filter_button);

        //filterbutton instance'ları

        GeneralSortButton.setOnClickListener (v -> {
            GeneralSortButton.setVisibility(ListView.GONE);
            SortAscendingButton.setVisibility(ListView.VISIBLE);
            SortDescendingButton.setVisibility(ListView.VISIBLE);
        });

        SortAscendingButton.setOnClickListener (v -> {
            ItemManager.sortProductList('A');
            GeneralSortButton.setVisibility(ListView.VISIBLE);
            SortAscendingButton.setVisibility(ListView.GONE);
            SortDescendingButton.setVisibility(ListView.GONE);
        });

        SortDescendingButton.setOnClickListener (v -> {
            ItemManager.sortProductList('D');
            GeneralSortButton.setVisibility(ListView.VISIBLE);
            SortAscendingButton.setVisibility(ListView.GONE);
            SortDescendingButton.setVisibility(ListView.GONE);
        });

        FilterButton.setOnClickListener(v -> {

            setContentView(R.layout.activity_product_filtering);

            CheckBox csCheckbox = findViewById(R.id.csCategoryCheckBox);
            CheckBox eeeCheckbox = findViewById(R.id.eeeCategoryCheckBox);
            CheckBox engCheckbox = findViewById(R.id.engCategoryCheckBox);
            CheckBox histCheckbox = findViewById(R.id.histCategoryCheckBox);
            CheckBox philCheckbox = findViewById(R.id.philCategoryCheckBox);
            CheckBox mbgCheckbox = findViewById(R.id.mbgCategoryCheckBox);
            CheckBox chemCheckbox = findViewById(R.id.chemCategoryCheckBox);
            CheckBox physCheckbox = findViewById(R.id.physCategoryCheckBox);
            CheckBox mathCheckbox = findViewById(R.id.mathCategoryCheckBox);
            CheckBox meCheckbox = findViewById(R.id.meCategoryCheckBox);
            CheckBox ieCheckbox = findViewById(R.id.ieCategoryCheckBox);
            CheckBox priceCheckbox = findViewById(R.id.ProductPriceFilterButton);
            CheckBox ratingCheckbox = findViewById(R.id.ProductUserRatingFilterButton);
            CheckBox categoryCheckBox = findViewById(R.id.categoryCheckbox);
            Slider priceSlider = findViewById(R.id.ProductPriceFilterSlider);
            Slider ratingSlider = findViewById(R.id.ProductUserRatingFilterSlider);
            TextView filterText = findViewById(R.id.FilterText);

            priceCheckbox.setOnClickListener(e -> {
                Log.d("Enes", "Price checkbox clicked");

                priceSlider.setVisibility(View.VISIBLE);

                priceSlider.addOnChangeListener((slider, value, fromUser) -> {

                    float maxValue = priceSlider.getValue();

                    Log.d("Enes", "Price Slider Value: Min="  + ", Max=" + maxValue);

                    ItemManager.filterList('P', 0, maxValue);
                });
            });


            categoryCheckBox.setOnClickListener(e -> {

                ArrayList<String> categories = new ArrayList<>();

                csCheckbox.setVisibility(ListView.VISIBLE);
                eeeCheckbox.setVisibility(ListView.VISIBLE);
                engCheckbox.setVisibility(ListView.VISIBLE);
                histCheckbox.setVisibility(ListView.VISIBLE);
                philCheckbox.setVisibility(ListView.VISIBLE);
                mbgCheckbox.setVisibility(ListView.VISIBLE);
                chemCheckbox.setVisibility(ListView.VISIBLE);
                physCheckbox.setVisibility(ListView.VISIBLE);
                mathCheckbox.setVisibility(ListView.VISIBLE);
                meCheckbox.setVisibility(ListView.VISIBLE);
                ieCheckbox.setVisibility(ListView.VISIBLE);

                if(csCheckbox.isChecked()){
                    categories.add("CS");
                }

                if(eeeCheckbox.isChecked()){
                    categories.add("EEE");
                }

                if(engCheckbox.isChecked()){
                    categories.add("ENG");
                }

                if(histCheckbox.isChecked()){
                    categories.add("HIST");
                }

                if(philCheckbox.isChecked()){
                    categories.add("PHIL");
                }

                if(mbgCheckbox.isChecked()){
                    categories.add("MBG");
                }

                if(chemCheckbox.isChecked()){
                    categories.add("CHEM");
                }

                if(physCheckbox.isChecked()){
                    categories.add("PHYS");
                }

                if(mathCheckbox.isChecked()){
                    categories.add("MATH");
                }

                if(meCheckbox.isChecked()){
                    categories.add("ME");
                }

                if(ieCheckbox.isChecked()){
                    categories.add("IE");
                }

                ItemManager.filterList('P',categories);

            });


            //ratingCheckbox.setOnClickListener(e -> {
                //ratingSlider.setVisibility(ListView.VISIBLE);
                //ratingSlider.setOnClickListener(a -> {
                    //float value = ratingSlider.getValues().get(0);
                    //ItemManager.filterList('P',0,value);
                //});
            //});

            Button doneButton = findViewById(R.id.FilterDoneButton);

            doneButton.setOnClickListener(e -> {
                setContentView(R.layout.activity_productlisting);
                showList();
            });
        });

    }
    public static ItemAdapter getAdapter(){
        return adapter;
    }

    public static FirebaseFirestore getDb(){
        return db;
    }

}