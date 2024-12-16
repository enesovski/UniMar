package com.x_force.unimar.chat;

import com.google.firebase.Timestamp;
import java.util.ArrayList;

public class ChatRoom {

    Timestamp lastMessageSendTime;
    String roomId,lastSenderId;

    ArrayList<String> userIds;

    String lastMessage;

    public ChatRoom() {};

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public ChatRoom(String roomId, String lastSenderId, ArrayList<String> userIds, Timestamp lastMessageSendTime) {
        this.roomId = roomId;
        this.lastSenderId = lastSenderId;
        this.userIds = userIds;
        this.lastMessageSendTime = lastMessageSendTime;
    }

    public Timestamp getLastMessageSendTime() {
        return lastMessageSendTime;
    }

    public void setLastMessageSendTime(Timestamp lastMessageSendTime) {
        this.lastMessageSendTime = lastMessageSendTime;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getLastSenderId() {
        return lastSenderId;
    }

    public void setLastSenderId(String lastSenderId) {
        this.lastSenderId = lastSenderId;
    }

    public ArrayList<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(ArrayList<String> userIds) {
        this.userIds = userIds;
    }
}
