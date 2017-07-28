package com.streamdata.apps.cryptochat.utils;

import android.graphics.Bitmap;
import android.util.Log;

import com.streamdata.apps.cryptochat.cryptography.Cryptographer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class DataBaseUtils {

    public static byte[] iconToBytes(Icon icon) {
        ByteArrayOutputStream byteArrayOutputStreamIcon = new ByteArrayOutputStream();
        byte[] byteArray = null;

        try {
            icon.getBitmap().compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStreamIcon);
            byteArray = byteArrayOutputStreamIcon.toByteArray();
        } finally {
            try {
                byteArrayOutputStreamIcon.close();
            } catch (IOException e) {
                Log.e("iconToBytes", e.getMessage());
            }
        }

        return byteArray;
    }

    public static byte[] cryptographerToBytes(Cryptographer cryptographer) {

        ByteArrayOutputStream byteArrayOutputStreamCryptographer = new ByteArrayOutputStream();
        byte[] cryptographerBytes = null;
        try {
            ObjectOutput out = null;
            out = new ObjectOutputStream(byteArrayOutputStreamCryptographer);
            out.writeObject(cryptographer);
            out.flush();
            cryptographerBytes = byteArrayOutputStreamCryptographer.toByteArray();
        } catch (IOException e){
            Log.e("iconToBytes", e.getMessage());
        } finally {
            try {
                byteArrayOutputStreamCryptographer.close();
            } catch (IOException e) {
                Log.e("iconToBytes", e.getMessage());
            }
        }

        return cryptographerBytes;
    }
}
