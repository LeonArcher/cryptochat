package com.streamdata.apps.cryptochat.models;

import android.graphics.Bitmap;

import com.streamdata.apps.cryptochat.utils.Icon;


public class Contact {

    private final int id;
    private final String name;
    private final Icon icon;
    private final String publicKey;

    public Contact(int id, String name, Icon icon, String publicKey) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return getName();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Bitmap getIconBitmap() {
        return icon.getBitmap();
    }

    public String getPublicKey() { return publicKey; }
}
