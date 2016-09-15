package com.streamdata.leon.cryptochat;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.streamdata.leon.cryptochat.models.Message;


public class ChatAdapter extends BaseAdapter {

    private LayoutInflater inflater = null;
    ArrayList<Message> chatMessageList;

    public ChatAdapter(Activity activity, ArrayList<Message> list) {
        chatMessageList = list;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    static class ViewHolder {
        TextView txtItem;
    }

    @Override
    public int getCount() {
        return chatMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Message message = (Message) chatMessageList.get(position);
        View vi = convertView;

        if (convertView == null){

            vi = inflater.inflate(R.layout.input_chat_item, null);
            viewHolder = new ViewHolder();
            viewHolder.txtItem = (TextView) vi.findViewById(R.id.message_text);
            vi.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) vi.getTag();
        }

        LinearLayout bubbleLayout = (LinearLayout) vi.findViewById(R.id.bubble_layout);
        // if message is mine then align to right
        if (message.isMine) {
            bubbleLayout.setBackgroundResource(R.drawable.chat_bubble_left);
        }
        // If not mine then align to left
        else {
            bubbleLayout.setBackgroundResource(R.drawable.chat_bubble_right);
        }

        viewHolder.txtItem.setText(message.getText());

        return vi;
    }


    public void add(Message object) {
        chatMessageList.add(object);
    }
}