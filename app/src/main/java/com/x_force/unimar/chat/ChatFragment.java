package com.x_force.unimar.chat;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.x_force.unimar.R;
import com.x_force.unimar.chat.adapters.RecentChat;
import com.x_force.unimar.chat.adapters.UserRecyclerAdapter;
import com.x_force.unimar.login.User;

public class ChatFragment extends Fragment {

    RecyclerView recentChatrecyclerView;
    RecentChat adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public ChatFragment(){

    }
    public View oncreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view= inflater.inflate(R.layout.activity_search_user,container,false);
        recentChatrecyclerView =view.findViewById(R.id.recyclerView);
        return view;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void handleSearch() {


        //Creates a query which has users that includes search input
        Query query = db.collection("chatrooms").whereArrayContains("userIds",FirebaseAuth.getInstance().getUid() ).orderBy("lastMessageSendTime",Query.Direction.DESCENDING);

        //checks the query and prints the results
        query.get().addOnSuccessListener(querySnapshot -> {
            for (DocumentSnapshot document : querySnapshot) {
                Log.d("FirestoreQuery", "Document: " + document.getData());
            }
        }).addOnFailureListener(e -> {
            Log.d("FirestoreQuery", "Error getting documents: ", e);
        });


        //Sets the option of query and adapter
        FirestoreRecyclerOptions<ChatRoom> options = new FirestoreRecyclerOptions.Builder<ChatRoom>().setQuery(query, ChatRoom.class).build();

        adapter = new RecentChat(options,getContext());
        recentChatrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recentChatrecyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public void onStart() {
        super.onStart();
        if(adapter != null){
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null){
            adapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter != null){
            adapter.startListening();
        }
    }
}
