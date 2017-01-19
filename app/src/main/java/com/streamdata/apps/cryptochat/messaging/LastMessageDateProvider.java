package com.streamdata.apps.cryptochat.messaging;

import android.support.annotation.WorkerThread;

import com.streamdata.apps.cryptochat.database.DBHandler;
import com.streamdata.apps.cryptochat.models.Contact;
import com.streamdata.apps.cryptochat.models.Message;

import java.sql.SQLDataException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Provider for skipping already received messages via time-date field (stores target-date map)
 */
public class LastMessageDateProvider {

    private Map<String, Date> skipToDateTable = null;

    private final DBHandler db;

    public LastMessageDateProvider(DBHandler db) {
        this.db = db;
    }

    @WorkerThread
    public synchronized Date get(String targetId) {

        if (skipToDateTable == null) {
            init();
        }

        return skipToDateTable.get(targetId);
    }

    // updates table only if more recent date passed
    public synchronized void update(String targetId, Date date) {
        Date currentDate = skipToDateTable.get(targetId);

        if (currentDate == null || date.after(currentDate)) {
            skipToDateTable.put(targetId, date);
        }
    }

    @WorkerThread
    private void init() {

        Map<String, Date> newSkipToDateTable;

        // try to init table from database, init with empty if failed
        try {
            newSkipToDateTable = new ConcurrentHashMap<>();

            List<Contact> contacts = db.getAllContacts();

            // if empty contact list
            if (contacts == null || contacts.size() == 0) {
                return;
            }

            for (Contact contact : contacts) {
                Message lastMessage = db.getLastMessageBySenderId(contact.getId());

                if (lastMessage == null) {
                    continue;
                }

                newSkipToDateTable.put(contact.getServerId(), lastMessage.getDate());
            }

        } catch (SQLDataException ex) {
            newSkipToDateTable = new ConcurrentHashMap<>();
        }

        skipToDateTable = newSkipToDateTable;
    }
}
