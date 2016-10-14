package com.streamdata.apps.cryptochat.messaging;

import com.streamdata.apps.cryptochat.models.Message;
import com.streamdata.apps.cryptochat.scheduling.Task;
import com.streamdata.apps.cryptochat.utils.ReceiverTargetKey;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * Task for filtering messages:
 * messages with sender serverId different from targetId get removed,
 * messages with receiver serverId different from receiverId get removed
 *
 * The last id of proper message is saved to skipToIdTable
 */
public class TargetFilterMessagesTask implements Task<ArrayList<Message>> {

    private final Task<ArrayList<Message>> messagesProviderTask;

    private final String receiverId;
    private final String targetId;

    private Map<ReceiverTargetKey, Integer> skipToIdTable;

    public TargetFilterMessagesTask(
            Task<ArrayList<Message>> messagesProviderTask,
            String receiverId,
            String targetId,
            Map<ReceiverTargetKey, Integer> skipToIdTable
    ) {
        this.messagesProviderTask = messagesProviderTask;
        this.receiverId = receiverId;
        this.targetId = targetId;
        this.skipToIdTable = skipToIdTable;
    }

    @Override
    public ArrayList<Message> run() throws Exception {
        ArrayList<Message> messages = messagesProviderTask.run();
        ArrayList<Message> targetMessages = new ArrayList<>();

        // select messages only with senderId == targetId and matching receiverId
        for (Message message : messages) {
            if (
                    Objects.equals(message.getSender().getServerId(), targetId) &&
                    Objects.equals(message.getReceiver().getServerId(), receiverId)
                ) {
                targetMessages.add(message);
            }
        }

        // save the last id of target messages to skip table
        int sz = targetMessages.size();
        if (sz > 0) {
            ReceiverTargetKey key = new ReceiverTargetKey(receiverId, targetId);
            skipToIdTable.put(key, targetMessages.get(sz - 1).getId());
        }

        return targetMessages;
    }
}
