package com.severin.baron.firebase_lab.Model;

import java.util.List;

/**
 * Created by erikrudie on 8/13/16.
 */
public class User {

    List<Room> activeInRooms;  // these contain room id/display only
    String preferredTextColor;
    String displayName;
    final String userId;  // userId == gmail account
    boolean changeFlag;


    public User(String userId) {
        this.userId = userId;
    }

    public User(String userId, List<Room> activeInRooms, String preferredTextColor, String displayName, boolean changeFlag) {
        this.userId = userId;
        this.activeInRooms = activeInRooms;
        this.preferredTextColor = preferredTextColor;
        this.displayName = displayName;
        this.changeFlag = changeFlag;
    }

    public List<Room> getActiveInRooms() {
        return activeInRooms;
    }

    public void setActiveInRooms(List<Room> activeInRooms) {
        this.activeInRooms = activeInRooms;
    }

    public String getPreferredTextColor() {
        return preferredTextColor;
    }

    public void setPreferredTextColor(String preferredTextColor) {
        this.preferredTextColor = preferredTextColor;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }
}
