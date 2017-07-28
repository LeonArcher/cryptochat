package com.streamdata.apps.cryptochat.database;

import com.streamdata.apps.cryptochat.models.Message;
import com.streamdata.apps.cryptochat.scheduling.Task;

import java.util.List;

/**
 * Task saves message list to the database
 * The same list of messages returned without modifications
 */
public class SaveMessagesTask implements Task<List<Message>> {

    private final List<Message> messages;
    private final DBHandler db;

    public SaveMessagesTask(List<Message> messages, DBHandler db) {
        this.messages = messages;
        this.db = db;
    }

    @Override
    public List<Message> run() {

        for (Message message : messages) {
            db.addMessage(message);
        }

        return messages;
    }
}
