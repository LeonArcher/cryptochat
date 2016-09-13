package com.streamdata.apps.cryptochat.models;

import java.util.Date;

/**
 * Created by Leon Archer on 12.09.2016.
 */
public class Message {

    private final String text;
    private final Contact sender;
    private final Date sentTime;

    public Message(Contact sender, String text, Date sentTime) {
        this.sender = sender;
        this.text = text;
        this.sentTime = sentTime;
    }

    public String getText() {
        return text;
    }

    public Contact getSender() {
        return sender;
    }

    public Date getSentTime() {
        return sentTime;
    }
}
