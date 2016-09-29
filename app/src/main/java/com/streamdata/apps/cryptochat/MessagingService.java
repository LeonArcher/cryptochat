package com.streamdata.apps.cryptochat;

import android.os.Handler;
import android.util.Log;

import com.streamdata.apps.cryptochat.models.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Leon Archer on 29.09.2016.
 */
public class MessagingService {

    final int STATUS_NEW_MESSAGE = 0;
    final int STATUS_SEND_MESSAGE_SUCCESS = 1;
    final int STATUS_SEND_MESSAGE_ERROR = 2;

    ExecutorService sendExecutor = Executors.newSingleThreadExecutor();
    ExecutorService receiveExecutor = Executors.newSingleThreadExecutor();

    private static class MessageListenerJob implements Runnable {

        private final Handler handler;
        private final String contactKey;
        private int lastMessageId;

        android.os.Message msg;

        public MessageListenerJob(Handler handler, String contactKey, int lastMessageId) {
            this.handler = handler;
            this.lastMessageId = lastMessageId;
            this.contactKey = contactKey;
        }

        @Override
        public void run() {
            //
        }

        private void obtainMessage(Message message) {
            msg = handler.obtainMessage(STATUS_NEW_MESSAGE, message);
            handler.sendMessage(msg);
        }
    }

    private static class MessageSenderJob implements Runnable {

        private final Handler handler;
        private final Message message;

        public MessageSenderJob(Handler handler, Message message) {
            this.handler = handler;
            this.message = message;
        }

        @Override
        public void run() {
            // TODO: implement sending procedure
        }
    }

    public void bindListener(Handler handler, String contactKey, int lastMessageId) {
        Log.d("debug", "Listener binding procedure initiated.");
        receiveExecutor.execute(new MessageListenerJob(handler, contactKey, lastMessageId));
    }

    public void sendMessage(Handler handler, Message message) {
        sendExecutor.execute(new MessageSenderJob(handler, message));
    }
}
