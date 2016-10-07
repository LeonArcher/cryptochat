package com.streamdata.apps.cryptochat.network;

import android.util.Log;

import com.streamdata.apps.cryptochat.models.RMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Class for REST web service connection and network objects operations
 */
public class NetworkObjectLayer {
    public static final String NETWORK_OL_LOG_TAG = "NetworkObjectLayer";
    public static final String WEB_SERVICE_URL = "http://crypto-chat.azurewebsites.net/";

    // init helpers
    private final RMessageParser messageParser = new RMessageParser();
    private final NetworkDataLayer networkDataLayer = new NetworkDataLayer();

    public ArrayList<RMessage> getMessages(String receiverId) throws IOException, JSONException {

        String response;

        // try to get data from server, abort on error
        response = networkDataLayer.get(getReceiveMessageRequestUrl(receiverId));

        JSONArray jArray;

        // try to parse the whole data array
        jArray = new JSONArray(response);

        ArrayList<RMessage> messageList = new ArrayList<>();

        // parse each individual message, on error continue
        for (int i = 0; i < jArray.length(); ++i) {
            RMessage message;

            try {
                JSONObject jObject = jArray.getJSONObject(i);
                // TODO: optimize one extra JSON transform
                message = messageParser.parse(jObject.toString());

            } catch (JSONException ex) {
                Log.e(NETWORK_OL_LOG_TAG, null, ex);
                continue;
            }

            // add successfully parsed message to output
            messageList.add(message);
        }

        return messageList;
    }

    public void postMessage(RMessage message) throws IOException {

        // create request string from RMessage model
        String request = messageParser.json(message);

        // send message json
        networkDataLayer.post(getSendMessageRequestUrl(), request);
    }

    private static String getReceiveMessageRequestUrl(String id) {
        return String.format(
                "%s/api/packages/%s",
                WEB_SERVICE_URL,
                id
        );
    }

    private static String getSendMessageRequestUrl() {
        return String.format(
                "%s/api/packages/",
                WEB_SERVICE_URL
        );
    }
}
