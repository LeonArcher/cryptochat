package com.streamdata.apps.cryptochat.models;

import android.graphics.drawable.Drawable;


/**
 * Created by Leon Archer on 12.09.2016.
 */
public class Contact {

    private final String id;
    private final String name;
    private final Drawable icon;

    public Contact(String id, String name, Drawable icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }
}
