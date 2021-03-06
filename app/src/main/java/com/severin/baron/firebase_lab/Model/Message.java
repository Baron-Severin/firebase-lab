package com.severin.baron.firebase_lab.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.security.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by erikrudie on 8/13/16.
 */
public class Message implements Comparable<Message> {

    final String body;
    final Date timeStamp;
    final String userId;  // userId == gmail address
    final String displayName;

    public Message(String body, String userId, String displayName) {
        this.body = body;
        this.userId = userId;
        this.timeStamp = new Date();
        this.displayName = displayName;
    }

    @JsonCreator
    public Message(@JsonProperty("body") String body,
                   @JsonProperty("timeStamp") Date timeStamp,
                   @JsonProperty("userId") String userId,
                   @JsonProperty("displayName") String displayName) {
        this.body = body;
        this.timeStamp = timeStamp;
        this.userId = userId;
        this.displayName = displayName;
    }

    @Override
    public int compareTo(Message message) {
        return this.timeStamp.compareTo(message.getTimeStamp());
    }

    @Override
    public String toString() {
        return this.body;
    }

    public String getBody() {
        return body;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public String getUserId() {
        return userId;
    }
}
