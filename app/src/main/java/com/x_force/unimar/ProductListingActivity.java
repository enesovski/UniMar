package com.x_force.unimar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;
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
import com.x_force.unimar.Item.ProductListCallback;
import com.x_force.unimar.Views.ProductView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class ProductListingActivity extends AppCompatActivity {
    static FirebaseFirestore db;
    GridView itemList;
    protected List<Item> items;
    public static ItemAdapter adapter;


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
    //SETCONTENTVIEW activity_productlisting.xml de olması lazım yoksa nullpointerexception!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public void showList(){

        itemList = findViewById(R.id.product_list);

        this.items = ItemManager.sortProductList('m');

        adapter = new ItemAdapter(this,items);

        ItemManager.adapter = adapter;

        itemList.setAdapter(adapter);
    }

    //SETCONTENTVIEW activity_productlisting.xml de olması lazım yoksa nullpointerexception!!!!!!!!!!!!!!!!!!!!!!!!!
    public void showSearchList(String s) {

        itemList = findViewById(R.id.product_list);

        this.items = ItemManager.searchInTheList('P', s);

        adapter = new ItemAdapter(this, items);

        ItemManager.adapter = adapter;

        itemList.setAdapter(adapter);
    }

    //SETCONTENTVIEW activity_productlisting.xml de olması lazım yoksa nullpointerexception!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public void showFilteredList(float max){
        itemList = findViewById(R.id.product_list);

        this.items = ItemManager.filterList('P', 0, max);

        adapter = new ItemAdapter(this, items);

        ItemManager.adapter = adapter;

        itemList.setAdapter(adapter);

        setButtonInteractions();
    }

    public void showFilteredList(ArrayList<Item> items) {
        itemList = findViewById(R.id.product_list);

        this.items = items;

        adapter = new ItemAdapter(this, items);

        ItemManager.adapter = adapter;

        itemList.setAdapter(adapter);

//        ItemManager.filterList('P', categories, new ItemManager.CategoryCallBack() {
//            @Override
//            public void onCallback(List<Item> resultList) {
//                items = resultList;
//
//                adapter = new ItemAdapter(ProductListingActivity.this, items);
//                ItemManager.adapter = adapter;
//                itemList.setAdapter(adapter);
//
//                Log.d("FilteredItems", "Filtered list size: " + items.size());
//            }
//        });
    }

    public void setButtonInteractions(){

        Button GeneralSortButton = findViewById(R.id.sort_button);
        Button SortAscendingButton = findViewById(R.id.button_sort_ascending);
        Button SortDescendingButton = findViewById(R.id.button_sort_descending);
        Button FilterButton = findViewById(R.id.filter_button);
        SearchView productSearchBar = findViewById(R.id.product_searchbar);

        //filterbutton instance'ları
        Button addNewbutton = findViewById(R.id.button_change_activity_add);

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
            //TextView filterText = findViewById(R.id.FilterText);
            Button cancelFilteringButton = findViewById(R.id.cancelFilteringButton);

            priceCheckbox.setOnClickListener(e -> {
                Log.d("Enes", "Price checkbox clicked");

                priceSlider.setVisibility(View.VISIBLE);

//                priceSlider.addOnChangeListener((slider, value, fromUser) -> {
//
//                    float maxValue = priceSlider.getValue();
//
//                    Log.d("Enes", "Price Slider Value: Min="  + ", Max=" + maxValue);
//
//                    ItemManager.filterList('P', 0, maxValue);
//                });
            });


            categoryCheckBox.setOnClickListener(e -> {

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

                FilterButton.setVisibility(View.GONE);
                setContentView(R.layout.activity_productlisting);
                float max = 0;

                if(priceCheckbox.isChecked()){
                    max = priceSlider.getValue();
                    Log.d("max", priceSlider.getValue() + "");
                    showFilteredList(max);
                }

                else if( categoryCheckBox.isChecked() ) {

                    ArrayList<String> categories = new ArrayList<>();

                    if( csCheckbox.isChecked() ) {
                        categories.add("cs");
                        Log.d("deneme","cs kutusundan sonra categori boyutu" + categories.size());
                    }

                    if( eeeCheckbox.isChecked() ) {
                        categories.add("eee");
                    }

                    if( engCheckbox.isChecked() ) {
                        categories.add("eng");
                    }

                    if( histCheckbox.isChecked() ) {
                        categories.add("hist");
                    }

                    if( philCheckbox.isChecked() ) {
                        categories.add("phil");
                    }

                    if( mbgCheckbox.isChecked() ) {
                        categories.add("mbg");
                    }

                    if( chemCheckbox.isChecked() ) {
                        categories.add("chem");
                    }

                    if( physCheckbox.isChecked() ) {
                        categories.add("phys");
                    }

                    if( mathCheckbox.isChecked() ) {
                        categories.add("math");
                    }

                    if( meCheckbox.isChecked() ) {
                        categories.add("me");
                    }

                    if( ieCheckbox.isChecked() ) {
                        categories.add("ie");
                        Log.d("kopek","boyut: "+categories.size());
                    }

                    ItemManager.filterList('P', categories, new ItemManager.CategoryCallBack() {
                        @Override
                        public void onCallback(List<Item> resultList) {
                            Log.d("kedi","boyut: "+resultList.size());
                            items = resultList;
                            showFilteredList((ArrayList<Item>) items);
                        }
                    });

                }

            });

            cancelFilteringButton.setOnClickListener(e -> {
                ItemManager.refreshProductQuery();
                setContentView(R.layout.activity_productlisting);
                showList();
                setButtonInteractions();
            });


        });

        productSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showSearchList(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                showSearchList(newText);
                return true;
            }
        });

        addNewbutton.setOnClickListener(v ->{
            Intent intent = new Intent(this, ItemAddActivity.class);
            startActivity(intent);
            finish();
        });

    }
    public static ItemAdapter getAdapter(){
        return adapter;
    }

    public static FirebaseFirestore getDb(){
        return db;
    }

}