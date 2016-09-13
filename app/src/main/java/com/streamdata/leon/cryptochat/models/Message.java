package com.streamdata.leon.cryptochat.models;

import java.util.Date;

/**
 * Created by Leon Archer on 12.09.2016.
 */
public class Message {

    public Message(Contact sender, Contact receiver, String text, Date sent_time) {

        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.sent_time = sent_time;
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

    public Date getSent_time() {
        return sent_time;
    }

    private String text;
    private Contact sender;
    private Contact receiver;
    private Date sent_time;
}
