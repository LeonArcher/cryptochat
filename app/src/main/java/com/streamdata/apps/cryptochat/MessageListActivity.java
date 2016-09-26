package com.streamdata.apps.cryptochat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;
import com.streamdata.apps.cryptochat.models.Contact;
import com.streamdata.apps.cryptochat.models.Message;
import com.streamdata.apps.cryptochat.utils.DateUtils;


public class MessageListActivity extends AppCompatActivity {

    // TODO: get contact list and self contact from database
    Contact selfContact = new Contact(Contact.selfId, "alex45", "Alex", null);
    ArrayList<Message> messageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        Contact friendContact = new Contact(1, "jack_slash", "Jack", null);
        messageList.add(new Message(
                selfContact,
                friendContact,
                "Hi!, How are you?",
                DateUtils.stringToDate("2 15 2015")
        ));
        messageList.add(new Message(
                friendContact,
                selfContact,
                "i'm Fine,  and you?",
                DateUtils.stringToDate("2 15 2015")
        ));
        messageList.add(new Message(
                friendContact,
                selfContact,
                "LongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLong" +
                        "LongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLong",
                DateUtils.stringToDate("2 15 2015")
        ));
        messageList.add(new Message(
                selfContact,
                friendContact,
                "LongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLong" +
                        "LongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLong",
                DateUtils.stringToDate("2 15 2015")
        ));
        messageList.add(new Message(
                friendContact,
                selfContact,
                "Shot",
                DateUtils.stringToDate("2 15 2015")
        ));
        messageList.add(new Message(
                selfContact,
                friendContact,
                "Shot",
                DateUtils.stringToDate("2 15 2015")
                ));
        messageList.add(new Message(
                selfContact,
                friendContact,
                "Consecutive",
                DateUtils.stringToDate("2 15 2015")
                ));
        messageList.add(new Message(
                selfContact,
                friendContact,
                "Consecutive",
                DateUtils.stringToDate("2 15 2015")
                ));
        messageList.add(new Message(
                selfContact,
                friendContact,
                "Consecutive",
                DateUtils.stringToDate("2 15 2015")
                ));

        messageList.add(new Message(
                friendContact,
                selfContact,
                "Consecutive",
                DateUtils.stringToDate("2 15 2015")
                ));
        messageList.add(new Message(
                friendContact,
                selfContact,
                "Consecutive",
                DateUtils.stringToDate("2 15 2015")
                ));
        messageList.add(new Message(
                friendContact,
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
