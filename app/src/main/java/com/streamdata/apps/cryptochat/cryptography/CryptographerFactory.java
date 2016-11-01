package com.streamdata.apps.cryptochat.cryptography;

import com.streamdata.apps.cryptochat.protocol.Protocol;


public interface CryptographerFactory {
    Cryptographer create(byte[] blob) throws CryptographerException;
    Cryptographer create(Protocol protocol) throws CryptographerException;
}
