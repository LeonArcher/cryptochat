package com.streamdata.apps.cryptochat.network;

import com.streamdata.apps.cryptochat.models.RMessage;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * Class for REST web service connection and network objects operations
 */
public class NetworkObjectLayer {

    public static final String WEB_SERVICE_URL = "http://crypto-chat.azurewebsites.net/";

    // init helpers
    private final RMessageParser messageParser = new RMessageParser();
    private final NetworkDataLayer networkDataLayer;

    // network object layer is deployed on existing network data layer
    public NetworkObjectLayer(NetworkDataLayer networkDataLayer) {
        this.networkDataLayer = networkDataLayer;
    }

    public List<RMessage> getMessages(String receiverId) throws IOException, JSONException {

        String response;

        // try to get data from server, abort on error
        response = networkDataLayer.get(getReceiveMessageRequestUrl(receiverId));

        // parse the whole messages array
        return messageParser.parseArray(response);
    }

    public RMessage postMessage(RMessage message) throws IOException, JSONException {

        // create request string from RMessage model
        String request = messageParser.json(message);

        // send message json and get response
        String response = networkDataLayer.post(getSendMessageRequestUrl(), request);

        // convert response to RMessage
        return messageParser.parse(response);
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
