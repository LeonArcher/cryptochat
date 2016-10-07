package com.streamdata.apps.cryptochat.network;

import android.util.Log;

import com.streamdata.apps.cryptochat.models.RMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * TODO: Add a class header comment!
 */
public class NetworkObjectLayer {
    public static final String NETWORK_OL_LOG_TAG = "NetworkObjectLayer";

    private final RMessageParser messageParser = new RMessageParser();

    public List<RMessage> getMessages(String bucketId) {
        return null;
    }

    public void postMessage(RMessage message) {

        // create JSON object from RMessage model
        JSONObject jMessage = new JSONObject();
        try {
            jMessage.put("sender_id", message.getSenderId());
            jMessage.put("receiver_id", message.getReceiverId());
            jMessage.put("data", message.getData());

        } catch (JSONException ex) {
            Log.e(NETWORK_OL_LOG_TAG, null, ex);
            // TODO: handle the exception
        }
    }
}
