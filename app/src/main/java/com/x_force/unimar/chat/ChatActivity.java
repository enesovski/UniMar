package com.x_force.unimar.chat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.Manifest;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.x_force.unimar.R;
import com.x_force.unimar.chat.adapters.ChatViewAdapter;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    String currentUserId;
    private ChatRoom chatroom;
    String chatId;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view);

        userId = getIntent().getStringExtra("userId");
        String name = getIntent().getStringExtra("name");

        if (userId == null || name == null) {
            Toast.makeText(this, "Invalid user data. Returning to home.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchUserActivity.class);
            startActivity(intent);
            finish();
        });

        TextView chatTitle = findViewById(R.id.chatTitle);
        chatTitle.setText("Chat with " + name);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUserId = auth.getUid();

        if (currentUserId == null) {
            Toast.makeText(this, "You are not logged in.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        chatId = createChatRoomId(currentUserId, userId);
        Log.d("ChatActivity", "Generated chatRoomId: " + chatId);

        Query query = db.collection("chatrooms")
                .document(chatId)
                .collection("chats")
                .orderBy("timestamp", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();

        ChatViewAdapter adapter = new ChatViewAdapter(options, this);
        recyclerView.setAdapter(adapter);
        adapter.startListening();

        ImageButton sendButton = findViewById(R.id.sendButton);
        EditText messageEditText = findViewById(R.id.messageEditText);


        sendButton.setOnClickListener(v -> {
            String messageContent = messageEditText.getText().toString().trim();
            if (!messageContent.isEmpty()) {
                sendMessage(chatId, currentUserId, userId, messageContent);

                //text fieldi temizle
                messageEditText.setText("");
            }
        });
        listenForNewMessages(chatId, userId, db);
        creatingChatRoom(chatId);
    }

    private void sendMessage(String chatRoomId, String senderId, String receiverId, String content) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Message message = new Message(senderId, receiverId, content, new Timestamp(new Date()));

        db.collection("chatrooms")
                .document(chatRoomId)
                .collection("chats")
                .add(message)
                .addOnSuccessListener(documentReference -> Log.d("ChatActivity", "Message sent successfully"))
                .addOnFailureListener(e -> Log.e("ChatActivity", "Failed to send message", e));


        chatroom.setLastMessageSendTime(Timestamp.now());
        chatroom.setLastSenderId(FirebaseAuth.getInstance().getUid());
        chatroom.setLastMessage(message.getContent());
        FirebaseFirestore.getInstance().collection("chatrooms").document(chatId).set(chatroom);

    }

    private void listenForNewMessages(String chatRoomId, String otherUserId, FirebaseFirestore db) {
        db.collection("chatrooms")
                .document(chatRoomId)
                .collection("chats")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        Log.e("ChatActivity", "Listen failed.", e);
                        return;
                    }

                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        Message latestMessage = querySnapshot.getDocuments().get(0).toObject(Message.class);
                        if (latestMessage != null && !latestMessage.getSenderId().equals(FirebaseAuth.getInstance().getUid())) {
                            sendNotification(latestMessage);
                        }
                    }
                });
    }

    private void sendNotification(Message message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "chat_notifications")
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle("New message")
                .setContentText(message.getContent())
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }

    private String createChatRoomId(String id1, String id2) {
        return id1.compareTo(id2) < 0 ? id1 + "_" + id2 : id2 + "_" + id1;
    }

    private void creatingChatRoom(String chatId) {
        FirebaseFirestore.getInstance().collection("chatrooms").document(chatId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                chatroom = task.getResult().toObject(ChatRoom.class);
                if (chatroom == null) {
                    ArrayList<String> userIDs = new ArrayList<>();
                    userIDs.add(currentUserId);
                    userIDs.add(userId);
                    chatroom = new ChatRoom(chatId, "", userIDs, Timestamp.now());
                }
                FirebaseFirestore.getInstance().collection("chatrooms").document(chatId).set(chatroom);
            }
        });

    }
}