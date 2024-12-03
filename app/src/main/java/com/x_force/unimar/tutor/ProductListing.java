package com.x_force.unimar.tutor;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.x_force.unimar.MainActivity;

public class ProductListing implements IListing{

    private static FirebaseFirestore db = MainActivity.getDB();
    private String name;
    private String desc;
    private int cost;
    private String docId;

    public ProductListing(String name, String desc, int cost) { // user + category de olması lazım
        this.setName(name);
        this.setDesc(desc);
        this.setCost(cost);
        this.addListing();
    }

    @Override
    public void addListing() {
        db.collection("productListing").add(this).addOnSuccessListener(documentReference -> {
            this.setDocId(documentReference.getId());     //calısmıyo    // üstteki add methodu yeni bi document referance döndürüyo bu otomatik document Idsi yaratıyo
            documentReference.update("name",this.getName(),"desc",this.getDesc(),
                    "cost",this.getCost(),"docId",this.getDocId());

            Log.d("Firestore", "Document added with Id: " + this.getDocId()); // sysout
        }).addOnFailureListener(e -> {
            Log.w("Firestore", "Error adding document!", e); //sysout
        });
    }

    @Override
    public void deleteListing() {
        db.collection("productListing").document(this.getDocId()).delete().addOnSuccessListener(documentReference -> {
            Log.d("Firestore", "Documnet deleted with Id: " + this.getDocId());
        }).addOnFailureListener(e -> {
            Log.d("Firestore", "Error deleting document", e);
        });
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
}
