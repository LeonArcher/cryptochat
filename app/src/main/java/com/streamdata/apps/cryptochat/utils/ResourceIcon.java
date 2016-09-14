package com.streamdata.apps.cryptochat.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Leon Archer on 14.09.2016.
 */
public class ResourceIcon implements Icon {
    private Bitmap data;

    public ResourceIcon(Resources resources, int resourceId) {
        data = BitmapFactory.decodeResource(resources, resourceId);
    }

    @Override
    public Bitmap getBitmap() {
        return data;
    }
}
