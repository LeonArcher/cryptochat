package com.streamdata.apps.cryptochat.messaging;

import android.util.Log;

import com.streamdata.apps.cryptochat.models.Message;
import com.streamdata.apps.cryptochat.models.RMessage;
import com.streamdata.apps.cryptochat.network.NetworkObjectLayer;
import com.streamdata.apps.cryptochat.scheduling.Task;
import com.streamdata.apps.cryptochat.utils.MessageAdapter;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Class for receiving all messages for specified receiverId and skipping messages with id less or
 * equal to skipToId
 */
public class ReceiveMessagesTask implements Task<ArrayList<Message>> {

    private final NetworkObjectLayer network;
    private final String receiverId;
    private final int skipToId;

    public ReceiveMessagesTask(NetworkObjectLayer network, String receiverId, int skipToId) {
        this.network = network;
        this.receiverId = receiverId;
        this.skipToId = skipToId;
    }

    @Override
    public ArrayList<Message> run() throws IOException, JSONException, ParseException {
        ArrayList<Message> messages = new ArrayList<>();

        // get all messages
        ArrayList<RMessage> rMessages = network.getMessages(receiverId);

        for (RMessage rMessage : rMessages) {

            // the receiver should always be self contact
            if (!Objects.equals(rMessage.getReceiverId(), receiverId)) {

                // TODO: select proper exception type
                IOException ex = new IOException("Got wrong receiver id");
                Log.e(MessageController.MESSAGING_LOG_TAG, null, ex);
                throw ex;
            }

            // only new messages should be processed
            if (rMessage.getId() <= skipToId) {
                continue;
            }

            // TODO: decrypt the message text

            Message newMessage = MessageAdapter.toMessage(rMessage);

            messages.add(newMessage);
            // TODO: send message to database

            Log.d(MessageController.MESSAGING_LOG_TAG, newMessage.getText());
        }

        return messages;
    }
}
