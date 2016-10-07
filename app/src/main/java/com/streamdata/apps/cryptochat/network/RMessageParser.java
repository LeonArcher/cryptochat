package com.streamdata.apps.cryptochat.network;

import android.util.Log;

import com.streamdata.apps.cryptochat.models.RMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Parser of REST service message from string
 */
public class RMessageParser implements Parser<RMessage> {

    @Override
    public RMessage parse(String data) {
        RMessage message;

        try {
            JSONObject jObject = new JSONObject(data);

            message = new RMessage(
                    jObject.getInt("id"),
                    jObject.getString("sender_id"),
                    jObject.getString("receiver_id"),
                    jObject.getString("data"),
                    jObject.getString("sent_time")
            );

        } catch (JSONException ex) {
            Log.e(PARSER_LOG_TAG, null, ex);
            throw new RuntimeException(ex);
        }

        return message;
    }

    @Override
    public String json(RMessage message) {
        JSONObject jMessage = new JSONObject();

        try {
            jMessage.put("sender_id", message.getSenderId());
            jMessage.put("receiver_id", message.getReceiverId());
            jMessage.put("data", message.getData());

        } catch (JSONException ex) {
            Log.e(PARSER_LOG_TAG, null, ex);
            throw new RuntimeException(ex);
        }

        return jMessage.toString();
    }
}
