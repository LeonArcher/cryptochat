package com.streamdata.apps.cryptochat.messaging;

import com.streamdata.apps.cryptochat.models.RMessage;
import com.streamdata.apps.cryptochat.scheduling.Task;
import com.streamdata.apps.cryptochat.utils.ReceiverTargetKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Task for filtering messages:
 * messages with sender serverId different from targetId get removed,
 * messages with receiver serverId different from receiverId get removed
 *
 * The last id of proper message is saved to skipToIdTable
 */
public class TargetFilterMessagesTask implements Task<List<RMessage>> {

    private final Task<List<RMessage>> messagesProviderTask;

    private final String receiverId;
    private final String targetId;

    private Map<ReceiverTargetKey, Integer> skipToIdTable;

    public TargetFilterMessagesTask(
            Task<List<RMessage>> messagesProviderTask,
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
    public List<RMessage> run() throws Exception {

        // get messages via running the child task
        List<RMessage> messages = messagesProviderTask.run();

        List<RMessage> targetMessages = new ArrayList<>();

        // select messages only with senderId == targetId and matching receiverId
        for (RMessage message : messages) {
            if (
                    Objects.equals(message.getSenderId(), targetId) &&
                    Objects.equals(message.getReceiverId(), receiverId)
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
