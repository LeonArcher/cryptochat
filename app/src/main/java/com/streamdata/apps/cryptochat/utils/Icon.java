package com.streamdata.apps.cryptochat.utils;

import android.graphics.Bitmap;

/**
 * Generic Icon interface
 * Icon should create/load bitmap object on request
 */
public interface Icon {
    Bitmap getBitmap();
}
