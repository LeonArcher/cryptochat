package com.streamdata.leon.cryptochat.models;

import java.util.Date;

/**
 * Created by Leon Archer on 12.09.2016.
 */
public class Message {
    final private String text;
    final private Contact sender;
    final private Contact receiver;
    final private Date sent_time;
    final public Boolean isMine;

    public Message(Contact sender, Contact receiver, String text, Date sent_time, Boolean isMine) {

        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.sent_time = sent_time;
        this.isMine = isMine;
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


}
