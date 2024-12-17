package com.x_force.unimar.chat;
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
import com.google.firebase.firestore.SetOptions;
import com.x_force.unimar.R;
import com.x_force.unimar.chat.adapters.ChatViewAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    String currentUserId;
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

        String chatRoomId = createChatRoomId(currentUserId, userId);
        Log.d("ChatActivity", "Generated chatRoomId: " + chatRoomId);

        Query query = db.collection("chatrooms")
                .document(chatRoomId)
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
                sendMessage(chatRoomId, currentUserId, userId, messageContent);
                messageEditText.setText(""); // Clear the input field
            }
        });
        listenForNewMessages(chatRoomId, userId, db);
        creatingChatRoom(chatRoomId);
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

            // Update the document with the new field
        Map<String, Object> data = new HashMap<>();
        data.put("userIds", Arrays.asList(currentUserId, userId));

        // Use set() with SetOptions.merge() to add the array without overwriting
        db.collection("chatrooms")
                .document(chatRoomId)
                .set(data, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Array field added successfully!");
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error adding array field", e);
                });
        Map<String, Object> data2 = new HashMap<>();
        data.put("roomId", chatRoomId);
        db.collection("chatrooms")
                .document(chatRoomId)
                .update(data2)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Array field added successfully!");
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error adding array field", e);
                });

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

    private void creatingChatRoom(String chatId){
        FirebaseFirestore.getInstance().collection("chatrooms").document(chatId).get().addOnCompleteListener(task->{
            if(task.isSuccessful()){
                ChatRoom chatroom=task.getResult().toObject(ChatRoom.class);
                if(chatroom==null){
                    ArrayList<String> userIDs=new ArrayList<>();
                    userIDs.add(currentUserId);
                    userIDs.add(userId);
                    chatroom=new ChatRoom(chatId,"", userIDs,Timestamp.now());
                }
                FirebaseFirestore.getInstance().collection("chatrooms").document(chatId).set(chatroom);
            }
        });

    }
}