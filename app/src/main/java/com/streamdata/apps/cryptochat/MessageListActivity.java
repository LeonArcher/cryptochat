package com.streamdata.apps.cryptochat;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import com.streamdata.apps.cryptochat.models.Contact;
import com.streamdata.apps.cryptochat.models.Message;


public class MessageListActivity extends AppCompatActivity {

    // TODO: get message list, self contact and target contact from database
    Contact selfContact = new Contact(Contact.selfId, "alex45", "Alex", null);
    Contact targetContact = new Contact(1, "jack_slash", "Jack", null);
    ArrayList<Message> messageList = new ArrayList<>();

    private int lastMessageId;

    private BaseAdapter adapter;
    private ListView listView;
    private EditText messageEditText;
    private SendMessageListener sendMessageListener;

    private MessagingService messagingService;
    private MessagingHandler messagingHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: load message history from database
        // TODO: load lastMessageId from settings
        lastMessageId = 0;

        // initializing messaging service and handler
        messagingService = new MessagingService();
        messagingHandler = new MessagingHandler(this);

        // init activity view
        setContentView(R.layout.activity_message_list);

        // setup listView configuration
        float scale = getResources().getDisplayMetrics().density;
        adapter = new MessageListAdapter(this, messageList, scale);
        listView = (ListView) findViewById(R.id.msgListView);
        listView.setAdapter(adapter);

        // init send message listener
        sendMessageListener = new SendMessageListener(this);

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
        messagingService.unbindListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // subscribing to incoming messages (all new ones from lastMessageId)
        messagingService.bindListener(messagingHandler, selfContact, targetContact, lastMessageId);
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
                selfContact,
                targetContact,
                text,
                new Date()
        );

        messagingService.sendMessage(messagingHandler, message);

        // update message list
        messageList.add(message);
        adapter.notifyDataSetChanged();

        // clear edit field
        messageEditText.setText("");

        // scroll down
        listView.smoothScrollToPosition(adapter.getCount() - 1);
    }

    private static class MessagingHandler extends Handler {
        // weak bound with UI thread parent activity
        private final WeakReference<MessageListActivity> parentActivityReference;

        public MessagingHandler(MessageListActivity parent) {
            parentActivityReference = new WeakReference<>(parent);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            MessageListActivity parent = parentActivityReference.get();

            if (parent == null) {
                return;
            }

            switch (msg.what) {
                case MessagingService.STATUS_NEW_MESSAGES:
                    // get message list object
                    ArrayList<Message> newMessages = (ArrayList<Message>) msg.obj;

                    // update last received message id
                    parent.lastMessageId = newMessages.get(newMessages.size() - 1).getId();

                    // update message list
                    parent.messageList.addAll(newMessages);
                    parent.adapter.notifyDataSetChanged();

                    // scroll down
                    parent.listView.smoothScrollToPosition(parent.adapter.getCount() - 1);

                    Log.d(MessagingService.MESSAGING_LOG_TAG, "Received messages pack.");
                    break;

                case MessagingService.STATUS_SEND_MESSAGE_SUCCESS:
                    // TODO: handle successfully sent message
                    Log.d(MessagingService.MESSAGING_LOG_TAG, "Message sent successfully.");
                    break;

                case MessagingService.STATUS_SEND_MESSAGE_ERROR:
                    // TODO: handle fail sent message
                    Log.d(MessagingService.MESSAGING_LOG_TAG, "Message sending error.");
                    break;
            }
        }
    }
}
