package com.x_force.unimar.chat.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.x_force.unimar.R;
import com.x_force.unimar.chat.Message;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatViewAdapter extends FirestoreRecyclerAdapter<Message, ChatViewAdapter.MessageHolder> {
    private Context context;
    public ChatViewAdapter(@NonNull FirestoreRecyclerOptions<Message> options, Context context) {
        super(options);
        this.context = context;
    }

    // SuppressLint("SetTextI18n") string işlemleri için lazımmış
    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull MessageHolder holder, int position, @NonNull Message model) {
        if (model.getSenderId().equals(FirebaseAuth.getInstance().getUid())) {
            holder.sender_layout.setVisibility(View.GONE);
            holder.reciever_layout.setVisibility(View.VISIBLE);
            holder.reciever_text.setText(model.getContent());
            holder.recieverTime.setText(formatTimestamp(model.getTimestamp()));
        } else {
            holder.reciever_layout.setVisibility(View.GONE);
            holder.sender_layout.setVisibility(View.VISIBLE);
            holder.sender_text.setText(model.getContent());
            holder.senderTime.setText(formatTimestamp(model.getTimestamp()));
        }
    }

    private String formatTimestamp(Timestamp timestamp) {
        Date date = timestamp.toDate();
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date);
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_message_structure, parent, false);
        return new MessageHolder(view);
    }

    class MessageHolder extends RecyclerView.ViewHolder{
        LinearLayout sender_layout,reciever_layout;
        TextView sender_text,reciever_text,senderTime,recieverTime;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            reciever_layout=itemView.findViewById(R.id.reciever_layout);
            sender_layout=itemView.findViewById(R.id.sender_layout);
            sender_text=itemView.findViewById(R.id.senderText);
            senderTime =itemView.findViewById(R.id.senderTime);
            reciever_text=itemView.findViewById(R.id.recieverText);
            recieverTime =itemView.findViewById(R.id.receiverTime);

        }
    }
}
