package com.streamdata.apps.cryptochat.database;

import com.streamdata.apps.cryptochat.models.Message;
import com.streamdata.apps.cryptochat.scheduling.Task;

import java.util.List;

/**
 * Task returns all incoming/outgoing messages for target contact from db
 */
public class GetTalkMessagesTask implements Task<List<Message>> {

    private final int targetContactId;

    public GetTalkMessagesTask(int targetContactId) {
        this.targetContactId = targetContactId;
    }

    @Override
    public List<Message> run() throws MessagesNotFoundException {

        List<Message> messages = DBHandler.getInstance().getAllMessagesOfTalk(targetContactId);

        if (messages == null) {
            throw new MessagesNotFoundException(targetContactId);
        }

        return messages;
    }
}
