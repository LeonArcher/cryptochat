package com.streamdata.apps.cryptochat.models;

import java.util.Date;


public class Message {
    public static final int EMPTY_ID = 0;

    private final int id;
    private final int senderId;
    private final int receiverId;
    private final Date date;
    private final String text;


    public Message(int id, int senderId, int receiverId, Date date, String text) {

        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.date = date;
        this.text = text;
    }

    @Override
    public String toString() {
        return getText();
    }

    public int getId() { return id; }
    public int getSenderId() {
        return senderId;
    }
    public int getReceiverId() {
        return receiverId;
    }
    public Date getDate() { return date; }
    public String getText() {
        return text;
    }
}
