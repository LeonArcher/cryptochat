package com.streamdata.apps.cryptochat.network;

import com.streamdata.apps.cryptochat.models.RMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Parser of REST service message from string
 */
public class RMessageParser implements Parser<RMessage> {

    @Override
    public RMessage parse(String data) throws JSONException {

        JSONObject jObject = new JSONObject(data);

        RMessage message = new RMessage(
                jObject.getInt("id"),
                jObject.getString("sender_id"),
                jObject.getString("receiver_id"),
                jObject.getString("data"),
                jObject.getString("sent_time")
        );

        return message;
    }

    @Override
    public String json(RMessage message) throws JSONException {
        JSONObject jMessage = new JSONObject();

        jMessage.put("sender_id", message.getSenderId());
        jMessage.put("receiver_id", message.getReceiverId());
        jMessage.put("data", message.getData());

        return jMessage.toString();
    }
}
