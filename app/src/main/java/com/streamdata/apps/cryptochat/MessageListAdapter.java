package com.streamdata.apps.cryptochat;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.streamdata.apps.cryptochat.cryptography.CryptographerException;
import com.streamdata.apps.cryptochat.database.DBHandler;
import com.streamdata.apps.cryptochat.models.Message;

import java.util.List;


public class MessageListAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private final List<Message> chatMessageList;
    private final float scaleScreen;
    private final static int messageIndent = 100;

    private final int selfContactId;

    public MessageListAdapter(Activity activity, List<Message> list, float scale)
            throws CryptographerException {

        selfContactId = DBHandler.getInstance().getOwnerContact().getId();
        chatMessageList = list;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        scaleScreen = scale;
    }

    private static class ViewHolder {
        public final TextView txtItem;
        public final FrameLayout frmItem;

        ViewHolder(TextView txtItem, FrameLayout frmItem) {
            this.txtItem = txtItem;
            this.frmItem = frmItem;
        }
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        Message message = chatMessageList.get(position);
        View vi = convertView;

        if (convertView == null){

            vi = inflater.inflate(R.layout.input_chat_item, viewGroup, false);

            viewHolder = new ViewHolder(
                    (TextView) vi.findViewById(R.id.message_text),
                    (FrameLayout) vi.findViewById(R.id.bubble_layout)
            );
            vi.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) vi.getTag();
        }

        FrameLayout bubbleLayout = viewHolder.frmItem;

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        
        int pixels = (int) (messageIndent * scaleScreen);

        // if message is mine then align to right
        if (message.getSenderId() == selfContactId) {
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