package com.x_force.unimar.Item;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.x_force.unimar.MainActivity;

import java.util.ArrayList;

public class Product extends Item{

    public Product(String name, String desc, ArrayList<String> category, int cost) { // user olması lazım
        name = name.toUpperCase();
        this.setName(name);
        this.setDesc(desc);
        this.setCost(cost);
        this.setCategory(category);
        ItemManager.addToList(this);
    }

    public Product(){}
}