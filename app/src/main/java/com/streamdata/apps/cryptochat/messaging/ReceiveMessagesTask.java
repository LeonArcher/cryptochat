package com.streamdata.apps.cryptochat.messaging;

import android.util.Log;

import com.streamdata.apps.cryptochat.database.ContactNotFoundException;
import com.streamdata.apps.cryptochat.models.RMessage;
import com.streamdata.apps.cryptochat.network.NetworkObjectLayer;
import com.streamdata.apps.cryptochat.scheduling.Task;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for receiving all messages for specified receiverId and skipping messages with id less or
 * equal to skipToId
 */
public class ReceiveMessagesTask implements Task<List<RMessage>> {

    private final NetworkObjectLayer network;
    private final String receiverId;
    private final int skipToId;

    public ReceiveMessagesTask(NetworkObjectLayer network, String receiverId, int skipToId) {
        this.network = network;
        this.receiverId = receiverId;
        this.skipToId = skipToId;
    }

    @Override
    public List<RMessage> run()
            throws IOException, JSONException, ParseException, ContactNotFoundException {

        List<RMessage> messages = new ArrayList<>();

        // get all messages
        List<RMessage> receivedMessages = network.getMessages(receiverId);

        for (RMessage message : receivedMessages) {

            // only new messages should be processed
            if (message.getId() <= skipToId) {
                continue;
            }

//            Message newMessage = MessageAdapter.toMessage(rMessage);

            messages.add(message);

            Log.d(MessageController.MESSAGING_LOG_TAG, message.getData());
        }

        return messages;
    }
}
