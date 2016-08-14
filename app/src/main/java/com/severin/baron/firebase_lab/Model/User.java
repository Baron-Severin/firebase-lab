package com.severin.baron.firebase_lab.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.severin.baron.firebase_lab.Utility.PH;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erikrudie on 8/13/16.
 */
public class User {

    private List<Room> activeInRooms;  // these contain room id/display only
    private String preferredTextColor;
    private String displayName;
    private final String userId;  // userId == gmail account
    private boolean changeFlag;

    public User(String userId) {
        this.userId = userId;
        this.activeInRooms = new ArrayList<>();
        Room placeholder = new Room(0, PH.PLACEHOLDER_ROOM);
//        activeInRooms.add(placeholder);
        this.preferredTextColor = PH.TEXT_BLACK;
    }

    @JsonCreator
    public User(@JsonProperty("userId") String userId,
//                @JsonProperty("activeInRooms") List<Room> activeInRooms,
                @JsonProperty("preferredTextColor") String preferredTextColor,
                @JsonProperty("displayName") String displayName,
                @JsonProperty("changeFlag") boolean changeFlag) {
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

    public boolean isChangeFlag() {
        return changeFlag;
    }

    public void clearActiveRooms() {
        activeInRooms.clear();
        Room placeholder = new Room(0, PH.PLACEHOLDER_ROOM);
        activeInRooms.add(placeholder);
    }
}
