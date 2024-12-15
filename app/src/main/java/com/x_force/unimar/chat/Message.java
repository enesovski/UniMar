package com.x_force.unimar.chat;

import android.os.Build;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.time.LocalDateTime;
import java.util.Date;

public class Message {
    private String content;
    private String senderId;
    private  Timestamp timestamp;



    public Message(){

    }

    public Message(String content, String senderId, Timestamp timestamp) {
        this.content = content;
        this.senderId = senderId;
        this.timestamp = timestamp;
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

    public void setSenderId(String senderId){
        this.senderId = senderId;
    }


    @NonNull
    public String toString() {
        return senderId + ": " + content + " (" + timestamp + ")";
    }

}
