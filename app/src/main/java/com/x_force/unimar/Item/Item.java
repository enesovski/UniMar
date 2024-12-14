package com.x_force.unimar.Item;

import com.google.firebase.firestore.FirebaseFirestore;
import com.x_force.unimar.MainActivity;

public class Item {
    private static FirebaseFirestore db = MainActivity.getDB();
    private String name;
    private String desc;
    private int cost;
    private String docId;
    private String category;

    public Item(){

    }
    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
        //db.collection("tutorListing").document(this.getDocId()).update("docId",docId);
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
        //db.collection("tutorListing").document(this.getDocId()).update("desc",desc);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        //db.collection("tutorListing").document(this.getDocId()).update("name",name);
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
        //db.collection("tutorListing").document(this.getDocId()).update("cost",cost);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
        //db.collection("productListing").document(this.getDocId()).update("category",category);
    }

}
