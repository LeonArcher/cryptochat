package com.streamdata.apps.cryptochat.cryptography;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSACryptographer implements Cryptographer {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    RSACryptographer(byte[] privateKey, byte[] publicKey) throws NoSuchAlgorithmException,
            InvalidKeySpecException {

        KeyFactory kf = KeyFactory.getInstance("RSA");

        this.privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(privateKey));
        this.publicKey = kf.generatePublic(new X509EncodedKeySpec(publicKey));
    }

    @Override
    public String encrypt(String text) throws Exception  {

        byte[] ciphertext = encrypt(publicKey, text.getBytes());

        return Base64.encodeToString(ciphertext, Base64.DEFAULT);
    }

    private byte[] encrypt(Key publicKey, byte[] toBeCiphred) throws NoSuchPaddingException,
            InvalidKeyException, NoSuchProviderException, NoSuchAlgorithmException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding", "SC");
        rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return rsaCipher.doFinal(toBeCiphred);
    }

    @Override
    public String decrypt(String ciphertext) throws Exception {

        byte[] afterDecrypting = decrypt(privateKey, Base64.decode(ciphertext, Base64.DEFAULT));

        return stringify(afterDecrypting);
    }

    public byte[] decrypt(Key privateKey, byte[] encryptedText) throws NoSuchPaddingException,
            NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding", "SC");
        rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);

        return rsaCipher.doFinal(encryptedText);
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
}
