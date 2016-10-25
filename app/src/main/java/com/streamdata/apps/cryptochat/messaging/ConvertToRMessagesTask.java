package com.streamdata.apps.cryptochat.messaging;

import com.streamdata.apps.cryptochat.models.Message;
import com.streamdata.apps.cryptochat.models.RMessage;
import com.streamdata.apps.cryptochat.scheduling.Task;
import com.streamdata.apps.cryptochat.utils.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Add a class header comment!
 */
public class ConvertToRMessagesTask implements Task<List<Message>> {

    private final Task<List<RMessage>> rMessagesProviderTask;

    public ConvertToRMessagesTask(Task<List<RMessage>> rMessagesProviderTask) {
        this.rMessagesProviderTask = rMessagesProviderTask;
    }

    @Override
    public List<Message> run() throws Exception {

        List<RMessage> rMessages = rMessagesProviderTask.run();

        List<Message> convertedMessages = new ArrayList<>();

        for (RMessage rMessage : rMessages) {
            Message newMessage = MessageAdapter.toMessage(rMessage);

            convertedMessages.add(newMessage);
        }

        return convertedMessages;
    }
}
