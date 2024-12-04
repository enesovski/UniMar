package com.x_force.unimar.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.x_force.unimar.R;
import com.x_force.unimar.login.User;

public class UserRecyclerAdapter extends FirestoreRecyclerAdapter<User, UserRecyclerAdapter.UserViewHolder> {
    private Context context;

    public UserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<User> options, Context context) {
        super(options);
        this.context = context;
    }

    //Sets the texts' values which came from database
    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User model) {
        if(model.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
            holder.emailText.setText(model.getEmail() + " (ME) ");
        }
        else {
            holder.emailText.setText(model.getEmail());
        }
        holder.itemView.setOnClickListener(v -> {
           Intent intent = new Intent(context, ChatActivity.class);
           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           context.startActivity(intent);
        });
    }

    //Creates viewHolder object
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user, parent, false);
        return new UserViewHolder(view);
    }

    //Holds userRow to add recyclerView
    class UserViewHolder extends RecyclerView.ViewHolder{
        TextView emailText;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            emailText = itemView.findViewById(R.id.emailText);
        }
    }
}
