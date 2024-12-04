package com.x_force.unimar.tutor;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.x_force.unimar.MainActivity;

public class Product implements IListing{

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
        this.addToList();
    }

    @Override
    public void addToList() {
        db.collection("productListing").add(this).addOnSuccessListener(documentReference -> {
            this.setDocId(documentReference.getId());
//            this.docId = documentReference.getId();     //calısmıyo    // üstteki add methodu yeni bi document referance döndürüyo bu otomatik document Idsi yaratıyo
//            documentReference.update("name", this.getName(), "desc", this.getDesc(),
//                    "cost", this.getCost(), "docId", this.getDocId());
            Log.d("Firestore", "Document added with Id: " + this.getDocId()); // sysout
        }).addOnFailureListener(e -> {
            Log.w("Firestore", "Error adding document!", e); //sysout
        });
    }

    @Override
    public void deleteFromList() {
        db.collection("productListing").document(this.getDocId()).delete().addOnSuccessListener(documentReference -> {
            Log.d("Firestore", "Documnet deleted with Id: " + this.getDocId());
        }).addOnFailureListener(e -> {
            Log.d("Firestore", "Error deleting document", e);
        });
    }

    public static void sortList(char c){ // eğer c 'A' karakteri ise artan fiyata göre sıralıyor. D--> azalan. İkisi de değilse sortlanmamış databaseyi yazdırıyor
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        //db.collection("productListing").document(this.getDocId()).update("name",name);
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
        //db.collection("productListing").document(this.getDocId()).update("desc",desc);
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
        //db.collection("productListing").document(this.getDocId()).update("cost",cost);
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
        //db.collection("productListing").document(this.getDocId()).update("docId",docId);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
        //db.collection("productListing").document(this.getDocId()).update("category",category);
    }
}
