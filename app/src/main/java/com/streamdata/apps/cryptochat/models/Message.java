package com.streamdata.apps.cryptochat.models;

import java.util.Date;

public class Message {
    // id to modify in database (as unique key)
    public static final int EMPTY_ID = 0;

    private final int id;
    private final String text;
    private final Contact sender;
    private final Contact receiver;
    private final Date sentTime;

    public Message(int id, Contact sender, Contact receiver, String text, Date sentTime) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.sentTime = sentTime;
    }

    @Override
    public String toString() {
        return getText();
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Contact getSender() {
        return sender;
    }

    public Contact getReceiver() {
        return receiver;
    }

    public Date getSentTime() {
        return sentTime;
    }
}
