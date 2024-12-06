package com.x_force.unimar.Item;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.x_force.unimar.MainActivity;
public class Tutoring extends Item{

    private static FirebaseFirestore db = MainActivity.getDB();    //database öğesi
    private String name;
    private String desc;
    //private User user;
    private String docId;
    private int cost = 0;

    public Tutoring(String name, String desc, String docId, int cost) { //user + category de olması lazım
        this.setName(name);
        this.setDesc(desc);
        this.setCost(cost);
        this.setDocId(docId);
    }

    public void deleteFromList(){   // tutoringi siliyo databaseden
        db.collection("tutorListing").document(this.getDocId()).delete(); //document id li tutor listingi siliyo
    }

    public static void displayListings(){      //duzgun calısmıyo     //tutoring documentlerini gösteriyo?? ui ya displaylerken işimize yarayabilir
        db.collection("tutorListing").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                Tutoring tutoring = document.toObject(Tutoring.class);
                Log.d("Firestore","Document Id: " + tutoring.getDocId());
            }
        }).addOnFailureListener(e ->{
            Log.w("Firestore","Error getting documents", e);
        });
    }

}
