package com.streamdata.leon.cryptochat.models;

import java.util.Date;

/**
 * Created by Leon Archer on 12.09.2016.
 */
public class Message {
    public Message(Contact sender, String text, Date sent_time) {
        this.sender = sender;
        this.text = text;
        this.sent_time = sent_time;
    }

    public String getText() {
        return text;
    }

    public Contact getSender() {
        return sender;
    }

    public Date getSent_time() {
        return sent_time;
    }

    private String text;
    private Contact sender;
    private Date sent_time;
}
