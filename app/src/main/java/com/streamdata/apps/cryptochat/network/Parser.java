package com.streamdata.apps.cryptochat.network;

import java.util.List;

/**
 * Generic string parser
 */
public interface Parser<T> {

    T parse(String data) throws Exception;
    List<T> parseArray(String data) throws Exception;
    String json(T data) throws Exception;
}
