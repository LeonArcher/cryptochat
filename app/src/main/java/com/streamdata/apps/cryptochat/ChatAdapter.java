package com.streamdata.apps.cryptochat;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.streamdata.apps.cryptochat.models.Message;



public class ChatAdapter extends BaseAdapter {
    //Achtung! Глобальная переменная
    int mine_id = 0;

    private final LayoutInflater inflater;
    private final ArrayList<Message> chatMessageList;
    private final float scaleScreen;
    private final int messageIndent = 100;

    public ChatAdapter(Activity activity, ArrayList<Message> list, float scale) {
        chatMessageList = list;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        scaleScreen = scale;
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

        FrameLayout bubbleLayout = (FrameLayout) vi.findViewById(R.id.bubble_layout);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        
        int pixels = (int) (messageIndent * scaleScreen);

        // if message is mine then align to right
        if (message.getSenderId() == mine_id) {
            bubbleLayout.setBackgroundResource(R.drawable.chat_bubble_left);
            params.gravity = Gravity.START;
            params.setMarginEnd(pixels);
        }
        // If not mine then align to left
        else {
            bubbleLayout.setBackgroundResource(R.drawable.chat_bubble_right);
            params.gravity = Gravity.END;
            params.setMarginStart(pixels);
        }

        bubbleLayout.setLayoutParams(params);
        viewHolder.txtItem.setText(message.getText());

        return vi;
    }

    public void add(Message object) {
        chatMessageList.add(object);
    }
}