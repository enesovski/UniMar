package com.x_force.unimar.chat;

import androidx.annotation.NonNull;
import com.google.firebase.Timestamp;

public class Message {
    private String content;
    private String senderId;
    private String receiverId;
    private  Timestamp timestamp;

    public Message(){

    }
    public Message(String senderId, String receiverId, String content,  Timestamp timestamp) {
        this.content = content;
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.receiverId = receiverId;
    }

    public String getContent() {
        return  content;
    }

    public String getSenderId() {
        return senderId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setContent(String content){
        this.content = content;
    }

    @NonNull
    public String toString() {
        return senderId + ": " + content + " (" + timestamp + ")";
    }

}
