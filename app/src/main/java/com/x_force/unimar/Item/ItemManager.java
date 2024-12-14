package com.x_force.unimar.Item;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.x_force.unimar.ProductListingActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemManager{
    static FirebaseFirestore db = ProductListingActivity.getDb();
    static Query productQuery = db.collection("productListing");
    static Query tutoringQuery = db.collection("tutorListing");
    static ItemAdapter adapter = ProductListingActivity.getAdapter();

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
    public static void deleteFromList(Item item){
        if(item instanceof Product) {
            db.collection("productListing").document(item.getDocId()).delete().addOnSuccessListener(documentReference -> {
                Log.d("Firestore", "Document deleted with Id: " + item.getDocId());
            }).addOnFailureListener(e -> {
                Log.w("Firestore", "Error deleting document", e);
            });
        }
        else if(item instanceof Tutoring) {
            db.collection("tutorListing").document(item.getDocId()).delete().addOnSuccessListener(documentReference -> {
                Log.d("Firestore", "Document deleted with Id: " + item.getDocId());
            }
            ).addOnFailureListener(e -> {
                Log.w("Firestore", "Error deleting document", e);
            });
        }
    }
    public static List<Item> sortProductList(char c) { // eğer c 'A' karakteri ise artan fiyata göre sıralıyor. D--> azalan. İkisi de değilse sortlanmamış databaseyi yazdırıyor
        ArrayList<Item> items = new ArrayList<>();
        c = Character.toUpperCase(c);
        if(c == 'A'){
            productQuery = productQuery.orderBy("cost", Query.Direction.ASCENDING);
        }
        else if(c == 'D'){
            productQuery = productQuery.orderBy("cost", Query.Direction.DESCENDING);
        }
        else {

        }
        productQuery.get().addOnCompleteListener(done ->{
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
    public static List<Item> sortTutorList(char c) {
        List<Item> items = Collections.emptyList();
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
    public static List<Item> filterProductList(double min, double max){
        List<Item> items = Collections.emptyList();
        productQuery = productQuery.whereGreaterThanOrEqualTo("cost",min);

        productQuery = productQuery.whereLessThanOrEqualTo("cost", max);

        productQuery.get().addOnCompleteListener(done ->{
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
    public static Query getProductQuery(){
        return productQuery;
    }
    public static void refreshProductQuery(){
        productQuery = db.collection("productListing");
    }
    public static Query getTutorQuery(){
        return tutoringQuery;
    }
    public static void refreshTutorQuery(){
        tutoringQuery = db.collection("tutorListing");
    }
}