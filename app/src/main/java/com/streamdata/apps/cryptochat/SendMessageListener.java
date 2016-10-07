package com.streamdata.apps.cryptochat;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.streamdata.apps.cryptochat.MessageListActivity;

import java.lang.ref.WeakReference;

/**
 * Send message event listener for MessageListActivity class
 * (listens both button click and keyboard enter events)
 */
public class SendMessageListener implements View.OnClickListener, TextView.OnEditorActionListener {

    private final WeakReference<MessageListActivity> parentActivityReference;

    public SendMessageListener(MessageListActivity parent) {
        parentActivityReference = new WeakReference<>(parent);
    }

    // send message on button click
    @Override
    public void onClick(View view) {
        MessageListActivity parent = parentActivityReference.get();

        if (parent == null) {
            return;
        }

        parent.sendMessage();
    }

    // send message on enter press
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        MessageListActivity parent = parentActivityReference.get();

        if (parent == null) {
            return false;
        }

        if (actionId == EditorInfo.IME_NULL) {
            parent.sendMessage();
            return true;
        }
        return false;
    }
}
