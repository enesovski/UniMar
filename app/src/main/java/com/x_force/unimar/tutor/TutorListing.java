package com.x_force.unimar.tutor;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.x_force.unimar.MainActivity;
public class TutorListing implements IListing{

    private static FirebaseFirestore db = MainActivity.getDB();    //database öğesi
    private String name;
    private String desc;
    //private User user;
    private int cost = 0;
    private String docId;

    public TutorListing(String name, String desc, int cost) { //user + category de olması lazım
        this.setName(name);
        this.setDesc(desc);
        this.setCost(cost);
        this.addListing(); // metodun içinde setDocId yapıyo
        Log.d("Firesbase" ,"addin dışı " + this.getDocId());
    }
    @Override
    public void addListing(){    // databaseye ekliyo öğeyi ve ona özel documentId veriyo vermio???
        db.collection("tutorListing").add(this).addOnSuccessListener(documentReference -> {
            this.setDocId(documentReference.getId());     //calısmıyo    // üstteki add methodu yeni bi document referance döndürüyo bu otomatik document Idsi yaratıyo
            documentReference.update("name",this.getName(),"desc",this.getDesc(),
                                        "cost",this.getCost(),"docId",this.getDocId());
            Log.d("Firebase", "addin içi " + this.getDocId());
            Log.d("Firestore", "Document added with Id: " + this.getDocId()); // sysout
        }).addOnFailureListener(e -> {
            Log.w("Firestore", "Error adding document!", e); //sysout
        });

        Log.d("Firebase", "addinsonu" + this.getDocId());
    }

    @Override
    public void deleteListing(){   // tutoringi siliyo databaseden
        db.collection("tutorListing").document(this.getDocId()).delete(); //document id li tutor listingi siliyo
    }

    public static void displayListings(){      //duzgun calısmıyo     //tutoring documentlerini gösteriyo?? ui ya displaylerken işimize yarayabilir
        db.collection("tutorListing").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                TutorListing tutorListing = document.toObject(TutorListing.class);
                Log.d("Firestore","Document Id: " + tutorListing.getDocId());
            }
        }).addOnFailureListener(e ->{
            Log.w("Firestore","Error getting documents", e);
        });
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


}
