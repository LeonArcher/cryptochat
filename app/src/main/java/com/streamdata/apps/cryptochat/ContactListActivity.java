package com.streamdata.apps.cryptochat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.streamdata.apps.cryptochat.models.Contact;

public class ContactListActivity extends AppCompatActivity {

    private Contact[] mockContacts = {
            new Contact("alex45", "Alex", null),
            new Contact("john_s", "John", null)
    };
    // todo: implement contacts load from a database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        ListView lvContacts = (ListView) findViewById(R.id.listView);

        // todo: implement a custom adapter to use photo of each contact
        ArrayAdapter<Contact> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, mockContacts);
        lvContacts.setAdapter(adapter);
    }
}
