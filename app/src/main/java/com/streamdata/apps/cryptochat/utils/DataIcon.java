package com.streamdata.apps.cryptochat.utils;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

public class DataIcon implements Icon {
    private Bitmap data = null;

    public DataIcon() {
    }

    public void create(byte[] bitmapdata) {
        data = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
    }

    @Override
    @Nullable
    public Bitmap getBitmap() {
        return data;
    }
}
