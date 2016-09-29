package com.streamdata.apps.cryptochat;

import android.os.Handler;
import android.util.Log;

import com.streamdata.apps.cryptochat.models.Contact;
import com.streamdata.apps.cryptochat.models.Message;
import com.streamdata.apps.cryptochat.utils.DateUtils;
import com.streamdata.apps.cryptochat.utils.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Leon Archer on 29.09.2016.
 */
public class MessagingService {

    public static final int STATUS_NEW_MESSAGE = 0;
    public static final int STATUS_SEND_MESSAGE_SUCCESS = 1;
    public static final int STATUS_SEND_MESSAGE_ERROR = 2;

    public static final String MESSAGING_LOG_TAG = "Messaging";

    public static final String WEB_SERVICE_URL = "http://crypto-chat.azurewebsites.net/";
    public static final int MESSAGES_POLLING_RATE_SECONDS = 5;

    ExecutorService sendExecutor = Executors.newSingleThreadExecutor();
    ScheduledExecutorService receiveExecutor = Executors.newSingleThreadScheduledExecutor();

    private static class MessageListenerJob implements Runnable {

        private final Handler handler;

        private final String contactKey; // string key to access incoming messages for self contact
        private int lastMessageId; // messages with smaller id will not be downloaded (will increase during work)

        private final String requestUrl;

        android.os.Message msg; // system message for callback

        private final Contact selfContact;

        public MessageListenerJob(Handler handler, String contactKey, int lastMessageId) {
            this.handler = handler;
            this.lastMessageId = lastMessageId;
            this.contactKey = contactKey;

            // TODO: read self contact from the database
            selfContact = new Contact(Contact.selfId, contactKey, "Me", null);

            requestUrl = String.format(
                    "%s/api/packages/%s",
                    MessagingService.WEB_SERVICE_URL,
                    contactKey
            );
        }

        @Override
        public void run() {
            Log.d(MESSAGING_LOG_TAG, String.format("Message poll. Last id: %d", lastMessageId));
            
            // download array with all messages for self contact
            String jData = Network.getJSON(requestUrl, Network.DEFAULT_TIMEOUT);

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
                    final Date sentTime = DateUtils.stringToDate(jObject.getString("sent_time"));

                    // the receiver should always be self contact
                    if (!Objects.equals(receiverId, contactKey)) throw new AssertionError();

                    // only new messages should be processed
                    if (id <= lastMessageId) {
                        continue;
                    }

                    // TODO: read contact from database based on serverId (or handle missing contact)
                    Contact contact = new Contact(1, senderId, "John Doe", null);

                    // TODO: decrypt the message text
                    final String text = data;

                    // create and handle message (send to main thread via handler)
                    Message message = new Message(id, contact, selfContact, text, sentTime);
                    handleMessage(message);

                    // TODO: send message to database

                    lastMessageId = id;
                }
            } catch (JSONException ex) {
                Log.e("JSONException", null, ex);
            }
        }

        private void handleMessage(Message message) {
            msg = handler.obtainMessage(STATUS_NEW_MESSAGE, message);
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
            // TODO: implement sending procedure
        }
    }

    public void bindListener(Handler handler, String contactKey, int lastMessageId) {
        Log.d("debug", "Listener binding procedure initiated.");
        receiveExecutor.scheduleAtFixedRate(
                new MessageListenerJob(handler, contactKey, lastMessageId),
                0,
                MESSAGES_POLLING_RATE_SECONDS,
                TimeUnit.SECONDS
        );
    }

    public void sendMessage(Handler handler, Message message) {
        sendExecutor.execute(new MessageSenderJob(handler, message));
    }
}
