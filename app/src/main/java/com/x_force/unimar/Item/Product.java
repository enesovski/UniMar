package com.x_force.unimar.Item;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.x_force.unimar.MainActivity;

public class Product extends Item{

    private static FirebaseFirestore db = MainActivity.getDB();
    private String name;
    private String desc;
    private int cost;
    private String docId;
    private String category;

    public Product(String name, String desc, String category, int cost) { // user + category de olması lazım
        this.setName(name);
        this.setDesc(desc);
        this.setCost(cost);
        this.setCategory(category);
    }

    public void deleteFromList() {
        db.collection("productListing").document(this.getDocId()).delete().addOnSuccessListener(documentReference -> {
            Log.d("Firestore", "Documnet deleted with Id: " + this.getDocId());
        }).addOnFailureListener(e -> {
            Log.d("Firestore", "Error deleting document", e);
        });
    }


    public static void filterList(double min, double max){
        Query query = db.collection("productListing")
                .whereGreaterThanOrEqualTo("cost",min);

        query = query.whereLessThanOrEqualTo("cost", max);

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
