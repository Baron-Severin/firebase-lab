package com.severin.baron.firebase_lab.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erikrudie on 8/13/16.
 */
public class Room {

    final int roomId;
    String roomDisplayName;
    List<User> roomUsers;
    List<Message> allMessages;

    @JsonCreator
    public Room(@JsonProperty("roomId") int roomId,
                @JsonProperty("roomDisplayName") String roomDisplayName) {
        this.roomId = roomId;
        this.roomDisplayName = roomDisplayName;
        this.roomUsers = new ArrayList<>();
        this.allMessages = new ArrayList<>();
    }

    public int getRoomId() {
        return roomId;
    }

    public String getRoomDisplayName() {
        return roomDisplayName;
    }

    public void setRoomDisplayName(String roomDisplayName) {
        this.roomDisplayName = roomDisplayName;
    }

    public List<User> getRoomUsers() {
        return roomUsers;
    }

    public void setRoomUsers(List<User> roomUsers) {
        this.roomUsers = roomUsers;
    }

    public List<Message> getAllMessages() {
        return allMessages;
    }

    public void setAllMessages(List<Message> allMessages) {
        this.allMessages = allMessages;
    }
}
