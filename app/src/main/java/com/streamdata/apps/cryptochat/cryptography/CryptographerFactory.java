package com.streamdata.apps.cryptochat.cryptography;

import com.streamdata.apps.cryptochat.protocol.Protocol;

/**
 * Interface: Factory of Cryptographers.
 */
public interface CryptographerFactory {
    /**
     * Create Cryptographer from blob.
     *
     * @param blob      Input blob of bytes.
     * @return          Object of Cryptographer.
     *
     * @throws CryptographerException
     *
     * @see Cryptographer
     * @see CryptographerException
     */
    Cryptographer create(byte[] blob) throws CryptographerException;

    /**
     * Create Cryptographer from Protocol.
     *
     * @param protocol      Protocol.
     * @return              Object of Cryptographer.
     *
     * @throws CryptographerException
     *
     * @see Cryptographer
     * @see CryptographerException
     * @see Protocol
     */
    Cryptographer create(Protocol protocol) throws CryptographerException;
}
