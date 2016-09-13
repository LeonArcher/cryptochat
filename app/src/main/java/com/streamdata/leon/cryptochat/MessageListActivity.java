package com.streamdata.leon.cryptochat;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.text.ParseException;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.streamdata.leon.cryptochat.models.Contact;
import com.streamdata.leon.cryptochat.models.Message;

public class MessageListActivity extends AppCompatActivity {

    private Date stringToDate(String strDate) {

        Date date = new Date();
        try {
            SimpleDateFormat  format = new SimpleDateFormat("M d yyyy", Locale.ENGLISH);
            date = format.parse(strDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }


    private Message[] mockMessages = {
            new Message(new Contact("alex45", "Alex", null),
                        new Contact("john_s", "John", null),
                        "Hi!, How are you?",
                        stringToDate("2 15 2015")),
            new Message(new Contact("john_s", "John", null),
                        new Contact("alex45", "Alex", null),
                        "i'm fine,  and you?",
                        stringToDate("2 15 2015"))
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        ArrayAdapter<Message> messagesAdapter =
                new ArrayAdapter<Message>(this, R.layout.input_chat_item, mockMessages);

        ListView listView = (ListView) findViewById(R.id.msgListView);
        listView.setAdapter(messagesAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MessageList Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.streamdata.leon.cryptochat/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MessageList Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.streamdata.leon.cryptochat/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
