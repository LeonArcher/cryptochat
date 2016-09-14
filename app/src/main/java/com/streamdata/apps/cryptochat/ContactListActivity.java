package com.streamdata.apps.cryptochat;

import android.content.Context;
import android.content.res.Resources;
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
import com.streamdata.apps.cryptochat.utils.Icon;
import com.streamdata.apps.cryptochat.utils.ResourceIcon;

import java.util.ArrayList;

public class ContactListActivity extends AppCompatActivity {

    // todo: implement contacts load from a database
    private final ArrayList<Contact> mockContacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        // fill mock contacts array
        Resources resources = getResources();
        mockContacts.add(new Contact("alex45", "Alex",
                new ResourceIcon(resources, R.drawable.male_icon)));
        mockContacts.add(new Contact("john_s", "John",
                new ResourceIcon(resources, R.drawable.santa_icon)));
        mockContacts.add(new Contact("jane_f5", "Jane",
                new ResourceIcon(resources, R.drawable.female_icon)));
        mockContacts.add(new Contact("mr_henry_ford", "Henry",
                new ResourceIcon(resources, R.drawable.gentleman_icon)));
        mockContacts.add(new Contact("elizabeth_2", "Liza",
                new ResourceIcon(resources, R.drawable.lady_icon)));

        // create list view and apply custom contacts adapter
        ListView lvContacts = (ListView) findViewById(R.id.listView);
        ContactAdapter adapter = new ContactAdapter(this, mockContacts);
        lvContacts.setAdapter(adapter);
    }

    // custom adapter for better contacts representation (including additional info)
    private static class ContactAdapter extends ArrayAdapter<Contact> {
        public ContactAdapter(Context context, ArrayList<Contact> contacts) {
            super(context, R.layout.contact_list_item, contacts);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            Contact contact = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.contact_list_item, null);

                viewHolder = new ViewHolder();
                viewHolder.txtItem = (TextView) convertView.findViewById(R.id.name);
                viewHolder.imgItem = (ImageView) convertView.findViewById(R.id.icon);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.txtItem.setText(contact.getName());
            viewHolder.imgItem.setImageBitmap(contact.getIconBitmap());

            return convertView;
        }

        // ViewHolder pattern for better list performance
        private static class ViewHolder {
            TextView txtItem;
            ImageView imgItem;
        }
    }
}
