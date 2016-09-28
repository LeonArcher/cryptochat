package com.streamdata.apps.cryptochat.utils;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DataIcon implements Icon {
    private Bitmap data = null;

    public DataIcon(byte[] bitmapdata) {
        this.data = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
    }

    @Override
    public Bitmap getBitmap() {
        return data;
    }
}
