package com.streamdata.apps.cryptochat.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Leon Archer on 30.09.2016.
 */
public class Network {
    public static final String NETWORK_LOG_TAG = "Network";
    public static final int DEFAULT_TIMEOUT = 5000;

    public static String getJSON(String url, int timeout) {
        HttpURLConnection connection = null;
        try {
            // setup and process connection
            URL u = new URL(url);
            connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-length", "0");
            connection.setUseCaches(false);
            connection.setAllowUserInteraction(false);
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.connect();

            // receive data from input stream on successful response
            int status = connection.getResponseCode();
            switch (status) {
                case 200:
                case 201:
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));

                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    reader.close();
                    return stringBuilder.toString();
            }

        } catch (IOException ex) {
            Log.d(NETWORK_LOG_TAG, null, ex);

        } finally { // close connection if necessary
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    Log.d(NETWORK_LOG_TAG, null, ex);
                }
            }
        }
        return null;
    }
}
