package com.x_force.unimar.chat.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.x_force.unimar.R;
import com.x_force.unimar.chat.ChatActivity;
import com.x_force.unimar.chat.ChatRoom;
import com.x_force.unimar.login.User;

import java.text.SimpleDateFormat;
import java.util.Objects;

public class RecentChat extends FirestoreRecyclerAdapter<ChatRoom, RecentChat.ChatRoomViewHolder> {
    private Context context;


    public RecentChat(@NonNull FirestoreRecyclerOptions<ChatRoom> options, Context context) {
        super(options);
        this.context = context;
    }

    //Sets the texts' values which came from database
    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    @Override
    protected void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position, @NonNull ChatRoom model) {
        if(model.getUserIds().get(1).equals(FirebaseAuth.getInstance().getUid())){


            FirebaseFirestore.getInstance().collection("users").document(model.getUserIds().get(0)).get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    boolean MessageSendByMe=model.getLastSenderId().equals(FirebaseAuth.getInstance().getUid());
                    User user = task.getResult().toObject(User.class);
                    if(user!=null){
                        if(!MessageSendByMe){
                            holder.emailText.setText("You:"+ user.getEmail());
                        }else{
                            holder.emailText.setText(user.getEmail());
                        }
                        holder.messageTime.setText(model.getLastMessage());
                        holder.messageTime.setText(new SimpleDateFormat("HH:mm").format(model.getLastMessageSendTime().toDate()));
                        holder.itemView.setOnClickListener(v -> {
                            Intent intent = new Intent(context, ChatActivity.class);
                            intent.putExtra("email",user.getEmail());
                            intent.putExtra("ıd",user.getUserId());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        });
                    }

                }
            });
        } else if(model.getUserIds().get(0).equals(FirebaseAuth.getInstance().getUid())){
            FirebaseFirestore.getInstance().collection("users").document(model.getUserIds().get(1)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean MessageSendByMe = model.getLastSenderId().equals(FirebaseAuth.getInstance().getUid());
                        User user = task.getResult().toObject(User.class);
                        if (user != null){
                            if (!MessageSendByMe) {
                                holder.emailText.setText("You:" + user.getEmail());
                            } else {
                                holder.emailText.setText(user.getEmail());
                            }
                            holder.messageTime.setText(model.getLastMessage());
                            holder.messageTime.setText(new SimpleDateFormat("HH:MM").format(model.getLastMessageSendTime().toDate()));
                            holder.itemView.setOnClickListener(v -> {
                                Intent intent = new Intent(context, ChatActivity.class);
                                intent.putExtra("email", user.getEmail());
                                intent.putExtra("ıd", user.getUserId());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                        }


                    }
                }
            });
        }


    }

    //Creates viewHolder object
    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_chat_row, parent, false);
        return new ChatRoomViewHolder(view);
    }

    //Holds ChatRoomRow to add recyclerView
    class ChatRoomViewHolder extends RecyclerView.ViewHolder{
        TextView emailText;
        TextView messageText;
        TextView messageTime;

        public ChatRoomViewHolder(@NonNull View itemView) {
            super(itemView);
            emailText = itemView.findViewById(R.id.emailText);
            messageText=itemView.findViewById(R.id.lastMessageText);
            messageTime= itemView.findViewById(R.id.lastMessageTime);

        }
    }
}
