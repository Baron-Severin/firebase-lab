package com.severin.baron.firebase_lab.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erikrudie on 8/13/16.
 */
public class Room implements Comparable<Room> {

    final long roomId;
    String roomDisplayName;
    List<User> roomUsers;
//    MessageList messageList;

//    @JsonCreator
//    public Room(@JsonProperty("roomId") long roomId,
//                @JsonProperty("roomDisplayName") String roomDisplayName,
//                @JsonProperty("messageList") MessageList allMessages) {

    @JsonCreator
    public Room(@JsonProperty("roomId") long roomId,
                @JsonProperty("roomDisplayName") String roomDisplayName) {
        this.roomId = roomId;
        this.roomDisplayName = roomDisplayName;
        this.roomUsers = new ArrayList<>();
//        this.messageList = allMessages;
    }

//    public Room(long roomId, String roomDisplayName) {
//        this.roomId = System.currentTimeMillis();
//        this.roomDisplayName = roomDisplayName;
//        this.roomUsers = new ArrayList<>();
////        this.messageList = new MessageList();
//    }

    @Override
    public int compareTo(Room room) {
        return (int) (this.roomId - room.getRoomId());
    }

    public long getRoomId() {
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

//    public MessageList getAllMessages() {
//        return messageList;
//    }
//
//    public void setAllMessages(MessageList allMessages) {
//        this.messageList = allMessages;
//    }
}
