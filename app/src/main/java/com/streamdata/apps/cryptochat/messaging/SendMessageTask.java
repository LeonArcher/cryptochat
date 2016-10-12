package com.streamdata.apps.cryptochat.messaging;

import android.util.Log;

import com.streamdata.apps.cryptochat.models.Message;
import com.streamdata.apps.cryptochat.models.RMessage;
import com.streamdata.apps.cryptochat.network.NetworkObjectLayer;
import com.streamdata.apps.cryptochat.scheduling.Task;
import com.streamdata.apps.cryptochat.utils.MessageAdapter;

/**
 * Class for sending message. All sender/receiver information contains in Message model.
 * Returns response Message.
 */
public class SendMessageTask implements Task<Message, Exception> {

    private Message result = null;
    private Exception error = null;

    private final Message message; // message to send (contains receiver)
    private final NetworkObjectLayer network;

    public SendMessageTask(Message message, NetworkObjectLayer network) {
        this.message = message;
        this.network = network;
    }

    @Override
    public void run() {
        // TODO: send message to database
        RMessage rMessage = MessageAdapter.toRMessage(message);
        // TODO: encrypt the text

        RMessage responseMessage;

        // try to send message, get the response message
        try {
            responseMessage = network.postMessage(rMessage);

        } catch (Exception ex) {
            Log.e(MessageController.MESSAGING_LOG_TAG, null, ex);
            error = ex;
            return;
        }

        // convert RMessage to ordinary Message
        try {
            result = MessageAdapter.toMessage(responseMessage);

        } catch (Exception ex) {
            Log.e(MessageController.MESSAGING_LOG_TAG, null, ex);
            error = ex;
        }
    }

    @Override
    public Exception getError() {
        return error;
    }

    @Override
    public Message getResult() {
        return result;
    }
}
