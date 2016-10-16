package com.streamdata.apps.cryptochat.models;

/**
 * Network message model for REST service communication
 */
public class RMessage {
    private final int id;
    private final String receiverId;
    private final String senderId;
    private final String data;
    private final String sentTime;

    public int getId() {
        return id;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getData() {
        return data;
    }

    public String getSentTime() {
        return sentTime;
    }

    public RMessage(int id, String senderId, String receiverId, String data, String sentTime) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.data = data;
        this.sentTime = sentTime;
    }
}
