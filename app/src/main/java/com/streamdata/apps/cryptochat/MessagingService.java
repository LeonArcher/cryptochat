package com.streamdata.apps.cryptochat;

import android.os.Handler;
import android.util.Log;

import com.streamdata.apps.cryptochat.models.Contact;
import com.streamdata.apps.cryptochat.models.Message;
import com.streamdata.apps.cryptochat.models.RMessage;
import com.streamdata.apps.cryptochat.network.NetworkDataLayer;
import com.streamdata.apps.cryptochat.network.NetworkObjectLayer;
import com.streamdata.apps.cryptochat.utils.MessageAdapter;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MessagingService {

    public static final int STATUS_NEW_MESSAGES = 0;
    public static final int STATUS_SEND_MESSAGE_SUCCESS = 1;
    public static final int STATUS_SEND_MESSAGE_ERROR = 2;

    public static final String MESSAGING_LOG_TAG = "Messaging";
    public static final String MESSAGING_JSON_LOG_TAG = "MessagingJSON";

    public static final int MESSAGES_POLLING_RATE_SECONDS = 5;

    private ExecutorService sendExecutor = Executors.newSingleThreadExecutor();
    private ScheduledExecutorService receiveExecutor = null;

    // network-level layers
    private final NetworkDataLayer networkDataLayer;
    private final NetworkObjectLayer network;

    public MessagingService() {

        // init network
        networkDataLayer = new NetworkDataLayer();
        network = new NetworkObjectLayer(networkDataLayer);
    }

    public void bindListener(Handler handler, Contact selfContact, Contact targetContact,
                             int lastMessageId) {
        // unbind listener first before creating a new one
        if (receiveExecutor != null) {
            unbindListener();
        }

        Log.d(MESSAGING_LOG_TAG, "Listener binding procedure initiated.");
        receiveExecutor = Executors.newSingleThreadScheduledExecutor();
        receiveExecutor.scheduleAtFixedRate(
                new MessageListenerJob(handler, selfContact, targetContact, lastMessageId, network),
                0,
                MESSAGES_POLLING_RATE_SECONDS,
                TimeUnit.SECONDS
        );
    }

    public void unbindListener() {
        Log.d(MESSAGING_LOG_TAG, "Listener service is shutting down.");
        receiveExecutor.shutdown();
        receiveExecutor = null;
    }

    public void sendMessage(Handler handler, Message message) {
        sendExecutor.execute(new MessageSenderJob(handler, message, network));
    }

    private static class MessageListenerJob implements Runnable {

        private final Handler handler;

        private final Contact selfContact; // filter the receiver of messages
        private final Contact targetContact; // filter the sender of messages

        private int lastMessageId; // messages with smaller id will not be downloaded (will increase during work)

        private final NetworkObjectLayer network;

        android.os.Message msg; // system message for callback

        public MessageListenerJob(Handler handler, Contact selfContact, Contact targetContact,
                                  int lastMessageId, NetworkObjectLayer network) {
            this.handler = handler;
            this.lastMessageId = lastMessageId;
            this.selfContact = selfContact;
            this.targetContact = targetContact;
            this.network = network;
        }

        @Override
        public void run() {
            Log.d(MESSAGING_LOG_TAG, String.format("Message poll. Last id: %d", lastMessageId));

            ArrayList<Message> messages = new ArrayList<>();

            // get all messages
            ArrayList<RMessage> rMessages;
            try {
                rMessages = network.getMessages(selfContact.getServerId());

            } catch (IOException | JSONException ex) {
                throw new RuntimeException(ex);
            }

            for (RMessage rMessage : rMessages) {

                // the receiver should always be self contact
                if (!Objects.equals(rMessage.getReceiverId(), selfContact.getServerId())) {
                    Log.e(MESSAGING_LOG_TAG, null, new AssertionError());
                    continue;
                }

                // only new messages from target contact should be processed
                if ((rMessage.getId() <= lastMessageId) ||
                        (!Objects.equals(rMessage.getSenderId(), targetContact.getServerId()))) {
                    continue;
                }

                // TODO: decrypt the message text

                // convert RMessage to Message, on error continue to next message
                Message newMessage;
                try {
                    newMessage = MessageAdapter.toMessage(rMessage);

                } catch (ParseException ex) {
                    Log.e(MESSAGING_LOG_TAG, null, ex);
                    continue;
                }

                messages.add(newMessage);
                // TODO: send message to database

                Log.d(MESSAGING_LOG_TAG, newMessage.getText());
            }

            // handle messages if new ones received
            if (messages.size() > 0) {
                lastMessageId = messages.get(messages.size() - 1).getId();
                handleMessages(messages);
            }
        }

        private void handleMessages(ArrayList<Message> messages) {
            msg = handler.obtainMessage(STATUS_NEW_MESSAGES, messages);
            handler.sendMessage(msg);
        }
    }

    private static class MessageSenderJob implements Runnable {

        private final Handler handler;
        private final NetworkObjectLayer network;
        private final Message message; // message to send (contains receiver)

        public MessageSenderJob(Handler handler, Message message, NetworkObjectLayer network) {
            this.handler = handler;
            this.message = message;
            this.network = network;
        }

        @Override
        public void run() {
            // TODO: send message to database
            RMessage rMessage = MessageAdapter.toRMessage(message);
            // TODO: encrypt the text

            // try to send message, on error send error callback
            try {
                network.postMessage(rMessage);

            } catch (IOException ex) {
                Log.e(MESSAGING_JSON_LOG_TAG, null, ex);
                handler.sendEmptyMessage(STATUS_SEND_MESSAGE_ERROR);
                return;
            }

            // send success callback
            handler.sendEmptyMessage(STATUS_SEND_MESSAGE_SUCCESS);
        }
    }
}
