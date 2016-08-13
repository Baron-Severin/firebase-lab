package com.severin.baron.firebase_lab.Model;

import java.util.List;

/**
 * Created by erikrudie on 8/13/16.
 */
public class MessageHolder {

    final String userId;  // userId == gmail address
    List<Message> messages;

    public MessageHolder(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
