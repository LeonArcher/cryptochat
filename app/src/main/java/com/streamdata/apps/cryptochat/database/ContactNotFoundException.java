package com.streamdata.apps.cryptochat.database;

/**
 * No such contact in db exists
 */
public class ContactNotFoundException extends Exception {

    private final String serverId;

    public ContactNotFoundException(String serverId) {
        this.serverId = serverId;
    }

    public String getServerId() {
        return serverId;
    }
}
