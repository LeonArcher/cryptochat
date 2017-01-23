package com.streamdata.apps.cryptochat.database;

import com.streamdata.apps.cryptochat.models.Message;
import com.streamdata.apps.cryptochat.scheduling.Task;

import java.sql.SQLDataException;
import java.util.List;

/**
 * Task returns all incoming/outgoing messages for target contact from db
 */
public class GetTalkMessagesTask implements Task<List<Message>> {

    private final DBHandler db;
    private final int targetContactId;

    public GetTalkMessagesTask(int targetContactId, DBHandler db) {
        this.targetContactId = targetContactId;
        this.db = db;
    }

    @Override
    public List<Message> run() throws MessagesNotFoundException, SQLDataException {

        List<Message> messages = db.getAllMessagesOfTalk(targetContactId);

        if (messages == null) {
            throw new MessagesNotFoundException(targetContactId);
        }

        return messages;
    }
}
