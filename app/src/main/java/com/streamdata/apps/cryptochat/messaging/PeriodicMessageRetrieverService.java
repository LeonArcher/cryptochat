package com.streamdata.apps.cryptochat.messaging;

import com.streamdata.apps.cryptochat.models.Message;
import com.streamdata.apps.cryptochat.scheduling.Callback;

import java.util.ArrayList;

/**
 * Class for periodic launch of receive messages task through message controller
 */
public class PeriodicMessageRetrieverService {

    private static final int RETRIEVE_RATE_MS = 5000;

    private final MessageController messageController;

    private RetrieveInitiatorThread initiator = null;

    public PeriodicMessageRetrieverService(MessageController messageController) {
        this.messageController = messageController;
    }

    public void start(Callback<ArrayList<Message>> retrieveCallback,
                      String receiverId, String targetId) {

        if (initiator != null) {
            initiator.finish();
        }

        initiator = new RetrieveInitiatorThread(
                receiverId,
                targetId,
                messageController,
                retrieveCallback
        );

        initiator.start();
    }

    public void stop() {

        if (initiator == null) {
            return;
        }

        initiator.finish();
        initiator = null;
    }

    private static class RetrieveInitiatorThread extends Thread {

        private volatile boolean mFinish = false;

        private final MessageController messageController;
        private final String receiverId;
        private final String targetId;
        private final Callback<ArrayList<Message>> getNewMessagesCallback;

        public RetrieveInitiatorThread(String receiverId, String targetId,
                                       MessageController messageController,
                                       Callback<ArrayList<Message>> getNewMessagesCallback) {

            this.messageController = messageController;
            this.receiverId = receiverId;
            this.targetId = targetId;
            this.getNewMessagesCallback = getNewMessagesCallback;
        }

        @Override
        public void run() {
            do {
                if (!mFinish) {
                    messageController.getNewMessages(receiverId, targetId, getNewMessagesCallback);
                } else {
                    return;
                }

                try {
                    Thread.sleep(RETRIEVE_RATE_MS);

                } catch (InterruptedException e) {
                    // TODO: handle exception?
                }

            } while (true);
        }

        public void finish() {
            mFinish = true;
        }
    }
}
