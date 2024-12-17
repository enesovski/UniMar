package com.x_force.unimar.Item;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.x_force.unimar.login.User;

import java.util.ArrayList;

public class Item {
    private String name;
    private String desc;
    private int cost;
    private String docId;
    private String UserId;
    private String UserName;
    private ArrayList<String> category;

    public Item(){

    }

    public void setUserName(String UserName){
        this.UserName = UserName;
    }

    public String getUserName(){
        return UserName;
    }

    public void setUserId(String UserId){
        this.UserId = UserId;
    }

    public String getUserId(){
        return UserId;
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

    public ArrayList<String> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<String> category) {
        this.category = category;
        //db.collection("productListing").document(this.getDocId()).update("category",category);
    }

}
