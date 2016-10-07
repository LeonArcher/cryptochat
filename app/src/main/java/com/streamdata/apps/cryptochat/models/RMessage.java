package com.streamdata.apps.cryptochat.models;

import android.support.annotation.Nullable;
import android.util.Log;

import com.streamdata.apps.cryptochat.network.Parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    @Nullable public Message toMessage() {

        // parse Date from string using server-specific format
        Date dateSentTime;

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EE, dd MMM yyyy HH:mm:ss z",
                Locale.US
        );

        try {
            dateSentTime = dateFormat.parse(sentTime);

        } catch (ParseException ex) {
            Log.e(Parser.PARSER_LOG_TAG, null, ex);
            return null;
        }

        // TODO: load sender and receiver contact from the database
        Contact sender = new Contact(1, senderId, "John Doe", null);
        Contact receiver = new Contact(1, receiverId, "Jane Doe", null);

        return new Message(
                Message.EMPTY_ID,
                sender,
                receiver,
                data,
                dateSentTime
        );
    }
}
