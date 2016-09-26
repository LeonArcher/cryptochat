package com.streamdata.apps.cryptochat.models;

import java.util.Date;

public class Message {
    private final String text;
    private final Contact sender;
    private final Contact receiver;
    private final Date sentTime;

    public Message(Contact sender, Contact receiver, String text, Date sentTime) {

        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.sentTime = sentTime;
    }

    @Override
    public String toString() {
        return getText();
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
