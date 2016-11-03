package com.streamdata.apps.cryptochat.cryptography;

/**
 * Exception: error with Cryptographer.
 */
public class CryptographerException extends Exception {

    CryptographerException (String message){
        super(message);
    }
}
