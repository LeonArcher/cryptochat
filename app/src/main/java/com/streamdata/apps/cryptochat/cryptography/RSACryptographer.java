package com.streamdata.apps.cryptochat.cryptography;

import android.util.Base64;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 * Cryptographer based on RSA.
 */
public class RSACryptographer implements Cryptographer, Externalizable {
    private  PrivateKey privateKey = null;
    private  PublicKey publicKey = null;
    private  byte[] privateKeyBytes;
    private  byte[] publicKeyBytes;
    private  KeyFactory keyFactory;

    RSACryptographer(byte[] privateKey, byte[] publicKey) {
//        privateKeyBytes = privateKey;
        privateKeyBytes = Arrays.copyOf(privateKey, privateKey.length);
        publicKeyBytes = Arrays.copyOf(publicKey, publicKey.length);
    }

    RSACryptographer() {
    }

    @Override
    public String encrypt(String text) throws CryptographerException  {

        String encryptException;
        try {
            byte[] ciphertext = encrypt(getPublicKey(), text.getBytes());
            encryptException = Base64.encodeToString(ciphertext, Base64.DEFAULT);
        } catch (CryptographerException e) {
            throw new CryptographerException(e.getMessage());
        }

        return encryptException;
    }

    /**
     * Encrypt byte array to byte array.
     *
     * @param publicKey         Public key for RSA.
     * @param toBeCiphred       Byte array that needs to be encrypted.
     * @return                  Encrypted byte array.
     *
     * @throws CryptographerException
     *
     * @see CryptographerException
     */
    private byte[] encrypt(Key publicKey, byte[] toBeCiphred) throws CryptographerException{
        byte[] encryptMessage;

        try {
            Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding", "SC");
            rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encryptMessage = rsaCipher.doFinal(toBeCiphred);
        } catch (NoSuchPaddingException | InvalidKeyException | NoSuchProviderException |
                NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException e) {

            throw new CryptographerException(e.getMessage());
        }

        return encryptMessage;
    }

    @Override
    public String decrypt(String ciphertext) throws CryptographerException {
        String decryptMessage;

        try {
            byte[] afterDecrypting = decrypt(getPrivateKey(), Base64.decode(ciphertext, Base64.DEFAULT));
            decryptMessage = stringify(afterDecrypting);
        } catch (CryptographerException e) {
            throw new CryptographerException(e.getMessage());
        }

        return decryptMessage;
    }

    /**
     * Decrypt byte array to byte array.
     *
     * @param privateKey        Private key for RSA.
     * @param encryptedText     Byte array that needs to be decrypted.
     * @return                  Decrypted byte array.
     *
     * @throws CryptographerException
     *
     * @see CryptographerException
     */
    public byte[] decrypt(Key privateKey, byte[] encryptedText) throws CryptographerException{

        byte[] decryptMessage;

        try {
            Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding", "SC");
            rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
            decryptMessage = rsaCipher.doFinal(encryptedText);

        } catch ( NoSuchPaddingException | NoSuchProviderException | NoSuchAlgorithmException |
                InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new CryptographerException(e.getMessage());
        }

        return decryptMessage;
    }

    private String stringify(byte[] bytes) {

        return stringify(new String(bytes));
    }

    private String stringify(String str) {
        String aux = "";
        for (int i = 0; i < str.length(); i++) {
            aux += str.charAt(i);
        }

        return aux;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(privateKeyBytes);
        out.writeObject(publicKeyBytes);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        privateKeyBytes = (byte[]) in.readObject();
        publicKeyBytes = (byte[]) in.readObject();
    }

    private KeyFactory getKeyFactory() throws CryptographerException {
        if (keyFactory == null) {
            try {
                keyFactory = KeyFactory.getInstance("RSA");
            } catch (NoSuchAlgorithmException e) {
                throw new CryptographerException(e.getMessage());
            }
        }

        return keyFactory;
    }

    private PrivateKey getPrivateKey() throws CryptographerException {
        if (privateKey == null) {
            try {
                privateKey = getKeyFactory().generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
            } catch (InvalidKeySpecException e) {
                throw new CryptographerException(e.getMessage());
            }
        }

        return privateKey;
    }

    private PublicKey getPublicKey() throws CryptographerException {
        if (publicKey == null) {
            try {
                publicKey = getKeyFactory().generatePublic(new X509EncodedKeySpec(publicKeyBytes));
            } catch (InvalidKeySpecException e) {
                throw new CryptographerException(e.getMessage());
            }
        }

        return publicKey;
    }
}
