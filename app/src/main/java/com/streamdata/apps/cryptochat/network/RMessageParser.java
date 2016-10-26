package com.streamdata.apps.cryptochat.network;

import com.streamdata.apps.cryptochat.models.RMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Parser of REST service message from string
 */
public class RMessageParser implements Parser<RMessage> {

    @Override
    public RMessage parse(String data) throws JSONException {

        JSONObject jObject = new JSONObject(data);

        return new RMessage(
                jObject.getInt("id"),
                jObject.getString("sender_id"),
                jObject.getString("receiver_id"),
                jObject.getString("data"),
                jObject.getString("sent_time")
        );
    }

    @Override
    public List<RMessage> parseArray(String data) throws JSONException {

        JSONArray jArray = new JSONArray(data);
        List<RMessage> messages = new ArrayList<>();

        for (int i = 0; i < jArray.length(); ++i) {

            JSONObject jObject = jArray.getJSONObject(i);

            messages.add(new RMessage(
                    jObject.getInt("id"),
                    jObject.getString("sender_id"),
                    jObject.getString("receiver_id"),
                    jObject.getString("data"),
                    jObject.getString("sent_time")
            ));
        }
        return messages;
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
