package com.x_force.unimar.chat;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class ChatRoom {

    Timestamp lastmessagesendtime;
    String roomid,lastsenderid;
    ArrayList<String> userIds;

    public ChatRoom(String roomid, String lastsenderid, ArrayList<String> userIds, Timestamp lastmessagesendtime) {
        this.roomid = roomid;
        this.lastsenderid = lastsenderid;
        this.userIds = userIds;
        this.lastmessagesendtime = lastmessagesendtime;
    }

    public Timestamp getLastmessagesendtime() {
        return lastmessagesendtime;
    }

    public void setLastmessagesendtime(Timestamp lastmessagesendtime) {
        this.lastmessagesendtime = lastmessagesendtime;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getLastsenderid() {
        return lastsenderid;
    }

    public void setLastsenderid(String lastsenderid) {
        this.lastsenderid = lastsenderid;
    }

    public ArrayList<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(ArrayList<String> userIds) {
        this.userIds = userIds;
    }
}
