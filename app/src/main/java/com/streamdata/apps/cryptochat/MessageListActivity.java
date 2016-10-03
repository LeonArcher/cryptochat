package com.streamdata.apps.cryptochat;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import com.streamdata.apps.cryptochat.models.Contact;
import com.streamdata.apps.cryptochat.models.Message;
import com.streamdata.apps.cryptochat.utils.DateUtils;


public class MessageListActivity extends AppCompatActivity
        implements View.OnClickListener, TextView.OnEditorActionListener {

    // TODO: get message list, self contact and target contact from database
    Contact selfContact = new Contact(0, "alex45", "Alex", null, "0");
    Contact ownerContact = new Contact(0, "alex45", "Alex", null, "0");
    Contact targetContact = new Contact(1, "jack_slash", "Jack", null, "0");
    ArrayList<Message> messageList = new ArrayList<>();

    private int lastMessageId;

    private BaseAdapter adapter;
    private ListView listView;
    private EditText messageEditText;

    MessagingService messagingService;
    MessagingHandler messagingHandler;

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
        adapter = new MessageListAdapter(this, messageList, scale, ownerContact);
        listView = (ListView) findViewById(R.id.msgListView);
        listView.setAdapter(adapter);

        // setup sendMessageButton configuration
        Button sendMessageButton = (Button) findViewById(R.id.sendMessageButton);
        sendMessageButton.setOnClickListener(this);

        // map message edit text
        messageEditText = (EditText) findViewById(R.id.messageEditText);
        messageEditText.setImeActionLabel("Send", KeyEvent.KEYCODE_ENTER);
        messageEditText.setOnEditorActionListener(this);
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

    // send message on button click
    @Override
    public void onClick(View view) {
        sendMessage();
    }

    // send message on enter press
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_NULL) {
            sendMessage();
            return true;
        }
        return false;
    }

    // handle send message event
    private void sendMessage() {
        String text = messageEditText.getText().toString();

        // ignore empty messages
        if (Objects.equals(text, "")) {
            return;
        }

        // prepare message
        Message message = new Message(
                Message.EMPTY_ID,
                selfContact,
                targetContact,
                new Date(),
                text

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