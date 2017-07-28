package com.streamdata.apps.cryptochat.utils;

import com.streamdata.apps.cryptochat.cryptography.CryptographerException;
import com.streamdata.apps.cryptochat.database.ContactNotFoundException;
import com.streamdata.apps.cryptochat.database.DBHandler;
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

    public static Message toMessage(RMessage message)
            throws ParseException, ContactNotFoundException, CryptographerException {

        // parse Date from string using server-specific format
        Date dateSentTime = dateFormat.parse(message.getSentTime());

        Contact sender = db.getContactByServerId(message.getSenderId());
        Contact receiver = db.getContactByServerId(message.getReceiverId());

        // if no such contacts available in database
        if (sender == null) {
            throw new ContactNotFoundException(message.getSenderId());
        }
        if (receiver == null) {
            throw new ContactNotFoundException(message.getReceiverId());
        }

        return new Message(
                message.getId(),
                sender.getId(),
                receiver.getId(),
                dateSentTime,
                message.getData()
        );
    }

    public static RMessage toRMessage(Message message) throws CryptographerException {

        DBHandler db = DBHandler.getInstance();

        Contact sender = db.getContact(message.getSenderId());
        Contact receiver = db.getContact(message.getReceiverId());

        // TODO: create custom exception
        if (sender == null || receiver == null) {
            throw new RuntimeException();
        }

        return new RMessage(
                message.getId(),
                sender.getServerId(),
                receiver.getServerId(),
                message.getText(),
                dateFormat.format(message.getDate())
        );
    }
}
