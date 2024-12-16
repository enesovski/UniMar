package com.x_force.unimar.chat;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class ChatHandler {
    private Message message;

    private final FirebaseFirestore db;
    private RecyclerView recyclerView;

    public ChatHandler(Message message, FirebaseFirestore db, RecyclerView recyclerView) {
        this.message = message;
        this.db = db;
        this.recyclerView = recyclerView;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public FirebaseFirestore getDb() {
        return db;
    }


    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }


    //Creates message with given parameters



    //Sends message to the firebase
    public void sendMessage(){
        db.collection("messages").add(message);
    }
}
