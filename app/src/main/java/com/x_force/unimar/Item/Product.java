package com.x_force.unimar.Item;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.x_force.unimar.MainActivity;

import java.util.ArrayList;

public class Product extends Item{

    private String image;
    public Product(String name, String desc, ArrayList<String> category, int cost,String image) { // user olması lazım
        name = name.toUpperCase();
        this.setName(name);
        this.setDesc(desc);
        this.setCost(cost);
        this.setCategory(category);
        this.setImage(image);
        ItemManager.addToList(this);
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getImage(){
      return image;
    };

    public Product(){}
}