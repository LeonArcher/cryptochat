package com.streamdata.apps.cryptochat.messaging;

import com.streamdata.apps.cryptochat.models.Message;
import com.streamdata.apps.cryptochat.scheduling.Callback;
import com.streamdata.apps.cryptochat.scheduling.TaskRunner;

import java.util.ArrayList;
import java.util.concurrent.Executors;

/**
 * TODO: Add a class header comment!
 */
public class MessageController {
    public static final String MESSAGING_LOG_TAG = "Messaging";

    private final TaskRunner<ArrayList<Message>, Exception> taskRunner;

    //TODO: table for (receiverId, targetId) -> skipToId

    public MessageController() {
        taskRunner = new TaskRunner<>(Executors.newSingleThreadExecutor());
    }

    public void getNewMessages(String receiverId, String targetId,
                               Callback<ArrayList<Message>, Exception> getNewMessagesCallback) {
        //
    }

    public void sendMessage(Message message, Callback<Message, Exception> sendMessageCallback) {
        //
    }
}
