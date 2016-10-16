package com.streamdata.apps.cryptochat.messaging;

import com.streamdata.apps.cryptochat.models.Message;
import com.streamdata.apps.cryptochat.models.RMessage;
import com.streamdata.apps.cryptochat.network.NetworkObjectLayer;
import com.streamdata.apps.cryptochat.scheduling.Task;
import com.streamdata.apps.cryptochat.utils.MessageAdapter;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;

/**
 * Class for sending message. All sender/receiver information contains in Message model.
 * Returns response Message.
 */
public class SendMessageTask implements Task<Message> {

    private final Message message; // message to send (contains receiver)
    private final NetworkObjectLayer network;

    public SendMessageTask(Message message, NetworkObjectLayer network) {
        this.message = message;
        this.network = network;
    }

    @Override
    public Message run() throws IOException, JSONException, ParseException {
        // TODO: send message to database
        RMessage rMessage = MessageAdapter.toRMessage(message);
        // TODO: encrypt the text

        // send message, get the response message
        RMessage responseMessage = network.postMessage(rMessage);

        // convert RMessage to ordinary Message
        return MessageAdapter.toMessage(responseMessage);
    }
}
