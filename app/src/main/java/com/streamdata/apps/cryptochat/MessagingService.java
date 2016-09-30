package com.streamdata.apps.cryptochat;

import android.os.Handler;
import android.util.Log;

import com.streamdata.apps.cryptochat.models.Contact;
import com.streamdata.apps.cryptochat.models.Message;
import com.streamdata.apps.cryptochat.utils.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Leon Archer on 29.09.2016.
 */
public class MessagingService {

    public static final int STATUS_NEW_MESSAGES = 0;
    public static final int STATUS_SEND_MESSAGE_SUCCESS = 1;
    public static final int STATUS_SEND_MESSAGE_ERROR = 2;

    public static final String MESSAGING_LOG_TAG = "Messaging";
    public static final String MESSAGING_JSON_LOG_TAG = "MessagingJSON";

    public static final String WEB_SERVICE_URL = "http://crypto-chat.azurewebsites.net/";
    public static final int MESSAGES_POLLING_RATE_SECONDS = 5;

    ExecutorService sendExecutor = Executors.newSingleThreadExecutor();
    ScheduledExecutorService receiveExecutor = Executors.newSingleThreadScheduledExecutor();

    private static class MessageListenerJob implements Runnable {

        private final Handler handler;

        private final Contact selfContact; // filter the receiver of messages
        private final Contact targetContact; // filter the sender of messages

        private int lastMessageId; // messages with smaller id will not be downloaded (will increase during work)

        private final String requestUrl;

        android.os.Message msg; // system message for callback

        public MessageListenerJob(Handler handler, Contact selfContact, Contact targetContact,
                                  int lastMessageId) {
            this.handler = handler;
            this.lastMessageId = lastMessageId;
            this.selfContact = selfContact;
            this.targetContact = targetContact;

            requestUrl = String.format(
                    "%s/api/packages/%s",
                    MessagingService.WEB_SERVICE_URL,
                    selfContact.getServerId()
            );
        }

        @Override
        public void run() {
            Log.d(MESSAGING_LOG_TAG, String.format("Message poll. Last id: %d", lastMessageId));

            // download array with all messages for self contact
            String jData = Network.getJSON(requestUrl, Network.DEFAULT_TIMEOUT);

            // save all incoming messages to list and handle later
            ArrayList<Message> messageList = new ArrayList<>();

            // parse JSON array and simultaneously handle messages
            try {
                JSONArray jArray = new JSONArray(jData);

                for (int i = 0; i < jArray.length(); ++i) {

                    JSONObject jObject = jArray.getJSONObject(i);

                    // read all fields from package
                    final int id = jObject.getInt("id");
                    final String senderId = jObject.getString("sender_id");
                    final String receiverId = jObject.getString("receiver_id");
                    final String data = jObject.getString("data");
                    final String dateString = jObject.getString("sent_time");

                    Date sentTime = null;
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "EE, dd MMM yyyy HH:mm:ss z",
                            Locale.US
                    );
                    try {
                        sentTime = dateFormat.parse(dateString);
                    } catch (ParseException ex) {
                        Log.d(MESSAGING_LOG_TAG, null, ex);
                    }

                    // the receiver should always be self contact
                    if (!Objects.equals(receiverId, selfContact.getServerId())) {
                        Log.e(MESSAGING_LOG_TAG, null, new AssertionError());
                        continue;
                    }

                    // only new messages from target contact should be processed
                    if ((id <= lastMessageId) ||
                            (!Objects.equals(senderId, targetContact.getServerId()))) {
                        continue;
                    }

                    // TODO: decrypt the message text
                    final String text = data;

                    // create and handle message (send to main thread via handler)
                    Message message = new Message(id, targetContact, selfContact, text, sentTime);
                    messageList.add(message);

                    // TODO: send message to database

                    Log.d(MESSAGING_LOG_TAG, message.getText());
                }
            } catch (JSONException ex) {
                Log.e(MESSAGING_JSON_LOG_TAG, null, ex);
            }

            // handle messages if new ones received
            if (messageList.size() > 0) {
                lastMessageId = messageList.get(messageList.size() - 1).getId();
                handleMessages(messageList);
            }
        }

        private void handleMessages(ArrayList<Message> messages) {
            msg = handler.obtainMessage(STATUS_NEW_MESSAGES, messages);
            handler.sendMessage(msg);
        }
    }

    private static class MessageSenderJob implements Runnable {

        private final Handler handler;
        private final Message message; // message to send (contains receiver)

        public MessageSenderJob(Handler handler, Message message) {
            this.handler = handler;
            this.message = message;
        }

        @Override
        public void run() {
            // TODO: send message to database

            // form the request string
            final String requestUrl = String.format(
                "%s/api/packages/",
                MessagingService.WEB_SERVICE_URL
            );

            // TODO: encrypt the text
            final String data = message.getText();

            // create JSON object from message model
            JSONObject jMessage = new JSONObject();
            try {
                jMessage.put("sender_id", message.getSender().getServerId());
                jMessage.put("receiver_id", message.getReceiver().getServerId());
                jMessage.put("data", data);

            } catch (JSONException ex) {
                Log.e(MESSAGING_JSON_LOG_TAG, null, ex);
                handler.sendEmptyMessage(STATUS_SEND_MESSAGE_ERROR);
                return;
            }

            // post json package
            final String jString = jMessage.toString();
            String result = Network.postJSON(requestUrl, Network.DEFAULT_TIMEOUT, jString);

            // handle success and error results
            if (result == null) {
                handler.sendEmptyMessage(STATUS_SEND_MESSAGE_ERROR);
            } else {
                handler.sendEmptyMessage(STATUS_SEND_MESSAGE_SUCCESS);
            }
        }
    }

    public void bindListener(Handler handler, Contact selfContact, Contact targetContact,
                             int lastMessageId) {
        Log.d("debug", "Listener binding procedure initiated.");
        receiveExecutor.scheduleAtFixedRate(
                new MessageListenerJob(handler, selfContact, targetContact, lastMessageId),
                0,
                MESSAGES_POLLING_RATE_SECONDS,
                TimeUnit.SECONDS
        );
    }

    public void sendMessage(Handler handler, Message message) {
        sendExecutor.execute(new MessageSenderJob(handler, message));
    }
}
