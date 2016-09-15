package com.streamdata.leon.cryptochat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.streamdata.leon.cryptochat.models.Contact;
import com.streamdata.leon.cryptochat.models.Message;
import com.streamdata.leon.cryptochat.utils.DateUtils;


public class MessageListActivity extends AppCompatActivity {

    ArrayList<Message> messageList = new ArrayList<Message>();

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        DateUtils dateUtils = new DateUtils();

        messageList.add(0, new Message(new Contact("alex45", "Alex", null),
                new Contact("john_s", "John", null),
                "Hi!, How are you?",
                dateUtils.stringToDate("2 15 2015"),
                Boolean.TRUE));
        messageList.add(1, new Message(new Contact("john_s", "John", null),
                new Contact("alex45", "Alex", null),
                "i'm Fine,  and you?",
                dateUtils.stringToDate("2 15 2015"),
                Boolean.FALSE));

        ChatAdapter chatAdapter = new ChatAdapter(this, messageList);
        ListView listView = (ListView) findViewById(R.id.msgListView);
        listView.setAdapter(chatAdapter);

    }
}
