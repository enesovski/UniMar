package com.x_force.unimar.Item;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ItemManager {
    static FirebaseFirestore db;

    public static void addToList(Item item) {
        if(item instanceof Product) {
            db.collection("productListing").add(item).addOnSuccessListener(documentReference -> {
                item.setDocId(documentReference.getId());
                Log.d("Firestore", "Document added with Id: " + item.getDocId()); // sysout
            }).addOnFailureListener(e -> {
                Log.w("Firestore", "Error adding document!", e); //sysout
            });
        }
        else if(item instanceof Tutoring){
            db.collection("tutorListing").add(item).addOnSuccessListener(documentReference -> {
                item.setDocId(documentReference.getId());
                Log.d("Firestore", "Document added with Id: " + item.getDocId());
            }).addOnFailureListener(e -> {
                Log.w("Firestore", "Error adding document!", e);// sysout
            });
        }
    }
    public static void sortList(Item item, char c) { // eğer c 'A' karakteri ise artan fiyata göre sıralıyor. D--> azalan. İkisi de değilse sortlanmamış databaseyi yazdırıyor
        if(item instanceof Product){
            c = Character.toUpperCase(c);
            Query query;
            if(c == 'A'){
                query = db.collection("productListing").orderBy("cost", Query.Direction.ASCENDING);
            }
            else if(c == 'D'){
                query = db.collection("productListing").orderBy("cost", Query.Direction.DESCENDING);
            }
            else {
                query = db.collection("productListing");
            }

            query.get().addOnCompleteListener(done ->{
                if(done.isSuccessful()){
                    for(QueryDocumentSnapshot document : done.getResult()){
                        //Product product = document.toObject(Product.class);
                        Log.d("Firestore", document.getId() + " => " + document.getData());
                    }
                }else{
                    Log.w("Firestore", "Error getting documents: ", done.getException());
                }
            });
        }

        else if(item instanceof Tutoring){
            c = Character.toUpperCase(c);
            Query query;
            if(c == 'A'){
                query = db.collection("tutorListing").orderBy("cost", Query.Direction.ASCENDING);
            }
            else if(c == 'D'){
                query = db.collection("tutorListing").orderBy("cost", Query.Direction.DESCENDING);
            }
            else {
                query = db.collection("tutorListing");
            }

            query.get().addOnCompleteListener(done ->{
                if(done.isSuccessful()){
                    for(QueryDocumentSnapshot document : done.getResult()){
                        //Product product = document.toObject(Product.class);
                        Log.d("Firestore", document.getId() + " => " + document.getData());
                    }
                }else{
                    Log.w("Firestore", "Error getting documents: ", done.getException());
                }
            });
        }
    }
    public static List<Item> filterList(double min, double max){
        List<Item> items = Collections.emptyList();
        Query query = db.collection("productListing")
                .whereGreaterThanOrEqualTo("cost",min);

        query = query.whereLessThanOrEqualTo("cost", max);

        query.get().addOnCompleteListener(done ->{
            if(done.isSuccessful()){
                for(QueryDocumentSnapshot document : done.getResult()){
                    Product product = document.toObject(Product.class);
                    items.add(product);
                    Log.d("Firestore", document.getId() + " => " + document.getData());
                }
            }else{
                Log.w("Firestore", "Error getting documents: ", done.getException());
            }
        });

        return items;
    }
}