package com.x_force.unimar.chat.notification;

import com.google.firebase.Timestamp;
import com.x_force.unimar.chat.Message;

public class Notification {
    String userId;
    Message message;
    String senderId;
    Timestamp timestamp;

    public Notification(){

    }

    public Notification(String userId, Message message, String senderId, Timestamp timestamp) {
        this.userId = userId;
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
