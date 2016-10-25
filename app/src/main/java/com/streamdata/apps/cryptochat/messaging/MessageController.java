package com.streamdata.apps.cryptochat.messaging;

import android.os.Handler;

import com.streamdata.apps.cryptochat.models.Message;
import com.streamdata.apps.cryptochat.network.NetworkObjectLayer;
import com.streamdata.apps.cryptochat.scheduling.Callback;
import com.streamdata.apps.cryptochat.scheduling.Task;
import com.streamdata.apps.cryptochat.scheduling.TaskRunner;
import com.streamdata.apps.cryptochat.utils.ReceiverTargetKey;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 * Class for receive/send messages
 */
public class MessageController {
    public static final String MESSAGING_LOG_TAG = "Messaging";

    private final TaskRunner<List<Message>> receiveTaskRunner;
    private final TaskRunner<Message> sendTaskRunner;

    private final NetworkObjectLayer network;
    private final Handler uiHandler;

    // skip messages id's storage to prevent same messages downloading repeatedly
    Map<ReceiverTargetKey, Integer> skipToIdTable;

    public MessageController(Handler uiHandler, NetworkObjectLayer network) {
        receiveTaskRunner = new TaskRunner<>(Executors.newSingleThreadExecutor());
        sendTaskRunner = new TaskRunner<>(Executors.newSingleThreadExecutor());

        // init network object layer
        this.network = network;

        this.uiHandler = uiHandler;

        // TODO: load skipTOIdTable from settings file
        skipToIdTable = new ConcurrentHashMap<>();
    }

    public void getNewMessages(String receiverId, String targetId,
                               Callback<List<Message>> getNewMessagesCallback) {

        ReceiverTargetKey key = new ReceiverTargetKey(receiverId, targetId);
        Integer val = skipToIdTable.get(key);
        int skipToId = (val != null) ? val : 0;

        // TODO: insert step for message decryption

        // create message receiving task and subsequent filtering and converting tasks
        Task<List<Message>> task = new ConvertToRMessagesTask(
                new TargetFilterMessagesTask(
                        new ReceiveMessagesTask(network, receiverId, skipToId),
                        receiverId,
                        targetId,
                        skipToIdTable
                )
        );

        receiveTaskRunner.runTask(task, getNewMessagesCallback, uiHandler);
    }

    public void sendMessage(Message message, Callback<Message> sendMessageCallback) {

        sendTaskRunner.runTask(
                new SendMessageTask(message, network),
                sendMessageCallback,
                uiHandler
        );
    }
}
