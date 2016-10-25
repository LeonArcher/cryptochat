package com.streamdata.apps.cryptochat.database;

/**
 * Exception: no messages from or to this contact found in db
 */
public class MessagesNotFoundException extends Exception {

    private final int targetContactId;

    public MessagesNotFoundException(int targetContactId) {
        this.targetContactId = targetContactId;
    }

    public int getTargetContactId() {
        return targetContactId;
    }
}
