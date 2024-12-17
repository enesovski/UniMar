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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.x_force.unimar.R;
import com.x_force.unimar.chat.ChatActivity;
import com.x_force.unimar.chat.ChatRoom;
import com.x_force.unimar.login.User;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;

public class RecentChat extends FirestoreRecyclerAdapter<ChatRoom, RecentChat.ChatRoomViewHolder> {
    private Context context;
    public RecentChat(@NonNull FirestoreRecyclerOptions<ChatRoom> options, Context context) {
        super(options);
        this.context = context;
    }

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    @Override
    protected void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position, @NonNull ChatRoom model) {
        AtomicBoolean isExists = new AtomicBoolean(true);
        String chatRoomId;
        if(model.getUserIds().get(0).hashCode()<model.getUserIds().get(1).hashCode()){
            chatRoomId= model.getUserIds().get(0)+"_"+model.getUserIds().get(1);
        }else{
            chatRoomId= model.getUserIds().get(1)+"_"+model.getUserIds().get(0);
        }
        Query query = FirebaseFirestore.getInstance().collection("chatrooms").whereEqualTo("roomId",chatRoomId);
        query.get().addOnCompleteListener(task -> {
          if (task.getResult().isEmpty()){
              isExists.set(false);
          }
        });
        if(model.getUserIds().get(1).equals(FirebaseAuth.getInstance().getUid()) && isExists.get()){
            FirebaseFirestore.getInstance().collection("users").document(model.getUserIds().get(0)).get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    boolean MessageSendByMe=model.getLastSenderId().equals(FirebaseAuth.getInstance().getUid());
                    User user = task.getResult().toObject(User.class);
                    if(user!=null){
                        holder.emailText.setText(user.getEmail().substring(0,5));
                        if(MessageSendByMe){
                            if(model.getLastMessage()!=null){
                                holder.last_message_sender.setText("You:"+ model.getLastMessage());
                            }
                        }else{
                            if(model.getLastMessage()!=null){
                                holder.last_message_sender.setText(user.getName()+":"+model.getLastMessage());
                            }
                        }
                        holder.messageTime.setText(model.getLastMessage());
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        sdf.setTimeZone(TimeZone.getDefault()); // Uses the device's default time zone
                        String formattedTime = sdf.format(model.getLastMessageSendTime().toDate());
                        holder.messageTime.setText(formattedTime);
                        holder.itemView.setOnClickListener(v -> {
                            Intent intent = new Intent(context, ChatActivity.class);
                            intent.putExtra("name",user.getName());
                            intent.putExtra("userId",user.getUserId());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        });
                    }

                }
            });
        }else if(model.getUserIds().get(0).equals(FirebaseAuth.getInstance().getUid()) && isExists.get()){
            FirebaseFirestore.getInstance().collection("users").document(model.getUserIds().get(1)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean MessageSendByMe = model.getLastSenderId().equals(FirebaseAuth.getInstance().getUid());
                        User user = task.getResult().toObject(User.class);
                        if (user != null){
                            holder.emailText.setText(user.getEmail().substring(0,5));
                            if(MessageSendByMe){
                                if(model.getLastMessage()!=null){
                                    holder.last_message_sender.setText("You:"+ model.getLastMessage());
                                }
                            }else{
                                if(model.getLastMessage()!=null){
                                    holder.last_message_sender.setText(user.getName()+":"+model.getLastMessage());
                                }
                            }
                          //  holder.messageTime.setText(model.getLastMessage());
                            holder.messageTime.setText(new SimpleDateFormat("HH:MM").format(model.getLastMessageSendTime().toDate()));
                            holder.itemView.setOnClickListener(v -> {
                                Intent intent = new Intent(context, ChatActivity.class);
                                intent.putExtra("name", user.getName());
                                intent.putExtra("userId", user.getUserId());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                        }


                    }
                }
            });
        }


    }

    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_chat_row, parent, false);
        return new ChatRoomViewHolder(view);
    }
    class ChatRoomViewHolder extends RecyclerView.ViewHolder{
        TextView emailText;
        TextView messageText;
        TextView messageTime;
        TextView last_message_sender;

        public ChatRoomViewHolder(@NonNull View itemView) {
            super(itemView);
            last_message_sender=itemView.findViewById(R.id.last_message_sender);
            emailText = itemView.findViewById(R.id.name);
            messageText=itemView.findViewById(R.id.lastMessageText);
            messageTime= itemView.findViewById(R.id.lastMessageTime);

        }
    }
}
