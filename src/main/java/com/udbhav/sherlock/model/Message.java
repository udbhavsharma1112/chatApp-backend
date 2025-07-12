package com.udbhav.sherlock.model;

public class Message {
    private String messageId;
    private String receiverId;
    private String senderId;
    private String message;

    public Message() {}
    public Message(String messageId, String receiverId, String senderId, String message) {
        this.messageId = messageId;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.message = message;
    }
    public String getMessageId() {
        return messageId;
    }
    public String getReceiverId() {
        return receiverId;
    }
    public String getSenderId() {
        return senderId;
    }
    public String getMessage() {
        return message;
    }
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
