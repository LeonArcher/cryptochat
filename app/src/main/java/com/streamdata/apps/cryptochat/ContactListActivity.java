package com.streamdata.apps.cryptochat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.streamdata.apps.cryptochat.models.Contact;

import java.util.ArrayList;

public class ContactListActivity extends AppCompatActivity {

    // todo: implement contacts load from a database
    private ArrayList<Contact> mockContacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        // fill mock contacts array
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.male_icon);
        mockContacts.add(new Contact("alex45", "Alex", bm));
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.santa_icon);
        mockContacts.add(new Contact("john_s", "John", bm));
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.female_icon);
        mockContacts.add(new Contact("jane_f5", "Jane", bm));
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.gentleman_icon);
        mockContacts.add(new Contact("mr_henry_ford", "Henry", bm));
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.lady_icon);
        mockContacts.add(new Contact("elizabeth_2", "Liza", bm));

        // create list view and apply custom contacts adapter
        ListView lvContacts = (ListView) findViewById(R.id.listView);
        ContactAdapter adapter = new ContactAdapter(this);
        lvContacts.setAdapter(adapter);
    }

    // custom adapter for better contacts representation (including additional info)
    private class ContactAdapter extends ArrayAdapter<Contact> {
        public ContactAdapter(Context context) {
            super(context, R.layout.contact_list_item, mockContacts);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Contact contact = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.contact_list_item, null);
            }

            ((TextView) convertView.findViewById(R.id.name))
                    .setText(contact.getName());
            ((ImageView) convertView.findViewById(R.id.icon))
                    .setImageBitmap(contact.getIcon());

            return convertView;
        }
    }
}
