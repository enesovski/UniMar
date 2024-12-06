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

public class ChatActivity extends AppCompatActivity {

    public static String otherUserId;

    public User spoken_user;

    public String chatroomıd;

    public ChatRoom new_chatroom;
    FirebaseFirestore db;
    Button send_message_button,back_button,profile_button;
    EditText message_edit_text;



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
        spoken_user=new User(getIntent().getStringExtra("email"),getIntent().getStringExtra("ıd"));
        db = FirebaseFirestore.getInstance();
        create_chatroomıd(FirebaseAuth.getInstance().getUid(),spoken_user.getUserId());
        send_message_button=findViewById(R.id.sendButton);
        back_button=findViewById(R.id.backButton);
        profile_button=findViewById(R.id.profileButton);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        CollectionReference usersCollection = db.collection("chat");

        // With a specified ID
        DocumentReference newDocumentRef = usersCollection.document();

//        creating_chat_room();

    }

    protected void create_chatroomıd(String ıd1,String ıd2){
        if(ıd1.hashCode()<ıd2.hashCode()){
            this.chatroomıd= ıd1+"_"+ıd2;
        }else{
            this.chatroomıd=ıd2+"_"+ıd1;
        }
    }
    protected  DocumentReference getChatroomReference(String chatroomıd){
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomıd);
    }
    protected void creating_chat_room(){
        getChatroomReference(chatroomıd).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                System.out.println("ne oluyorrr");
                new_chatroom=task.getResult().toObject(ChatRoom.class);
                if(new_chatroom==null){
                    ArrayList<String> chatroomids=new ArrayList<>();
                    chatroomids.add(FirebaseAuth.getInstance().getUid());
                    chatroomids.add(spoken_user.getUserId());
                    new_chatroom=new ChatRoom(
                            chatroomıd,
                            "",
                            chatroomids,
                            Timestamp.now()

                    );
                    getChatroomReference(chatroomıd).set(new_chatroom);
                }

            }
        });
    }
}