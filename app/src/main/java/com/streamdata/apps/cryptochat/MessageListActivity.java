package com.streamdata.apps.cryptochat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;
import com.streamdata.apps.cryptochat.models.Contact;
import com.streamdata.apps.cryptochat.models.Message;
import com.streamdata.apps.cryptochat.utils.DateUtils;


public class MessageListActivity extends AppCompatActivity {

    ArrayList<Message> messageList = new ArrayList<Message>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        messageList.add(0, new Message(new Contact("alex45", "Alex", null),
                new Contact("john_s", "John", null),
                "Hi!, How are you?",
                DateUtils.stringToDate("2 15 2015"),
                Boolean.TRUE));
        messageList.add(1, new Message(new Contact("john_s", "John", null),
                new Contact("alex45", "Alex", null),
                "i'm Fine,  and you?",
                DateUtils.stringToDate("2 15 2015"),
                Boolean.FALSE));

        messageList.add(2, new Message(new Contact("john_s", "John", null),
                new Contact("alex45", "Alex", null),
                "LongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLong" +
                        "LongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLong",
                DateUtils.stringToDate("2 15 2015"),
                Boolean.FALSE));
        messageList.add(3, new Message(new Contact( "alex45", "Alex", null),
                new Contact("john_s", "John", null),
                "LongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLong" +
                        "LongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLongLong",
                DateUtils.stringToDate("2 15 2015"),
                Boolean.TRUE));
        messageList.add(4, new Message(new Contact("john_s", "John", null),
                new Contact("alex45", "Alex", null),
                "Shot",
                DateUtils.stringToDate("2 15 2015"),
                Boolean.FALSE));
        messageList.add(5, new Message(new Contact("alex45", "Alex", null),
                new Contact("john_s", "John", null),
                "Shot",
                DateUtils.stringToDate("2 15 2015"),
                Boolean.TRUE));
        messageList.add(6, new Message(new Contact("alex45", "Alex", null),
                new Contact("john_s", "John", null),
                "Consecutive",
                DateUtils.stringToDate("2 15 2015"),
                Boolean.FALSE));
        messageList.add(7, new Message(new Contact("alex45", "Alex", null),
                new Contact("john_s", "John", null),
                "Consecutive",
                DateUtils.stringToDate("2 15 2015"),
                Boolean.FALSE));
        messageList.add(8, new Message(new Contact("alex45", "Alex", null),
                new Contact("john_s", "John", null),
                "Consecutive",
                DateUtils.stringToDate("2 15 2015"),
                Boolean.FALSE));

        messageList.add(9, new Message(new Contact("john_s", "John", null),
                new Contact("alex45", "Alex", null),
                "Consecutive",
                DateUtils.stringToDate("2 15 2015"),
                Boolean.TRUE));
        messageList.add(10, new Message(new Contact("john_s", "John", null),
                new Contact("alex45", "Alex", null),
                "Consecutive",
                DateUtils.stringToDate("2 15 2015"),
                Boolean.TRUE));
        messageList.add(11, new Message(new Contact("john_s", "John", null),
                new Contact("alex45", "Alex", null),
                "Consecutive",
                DateUtils.stringToDate("2 15 2015"),
                Boolean.TRUE));

        float scale = getResources().getDisplayMetrics().density;
        ChatAdapter chatAdapter = new ChatAdapter(this, messageList, scale);
        ListView listView = (ListView) findViewById(R.id.msgListView);
        listView.setAdapter(chatAdapter);

    }
}
