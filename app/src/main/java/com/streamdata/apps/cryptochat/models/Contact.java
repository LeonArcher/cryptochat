package com.streamdata.apps.cryptochat.models;

import android.graphics.Bitmap;

import com.streamdata.apps.cryptochat.utils.Icon;


/**
 * Created by Leon Archer on 12.09.2016.
 */
public class Contact {

    public static final int selfId = 0;

    private final int id;
    private final String serverId;
    private final String name;
    private final Icon icon;

    public Contact(int id, String serverId, String name, Icon icon) {
        this.id = id;
        this.serverId = serverId;
        this.name = name;
        this.icon = icon;
    }

    @Override
    public String toString() {
        return getName();
    }

    public int getId() {
        return id;
    }

    public String getServerId() {
        return serverId;
    }

    public String getName() {
        return name;
    }

    public Bitmap getIconBitmap() {
        return icon.getBitmap();
    }
}
