package com.streamdata.apps.cryptochat.messaging;

import android.os.Handler;
import android.support.annotation.MainThread;

import com.streamdata.apps.cryptochat.database.DBHandler;
import com.streamdata.apps.cryptochat.models.Message;
import com.streamdata.apps.cryptochat.network.NetworkObjectLayer;
import com.streamdata.apps.cryptochat.scheduling.Callback;
import com.streamdata.apps.cryptochat.scheduling.TaskRunner;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Class for receive/send messages
 */
@MainThread
public class MessageController {
    public static final String MESSAGING_LOG_TAG = "Messaging";

    private final TaskRunner<List<Message>> receiveTaskRunner;
    private final TaskRunner<Message> sendTaskRunner;

    private final NetworkObjectLayer network;
    private final Handler uiHandler;

    private final DBHandler db;

    // provide date to skip incoming messages up to
    LastMessageDateProvider lastMessageDateProvider;

    public MessageController(Handler uiHandler, NetworkObjectLayer network, DBHandler db) {
        receiveTaskRunner = new TaskRunner<>(Executors.newSingleThreadExecutor());
        sendTaskRunner = new TaskRunner<>(Executors.newSingleThreadExecutor());

        // init helper layers
        this.network = network;
        this.uiHandler = uiHandler;
        this.db = db;
        this.lastMessageDateProvider = new LastMessageDateProvider(db);
    }

    public synchronized void getNewMessages(final String receiverId, final String targetId,
                                            final Callback<List<Message>> getNewMessagesCallback) {

        // TODO: insert step for message decryption

        // create message receiving, filtering and converting task
        ReceiveMessagesTask task = new ReceiveMessagesTask(
                network,
                receiverId,
                targetId,
                lastMessageDateProvider,
                db
        );

        // add intermediate callback layer for skipToDateTable update
        Callback<List<Message>> callback = new Callback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> result) {
                int sz = result.size();
                if (sz > 0) {
                    lastMessageDateProvider.update(targetId, result.get(sz - 1).getDate());
                }

                getNewMessagesCallback.onSuccess(result);
            }

            @Override
            public void onError(Exception ex) {
                getNewMessagesCallback.onError(ex);
            }
        };

        receiveTaskRunner.runTask(task, callback, uiHandler);
    }

    public void sendMessage(Message message, Callback<Message> sendMessageCallback) {

        sendTaskRunner.runTask(
                new SendMessageTask(message, network, db),
                sendMessageCallback,
                uiHandler
        );
    }
}
