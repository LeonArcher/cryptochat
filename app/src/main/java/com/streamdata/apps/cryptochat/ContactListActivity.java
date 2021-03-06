package com.streamdata.apps.cryptochat;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.streamdata.apps.cryptochat.models.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactListActivity extends AppCompatActivity {

    // TODO: implement contacts load from a database
    private final List<Contact> mockContacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        // create list view and apply custom contacts adapter
        ListView lvContacts = (ListView) findViewById(R.id.listView);
        ContactAdapter adapter = new ContactAdapter(this, mockContacts);
        lvContacts.setAdapter(adapter);
    }

    // custom adapter for better contacts representation (including additional info)
    private static class ContactAdapter extends BaseAdapter {
        private Context context;
        private List<Contact> contacts;

        public ContactAdapter(Context context, List<Contact> contacts) {
            this.context = context;
            this.contacts = contacts;
        }

        @Override
        public Contact getItem(int i) {
            return contacts.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getCount() {
            return contacts.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            Contact contact = getItem(position);

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.contact_list_item, viewGroup, false);

                viewHolder = new ViewHolder(
                        (TextView) convertView.findViewById(R.id.name),
                        (ImageView) convertView.findViewById(R.id.icon)
                );
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
            public final TextView txtItem;
            public final ImageView imgItem;

            public ViewHolder(TextView txtItem, ImageView imgItem) {
                this.txtItem = txtItem;
                this.imgItem = imgItem;
            }
        }
    }
}
