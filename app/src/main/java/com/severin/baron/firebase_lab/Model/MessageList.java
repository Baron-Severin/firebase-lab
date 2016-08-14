//package com.severin.baron.firebase_lab.Model;
//
//import com.fasterxml.jackson.annotation.JsonAnyGetter;
//import com.fasterxml.jackson.annotation.JsonAnySetter;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by erikrudie on 8/13/16.
// */
//public class MessageList {
//
////    private List<Message> messageList;
//    private Map<String, List<Message>> messageList;
//
//    public MessageList() {
//    }
//
////    @JsonCreator
//    public MessageList(Map<String, List<Message>> messages) {
//        this.messageList = messages;
//    }
//
////    public List<Message> getMessageList() {
////        return messageList;
////    }
////
////    public void setMessageList(List<Message> messageList) {
////        this.messageList = messageList;
////    }
//
//
//    @JsonAnyGetter
//    public Map<String, List<Message>> getMessageList() {
//        return messageList;
//    }
//
//    @JsonAnySetter
//    public void setMessageList(Map<String, List<Message>> messageList) {
//        this.messageList = messageList;
//    }
//
//}