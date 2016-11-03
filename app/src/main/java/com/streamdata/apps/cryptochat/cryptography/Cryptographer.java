package com.streamdata.apps.cryptochat.cryptography;

/**
 * Interface: Cryptographer - encrypt and decrypt messages.
 */
public interface Cryptographer {
    /**
     * Encrypt input String.
     *
     * @param output    The string that needs to be encrypt.
     * @return          Encrypted string.
     *
     * @throws CryptographerException
     *
     * @see CryptographerException
     */
    String encrypt(String output) throws CryptographerException;

    /**
     * Dencrypt input String.
     *
     * @param input     The string that needs to be decrypt.
     * @return          Decrypted string.
     *
     * @throws CryptographerException
     *
     * @see CryptographerException
     */
    String decrypt(String input) throws CryptographerException;
}
