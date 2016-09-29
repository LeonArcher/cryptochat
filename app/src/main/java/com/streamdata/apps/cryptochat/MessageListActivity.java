package com.streamdata.apps.cryptochat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;
import com.streamdata.apps.cryptochat.models.Contact;
import com.streamdata.apps.cryptochat.models.Message;
import com.streamdata.apps.cryptochat.utils.DateUtils;


public class MessageListActivity extends AppCompatActivity {

    // TODO: get message list, self contact and current contact from database
    Contact selfContact = new Contact(Contact.selfId, "alex45", "Alex", null);
    Contact currentContact = new Contact(1, "jack_slash", "Jack", null);
    ArrayList<Message> messageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        messageList.add(new Message(
                0,
                selfContact,
                currentContact,
                "Hi!, How are you?",
                DateUtils.stringToDate("2 15 2015")
        ));
        messageList.add(new Message(
                0,
                currentContact,
                selfContact,
                "i'm Fine,  and you?",
                DateUtils.stringToDate("2 15 2015")
        ));
        messageList.add(new Message(
                0,
                currentContact,
                selfContact,
                "LongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLong" +
                        "LongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLong",
                DateUtils.stringToDate("2 15 2015")
        ));
        messageList.add(new Message(
                0,
                selfContact,
                currentContact,
                "LongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLong" +
                        "LongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLong",
                DateUtils.stringToDate("2 15 2015")
        ));
        messageList.add(new Message(
                0,
                currentContact,
                selfContact,
                "Shot",
                DateUtils.stringToDate("2 15 2015")
        ));
        messageList.add(new Message(
                0,
                selfContact,
                currentContact,
                "Shot",
                DateUtils.stringToDate("2 15 2015")
        ));
        messageList.add(new Message(
                0,
                selfContact,
                currentContact,
                "Consecutive",
                DateUtils.stringToDate("2 15 2015")
        ));
        messageList.add(new Message(
                0,
                selfContact,
                currentContact,
                "Consecutive",
                DateUtils.stringToDate("2 15 2015")
        ));
        messageList.add(new Message(
                0,
                selfContact,
                currentContact,
                "Consecutive",
                DateUtils.stringToDate("2 15 2015")
        ));
        messageList.add(new Message(
                0,
                currentContact,
                selfContact,
                "Consecutive",
                DateUtils.stringToDate("2 15 2015")
        ));
        messageList.add(new Message(
                0,
                currentContact,
                selfContact,
                "Consecutive",
                DateUtils.stringToDate("2 15 2015")
        ));
        messageList.add(new Message(
                0,
                currentContact,
                selfContact,
                "Consecutive",
                DateUtils.stringToDate("2 15 2015")
        ));

        float scale = getResources().getDisplayMetrics().density;
        ChatAdapter chatAdapter = new ChatAdapter(this, messageList, scale);
        ListView listView = (ListView) findViewById(R.id.msgListView);
        listView.setAdapter(chatAdapter);

    }
}
