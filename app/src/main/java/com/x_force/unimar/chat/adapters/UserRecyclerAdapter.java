package com.x_force.unimar.chat.adapters;

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
import com.x_force.unimar.chat.ChatActivity;
import com.x_force.unimar.login.User;

import java.util.Objects;

public class UserRecyclerAdapter extends FirestoreRecyclerAdapter<User, UserRecyclerAdapter.UserViewHolder> {
    private Context context;

    public UserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<User> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User model) {
        if (model.getEmail() != null) {
            holder.emailText.setText(model.getEmail());
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("email", model.getEmail());
            intent.putExtra("userId", model.getUserId()); // Ensure 'userId' exists
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user, parent, false);
        return new UserViewHolder(view);
    }
    class UserViewHolder extends RecyclerView.ViewHolder{
        TextView emailText;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            emailText = itemView.findViewById(R.id.emailText);
        }
    }
}
