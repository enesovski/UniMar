package com.x_force.unimar.Item;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.x_force.unimar.MainActivity;
public class Tutoring extends Item{
    public Tutoring(String name, String desc, String category, int cost) { //user + category de olması lazım
        this.setName(name);
        this.setDesc(desc);
        this.setCost(cost);
        this.setCategory(category);
        ItemManager.addToList(this);
    }

    public Tutoring(){}
}
