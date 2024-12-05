package com.x_force.unimar.chat;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.x_force.unimar.R;
import com.x_force.unimar.login.User;

import java.util.List;

public class SearchUserActivity extends AppCompatActivity {

    EditText searchInput;
    Button searchButton;
    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    UserRecyclerAdapter adapter;

    List<User> userList;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true) // Enable offline persistence (optional)
                .build();

        db.setFirestoreSettings(settings);
        searchInput = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        recyclerView = findViewById(R.id.recyclerView);


        searchButton.setOnClickListener(v ->{
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

        searchTerm = searchTerm.trim();

        //Creates a query which has users that includes search input
        Query query = db.collection("users").whereGreaterThanOrEqualTo("email", searchTerm).whereLessThan("email", searchTerm + "\uf8ff");

        //checks the query and prints the results
        query.get().addOnSuccessListener(querySnapshot -> {
            for (DocumentSnapshot document : querySnapshot) {
                Log.d("FirestoreQuery", "Document: " + document.getData());
            }
        }).addOnFailureListener(e -> {
            Log.d("FirestoreQuery", "Error getting documents: ", e);
        });


        //Sets the option of query and adapter
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();

        adapter = new UserRecyclerAdapter(options,getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(adapter != null){
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter != null){
            adapter.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter != null){
            adapter.startListening();
        }
    }
}