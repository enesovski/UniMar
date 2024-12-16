package com.x_force.unimar;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.x_force.unimar.Item.Item;
import com.x_force.unimar.Item.ItemAdapter;
import com.x_force.unimar.Item.ItemManager;
import com.x_force.unimar.Item.Product;

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
        db = FirebaseFirestore.getInstance();
        ItemManager.refreshProductQuery();
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

        itemList.setAdapter(adapter);
    }

    public void setButtonInteractions(){
        Button GeneralSortButton = findViewById(R.id.sort_button);
        Button SortAscendingButton = findViewById(R.id.button_sort_ascending);
        Button SortDescendingButton = findViewById(R.id.button_sort_descending);

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
    }
    public static ItemAdapter getAdapter(){
        return adapter;
    }

    public static FirebaseFirestore getDb(){
        return db;
    }

}
