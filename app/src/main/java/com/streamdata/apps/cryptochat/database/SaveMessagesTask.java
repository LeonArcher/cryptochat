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

    public SaveMessagesTask(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public List<Message> run() {

        DBHandler db = DBHandler.getInstance();

        for (Message message : messages) {
            db.addMessage(message);
        }

        return messages;
    }
}
