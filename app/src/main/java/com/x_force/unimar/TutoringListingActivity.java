package com.x_force.unimar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.slider.Slider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.x_force.unimar.Item.Item;
import com.x_force.unimar.Item.ItemAdapter;
import com.x_force.unimar.Item.ItemManager;

import java.util.ArrayList;
import java.util.List;

public class TutoringListingActivity extends AppCompatActivity {
    static FirebaseFirestore db;
    static FirebaseAuth auth;
    GridView itemList;
    protected List<Item> items;
    public static ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutoringlisting);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        showList();
        setButtonInteractions();
    }

    public void showList(){
        itemList = findViewById(R.id.tutoring_list);

        this.items = ItemManager.sortTutorList('m');

        adapter = new ItemAdapter(this,items);

        ItemManager.adapter = adapter;

        itemList.setAdapter(adapter);
    }

    public void showFilteredList(float max){
        itemList = findViewById(R.id.tutoring_list);

        this.items = ItemManager.filterList('T',0,max);

        adapter = new ItemAdapter(this,items);

        ItemManager.adapter = adapter;

        itemList.setAdapter(adapter);

        setButtonInteractions();
    }

    public void showFilteredList(ArrayList<Item> items) {
        itemList = findViewById(R.id.tutoring_list);

        this.items = items;

        adapter = new ItemAdapter(this, items);

        ItemManager.adapter = adapter;

        itemList.setAdapter(adapter);

        setButtonInteractions();
    }

    public void showSearchList(String s){
        itemList = findViewById(R.id.tutoring_list);

        this.items = ItemManager.searchInTheList('T', s);

        adapter = new ItemAdapter(this,items);

        ItemManager.adapter = adapter;

        itemList.setAdapter(adapter);
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
            ItemManager.sortTutorList('A');
            GeneralSortButton.setVisibility(ListView.VISIBLE);
            SortAscendingButton.setVisibility(ListView.GONE);
            SortDescendingButton.setVisibility(ListView.GONE);
        });

        SortDescendingButton.setOnClickListener (v -> {
            ItemManager.sortTutorList('D');
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
                setContentView(R.layout.activity_tutoringlisting);
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
                ItemManager.refreshTutorQuery();
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
            Intent intent = new Intent(this, TutorAddActivity.class);
            startActivity(intent);
            finish();
        });

    }
}
