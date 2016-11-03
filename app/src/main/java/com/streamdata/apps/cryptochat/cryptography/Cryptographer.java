package com.streamdata.apps.cryptochat.cryptography;


public interface Cryptographer {
    String encrypt(String output) throws CryptographerException;
    String decrypt(String input) throws CryptographerException;
}
