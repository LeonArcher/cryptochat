package com.streamdata.apps.cryptochat.messaging;

import android.util.Log;

import com.streamdata.apps.cryptochat.database.ContactNotFoundException;
import com.streamdata.apps.cryptochat.database.DBHandler;
import com.streamdata.apps.cryptochat.models.Message;
import com.streamdata.apps.cryptochat.models.RMessage;
import com.streamdata.apps.cryptochat.network.NetworkObjectLayer;
import com.streamdata.apps.cryptochat.scheduling.Task;
import com.streamdata.apps.cryptochat.utils.MessageAdapter;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Receiving messages:
 * all messages for specified receiverId get received,
 * messages with Date before or equal to skipToDate get removed
 *
 * Filtering messages:
 * messages with sender serverId different from targetId get removed,
 * messages with receiver serverId different from receiverId get removed
 *
 * Converting messages:
 * using database helper, result messages get converted to Message model
 */
public class ReceiveMessagesTask implements Task<List<Message>> {

    private final NetworkObjectLayer network;

    private final String receiverId;
    private final String targetId;

    private final LastMessageDateProvider lmdProvider;

    private final DBHandler db;

    public ReceiveMessagesTask(
            NetworkObjectLayer network,
            String receiverId,
            String targetId,
            LastMessageDateProvider lmdProvider,
            DBHandler db
    ) {
        this.network = network;
        this.receiverId = receiverId;

        this.targetId = targetId;
        this.lmdProvider = lmdProvider;

        this.db = db;
    }

    @Override
    public List<Message> run()
            throws IOException, JSONException, ParseException, ContactNotFoundException {

        List<Message> resultMessages = new ArrayList<>();

        // get all messages
        List<RMessage> receivedMessages = network.getMessages(receiverId);

        // iterate through all messages and perform filtering and conversion procedures
        for (RMessage message : receivedMessages) {

            // pass only messages sent from target id to receiver id
            if (!Objects.equals(message.getSenderId(), targetId) ||
                    !Objects.equals(message.getReceiverId(), receiverId)) {
                continue;
            }

            // perform conversion
            Message convertedMessage = MessageAdapter.toMessage(message, db);

            // filter old messages
            Date skipToDate = lmdProvider.get(targetId);
            if (skipToDate != null && !convertedMessage.getDate().after(skipToDate)) {
                continue;
            }

            resultMessages.add(convertedMessage);

            Log.d(MessageController.MESSAGING_LOG_TAG, message.getData());
        }

        return resultMessages;
    }
}
