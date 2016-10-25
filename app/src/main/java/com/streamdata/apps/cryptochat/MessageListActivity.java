package com.streamdata.apps.cryptochat;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.streamdata.apps.cryptochat.database.DBController;
import com.streamdata.apps.cryptochat.database.DBHandler;
import com.streamdata.apps.cryptochat.messaging.MessageController;
import com.streamdata.apps.cryptochat.messaging.PeriodicMessageRetrieverService;
import com.streamdata.apps.cryptochat.models.Contact;
import com.streamdata.apps.cryptochat.models.Message;
import com.streamdata.apps.cryptochat.network.NetworkDataLayer;
import com.streamdata.apps.cryptochat.network.NetworkObjectLayer;
import com.streamdata.apps.cryptochat.scheduling.Callback;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class MessageListActivity extends AppCompatActivity {

    // TODO: get target contact from ContactListActivity
    Contact selfContact = DBHandler.getInstance().getOwnerContact();
    Contact targetContact = DBHandler.getInstance().getContact(1);
    List<Message> messageList = new ArrayList<>();

    private BaseAdapter adapter;
    private ListView listView;
    private EditText messageEditText;

    private MessageController messageController;
    private PeriodicMessageRetrieverService messageRetrieverService;

    private ReceiveMessagesCallback receiveCallback;
    private SendMessagesCallback sendCallback;

    private DBController dbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize callbacks
        receiveCallback = new ReceiveMessagesCallback(this);
        sendCallback = new SendMessagesCallback(this);

        // initializing UI handler, network, message controller, periodic message retriever service
        Handler uiHandler = new Handler();
        NetworkObjectLayer networkObjectLayer = new NetworkObjectLayer(new NetworkDataLayer());
        messageController = new MessageController(uiHandler, networkObjectLayer);
        messageRetrieverService = new PeriodicMessageRetrieverService(messageController);

        // initializing DB controller
        dbController = new DBController(uiHandler);

        // request history load from DB in background
        dbController.loadMessages(targetContact.getId(), new DbLoadMessagesCallback(this));

        // init activity view
        setContentView(R.layout.activity_message_list);

        // setup listView configuration
        float scale = getResources().getDisplayMetrics().density;
        adapter = new MessageListAdapter(this, messageList, scale);
        listView = (ListView) findViewById(R.id.msgListView);
        listView.setAdapter(adapter);

        // init send message listener
        SendMessageListener sendMessageListener = new SendMessageListener(this);

        // setup sendMessageButton configuration
        Button sendMessageButton = (Button) findViewById(R.id.sendMessageButton);
        sendMessageButton.setOnClickListener(sendMessageListener);

        // map message edit text
        messageEditText = (EditText) findViewById(R.id.messageEditText);
        messageEditText.setImeActionLabel("Send", KeyEvent.KEYCODE_ENTER);
        messageEditText.setOnEditorActionListener(sendMessageListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        messageRetrieverService.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // subscribing to incoming messages
        messageRetrieverService.start(
                receiveCallback,
                selfContact.getServerId(),
                targetContact.getServerId()
        );
    }

    // handle send message event
    void sendMessage() {
        String text = messageEditText.getText().toString();

        // ignore empty messages
        if (TextUtils.isEmpty(text)) {
            return;
        }

        // prepare message
        Message message = new Message(
                Message.EMPTY_ID,
                selfContact.getId(),
                targetContact.getId(),
                new Date(),
                text
        );

        messageController.sendMessage(message, sendCallback);

        // update message list
        messageList.add(message);
        adapter.notifyDataSetChanged();

        // clear edit field
        messageEditText.setText("");

        // scroll down
        listView.smoothScrollToPosition(adapter.getCount() - 1);

        Log.d(MessageController.MESSAGING_LOG_TAG, "Sent message.");
    }

    private static class ReceiveMessagesCallback implements Callback<ArrayList<Message>> {

        // weak bound with UI thread parent activity
        private final WeakReference<MessageListActivity> parentActivityReference;

        public ReceiveMessagesCallback(MessageListActivity parent) {
            parentActivityReference = new WeakReference<>(parent);
        }

        @Override
        public void onSuccess(ArrayList<Message> result) {
            MessageListActivity parent = parentActivityReference.get();

            if (parent == null) {
                return;
            }

            // return if no new messages
            if (result.isEmpty()) {
                return;
            }

            // update message list
            parent.messageList.addAll(result);
            parent.adapter.notifyDataSetChanged();

            // scroll down
            parent.listView.smoothScrollToPosition(parent.adapter.getCount() - 1);

            // save messages to database
            parent.dbController.saveMessages(result, new DbSaveMessagesCallback());

            Log.d(MessageController.MESSAGING_LOG_TAG, "Received messages pack.");
        }

        @Override
        public void onError(Exception ex) {
            // TODO: handle error
            throw new RuntimeException("Error receiving messages.", ex);
        }
    }

    private static class SendMessagesCallback implements Callback<Message> {

        // weak bound with UI thread parent activity
        private final WeakReference<MessageListActivity> parentActivityReference;

        public SendMessagesCallback(MessageListActivity parent) {
            parentActivityReference = new WeakReference<>(parent);
        }

        @Override
        public void onSuccess(Message result) {
            // TODO: handle successfully sent message
            MessageListActivity parent = parentActivityReference.get();

            if (parent == null) {
                return;
            }

            // save successfully sent message to database
            parent.dbController.saveMessages(
                    Collections.singletonList(result),
                    new DbSaveMessagesCallback()
            );
        }

        @Override
        public void onError(Exception ex) {
            // TODO: handle error
            throw new RuntimeException("Error sending message.", ex);
        }
    }

    private static class DbLoadMessagesCallback implements Callback<List<Message>> {

        // weak bound with UI thread parent activity
        private final WeakReference<MessageListActivity> parentActivityReference;

        public DbLoadMessagesCallback(MessageListActivity parent) {
            parentActivityReference = new WeakReference<>(parent);
        }

        @Override
        public void onSuccess(List<Message> result) {
            MessageListActivity parent = parentActivityReference.get();

            if (parent == null) {
                return;
            }

            // update message list
            parent.messageList.addAll(result);
            parent.adapter.notifyDataSetChanged();

            // scroll down
            parent.listView.smoothScrollToPosition(parent.adapter.getCount() - 1);

            Log.d(DBController.DB_CONTROLLER_LOG_TAG, "Loaded messages.");
        }

        @Override
        public void onError(Exception ex) {
            // TODO: handle error
        }
    }

    private static class DbSaveMessagesCallback implements Callback<List<Message>> {

        public DbSaveMessagesCallback() {
        }

        @Override
        public void onSuccess(List<Message> result) {
            // TODO: handle successful saving
        }

        @Override
        public void onError(Exception ex) {
            // TODO: handle error
        }
    }
}
