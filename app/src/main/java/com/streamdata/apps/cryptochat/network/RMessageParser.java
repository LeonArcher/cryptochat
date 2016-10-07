package com.streamdata.apps.cryptochat.network;

import android.support.annotation.Nullable;
import android.util.Log;

import com.streamdata.apps.cryptochat.models.RMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Parser of REST service message from string
 */
public class RMessageParser implements Parser<RMessage> {

    @Override
    @Nullable public RMessage parse(String data) {
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
            return null;
        }

        return message;
    }
}
