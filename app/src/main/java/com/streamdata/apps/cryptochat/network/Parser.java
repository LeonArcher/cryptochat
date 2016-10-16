package com.streamdata.apps.cryptochat.network;

import java.util.ArrayList;

/**
 * Generic string parser
 */
public interface Parser<T> {

    T parse(String data) throws Exception;
    ArrayList<T> parseArray(String data) throws Exception;
    String json(T data) throws Exception;
}
