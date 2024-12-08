package com.x_force.unimar;

import android.os.Bundle;
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

import java.util.List;

public class ListingActivity extends AppCompatActivity {
    static FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tutor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        showList();
    }

    public void showList(){
        ListView itemList = findViewById(R.id.item_list);

        List<Item> items = ItemManager.filterList(0,1000);

        ItemAdapter adapter = new ItemAdapter(this,items);

        itemList.setAdapter(adapter);
    }
}
