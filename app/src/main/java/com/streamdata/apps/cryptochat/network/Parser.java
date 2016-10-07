package com.streamdata.apps.cryptochat.network;

/**
 * Generic string parser
 */
public interface Parser<T> {
    String PARSER_LOG_TAG = "Parser";

    T parse(String data);
    String json(T data);
}
