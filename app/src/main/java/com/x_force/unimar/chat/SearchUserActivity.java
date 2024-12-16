package com.x_force.unimar.chat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.x_force.unimar.R;
import com.x_force.unimar.chat.adapters.RecentChat;
import com.x_force.unimar.chat.adapters.UserRecyclerAdapter;
import com.x_force.unimar.login.User;

public class SearchUserActivity extends AppCompatActivity {
    EditText searchInput;

    int count=0;
    Button searchButton;
    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    RecyclerView.Adapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_user);
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true) // Enable offline persistence (optional)
                .build();

        db.setFirestoreSettings(settings);
        searchInput = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        recyclerView = findViewById(R.id.recyclerView);

        if(count==0){
            handleSearch2();
        }
        searchButton.setOnClickListener(v ->{
            count++;

            String searchTerm = searchInput.getText().toString();
            if(searchTerm.isEmpty() || searchTerm.length()<3){
                searchInput.setError("Invalid Username");
                return;
            }
            handleSearch(searchTerm);
        });

    }
    @SuppressLint("NotifyDataSetChanged")
    private void handleSearch(String searchTerm) {
        Query query = db.collection("users")
                .whereGreaterThanOrEqualTo("name", searchTerm)
                .whereLessThan("name", searchTerm + "\uf8ff");

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        adapter = new UserRecyclerAdapter(options, getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        ((UserRecyclerAdapter) adapter).startListening();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void handleSearch2() {

        Query query = db.collection("chatrooms").whereArrayContains("userIds", FirebaseAuth.getInstance().getUid() ).orderBy("lastMessageSendTime",Query.Direction.DESCENDING);
        query.get().addOnSuccessListener(querySnapshot -> {
            for (DocumentSnapshot document : querySnapshot) {
                Log.d("FirestoreQuery", "Document: " + document.getData());
            }
        }).addOnFailureListener(e -> {
            Log.d("FirestoreQuery", "Error getting documents: ", e);
        });

        FirestoreRecyclerOptions<ChatRoom> options = new FirestoreRecyclerOptions.Builder<ChatRoom>().setQuery(query, ChatRoom.class).build();

        adapter = new RecentChat(options,getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
        ((RecentChat)adapter).startListening();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(adapter != null){
            if(count==0){
                ((RecentChat)adapter).startListening();
            }else{
                ((UserRecyclerAdapter)adapter).startListening();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter != null){
            if(count==0){
                ((RecentChat)adapter).stopListening();
            }else{
                ((UserRecyclerAdapter)adapter).stopListening();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter != null){
            if(count==0){
                ((RecentChat)adapter).startListening();
            }else{
                ((UserRecyclerAdapter)adapter).startListening();
            }

        }
    }
}