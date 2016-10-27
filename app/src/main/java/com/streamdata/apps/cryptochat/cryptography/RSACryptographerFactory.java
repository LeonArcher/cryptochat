package com.streamdata.apps.cryptochat.cryptography;

import com.streamdata.apps.cryptochat.protocol.Protocol;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAKeyGenParameterSpec;


public class RSACryptographerFactory implements CryptographerFactory {
    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }
    private final int KEY_SIZE = 1024;

    public Cryptographer create()  throws NoSuchProviderException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeySpecException {

        SecureRandom random = new SecureRandom();
        RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(KEY_SIZE, RSAKeyGenParameterSpec.F4);
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "SC");
        generator.initialize(spec, random);

        KeyPair keyPair = generator.generateKeyPair();

        return new RSACryptographer(keyPair.getPrivate().getEncoded(),
                                    keyPair.getPublic().getEncoded());
    }

    @Override
    public Cryptographer create(byte[] blob) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(blob);
        ObjectInputStream is = new ObjectInputStream(in);

        return (Cryptographer)is.readObject();
    }

    @Override
    public Cryptographer create(Protocol protocol)  throws Exception {
        return null;
    }
}
