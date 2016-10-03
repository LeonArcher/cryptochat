package com.streamdata.apps.cryptochat.models;

import java.util.Date;
import com.streamdata.apps.cryptochat.models.Contact;

public class Message {
    public static final int EMPTY_ID = 0;

    private final int id;
    private final Contact sender;
    private final Contact receiver;
    private final Date date;
    private final String text;


    public Message(int id, Contact sender, Contact receiver, Date date, String text) {

        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.date = date;
        this.text = text;
    }

    @Override
    public String toString() {
        return getText();
    }

    public int getId() { return id; }
    public Contact getSender() {
        return sender;
    }
    public Contact getReceiver() {
        return receiver;
    }
    public Date getDate() { return date; }
    public String getText() {
        return text;
    }
}
