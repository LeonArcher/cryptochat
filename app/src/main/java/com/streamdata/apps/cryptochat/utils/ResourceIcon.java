package com.streamdata.apps.cryptochat.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Icon, created from one of resource drawables
 * Returns bitmap object only on request (stores an object after request)
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
