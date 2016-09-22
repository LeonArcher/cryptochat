package com.streamdata.apps.cryptochat.models;

import java.util.Date;
import com.streamdata.apps.cryptochat.models.Contact;

public class Message {
    private final String text;
    private final Contact sender;
    private final Contact receiver;
    private final Date sentTime;
    private final Boolean isMine;

    public Message(Contact sender, Contact receiver, String text, Date sentTime, Boolean isMine) {

        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.sentTime = sentTime;
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

    public Date getSentTime() {
        return sentTime;
    }

    public Boolean getIsMine() { return isMine; }
}
