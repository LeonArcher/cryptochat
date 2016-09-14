package com.streamdata.apps.cryptochat.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Leon Archer on 14.09.2016.
 */
public class ResourceIcon implements Icon {
    private Bitmap data = null;
    private final Resources resources;
    private final int resourceId;

    public ResourceIcon(Resources resources, int resourceId) {
        this.resources = resources;
        this.resourceId = resourceId;
    }

    @Override
    public Bitmap getBitmap() {
        // lazy calculation
        if (data == null) {
            data = BitmapFactory.decodeResource(resources, resourceId);
        }
        return data;
    }
}
