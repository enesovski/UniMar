package com.x_force.unimar.chat;

import android.os.Build;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.Date;

public class Message {
    private String content;
    private String senderId;
    private LocalDateTime timestamp;

    public Message(String content, String senderId) {
        this.content = content;
        this.senderId = senderId;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.timestamp = LocalDateTime.now();
        }
    }



    public String getContent() {
        return  content;
    }

    public String getSenderId() {
        return senderId;
    }

    public LocalDateTime getTimestamp() {
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
