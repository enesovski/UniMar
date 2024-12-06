package com.x_force.unimar.chat;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.x_force.unimar.R;
import com.x_force.unimar.chat.adapters.ChatViewAdapter;
import com.x_force.unimar.chat.adapters.UserRecyclerAdapter;
import com.x_force.unimar.login.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    public static String otherUserId;

    public User spokenUser;

    public String chatRoomId;

    public ChatRoom newChatRoom;
    FirebaseFirestore db;
    Button sendMessageButton,backButton,profileButton;
    EditText messageEditText;

    ChatViewAdapter adapter;
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        messageEditText=findViewById(R.id.messageEditText);
        spokenUser=new User(getIntent().getStringExtra("ıd"), getIntent().getStringExtra("email"));
        createChatRoomId(spokenUser.getUserId(), Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        sendMessageButton=findViewById(R.id.sendButton);
        backButton=findViewById(R.id.backButton);
        profileButton=findViewById(R.id.profileButton);
        recyclerView=findViewById(R.id.recyclerView);

        sendMessageButton.setOnClickListener((v)->{
            String messageText=messageEditText.getText().toString().trim();
            if(!messageText.isEmpty()){
                newChatRoom.setLastMessageSendTime(Timestamp.now());
                newChatRoom.setLastSenderId(FirebaseAuth.getInstance().getUid());
                getChatroomReference(chatRoomId).set(newChatRoom);
                Message  chatMessage=new Message(messageText,FirebaseAuth.getInstance().getUid(),Timestamp.now());
                getChatroomReference(chatRoomId).collection("chats").add(chatMessage).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            messageEditText.setText("");
                        }
                    }
                });

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        db = FirebaseFirestore.getInstance();

        CollectionReference usersCollection = db.collection("chat");

        // With a specified ID
        DocumentReference newDocumentRef = usersCollection.document();

        creatingChatRoom();
        ChatAdapterQuery();



    }

    protected void createChatRoomId(String id1,String id2){
        if(id1.hashCode()<id2.hashCode()){
            this.chatRoomId= id1+"_"+id2;
        }else{
            this.chatRoomId=id2+"_"+id1;
        }
    }
    protected  DocumentReference getChatroomReference(String chatRoomId){
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatRoomId);
    }
    protected void creatingChatRoom(){
        Query query = db.collection("chatrooms").whereEqualTo("roomId",chatRoomId);
        query.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        ArrayList<String> chatRoomIds=new ArrayList<>();
                        chatRoomIds.add(FirebaseAuth.getInstance().getUid());
                        chatRoomIds.add(spokenUser.getUserId());
                        newChatRoom=new ChatRoom(
                                chatRoomId,
                                "",
                                chatRoomIds,
                                Timestamp.now()

                        );
                        // At least one document contains the given ID
                        Log.d("kahya", "ID exists in a document field!");
                    } else {
                        ArrayList<String> chatRoomIds=new ArrayList<>();
                        chatRoomIds.add(FirebaseAuth.getInstance().getUid());
                        chatRoomIds.add(spokenUser.getUserId());
                        newChatRoom=new ChatRoom(
                                chatRoomId,
                                "",
                                chatRoomIds,
                                Timestamp.now()

                        );
                        getChatroomReference(chatRoomId).set(newChatRoom);
                        Log.d("kahya", "ID does not exist.");
                    }
                });
        /*getChatroomReference(chatRoomId).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                newChatRoom=task.getResult().toObject(ChatRoom.class);
                if(newChatRoom==null){
                    ArrayList<String> chatRoomIds=new ArrayList<>();
                    chatRoomIds.add(FirebaseAuth.getInstance().getUid());
                    chatRoomIds.add(spokenUser.getUserId());
                    newChatRoom=new ChatRoom(
                            chatRoomId,
                            "",
                            chatRoomIds,
                            Timestamp.now()

                    );
                    getChatroomReference(chatRoomId).set(newChatRoom);
                }

            }
        });*/
    }
    @SuppressLint("NotifyDataSetChanged")
    private void ChatAdapterQuery() {

        //Creates a query which has users that includes search input
        Query query = db.collection("chatrooms").document(chatRoomId).collection("chats").orderBy("timestamp",Query.Direction.DESCENDING);



        //Sets the option of query and adapter
        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>().setQuery(query, Message.class).build();

        adapter = new ChatViewAdapter(options,getApplicationContext());
        LinearLayoutManager layout_order=new LinearLayoutManager(this);
        layout_order.setReverseLayout(true);
        recyclerView.setLayoutManager(layout_order);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted( positionStart,itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });

    }

}