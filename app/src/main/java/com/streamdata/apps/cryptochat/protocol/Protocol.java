package com.streamdata.apps.cryptochat.protocol;

public interface Protocol {
    boolean isMaster();
    void write(String message);
    String read ();
}
