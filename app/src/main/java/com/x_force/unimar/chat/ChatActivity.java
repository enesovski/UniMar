package com.x_force.unimar.chat;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.x_force.unimar.R;
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
        spokenUser=new User(getIntent().getStringExtra("ıd"), getIntent().getStringExtra("email"));
        createChatRoomId(spokenUser.getUserId(), Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        sendMessageButton=findViewById(R.id.sendButton);
        backButton=findViewById(R.id.backButton);
        profileButton=findViewById(R.id.profileButton);


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
        getChatroomReference(chatRoomId).get().addOnCompleteListener(task -> {
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
        });
    }
}