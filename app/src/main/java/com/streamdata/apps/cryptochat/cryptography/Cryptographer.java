package com.streamdata.apps.cryptochat.cryptography;

public interface Cryptographer {
    String encrypt(String output) throws Exception;
    String decrypt(String input) throws Exception;
}
