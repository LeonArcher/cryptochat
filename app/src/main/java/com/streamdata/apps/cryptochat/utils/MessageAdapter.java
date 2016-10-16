package com.streamdata.apps.cryptochat.utils;

import com.streamdata.apps.cryptochat.models.Contact;
import com.streamdata.apps.cryptochat.models.Message;
import com.streamdata.apps.cryptochat.models.RMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Class containing conversions between message types
 */
public class MessageAdapter {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "EE, dd MMM yyyy HH:mm:ss z",
            Locale.US
    );

    public static Message toMessage(RMessage message) throws ParseException {

        // parse Date from string using server-specific format
        Date dateSentTime = dateFormat.parse(message.getSentTime());

        // TODO: load sender and receiver contact from the database
        Contact sender = new Contact(1, message.getSenderId(), "John Doe", null);
        Contact receiver = new Contact(1, message.getReceiverId(), "Jane Doe", null);

        return new Message(
                message.getId(),
                sender,
                receiver,
                message.getData(),
                dateSentTime
        );
    }

    public static RMessage toRMessage(Message message) {

        return new RMessage(
                message.getId(),
                message.getSender().getServerId(),
                message.getReceiver().getServerId(),
                message.getText(),
                dateFormat.format(message.getSentTime())
        );
    }
}
