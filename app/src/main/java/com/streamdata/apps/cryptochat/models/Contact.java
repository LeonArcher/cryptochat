package com.streamdata.apps.cryptochat.models;

import android.graphics.Bitmap;

import com.streamdata.apps.cryptochat.cryptography.Cryptographer;
import com.streamdata.apps.cryptochat.cryptography.CryptographerFactory;
import com.streamdata.apps.cryptochat.cryptography.RSACryptographerFactory;
import com.streamdata.apps.cryptochat.utils.Icon;


public class Contact {

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

    public Icon getIcon() {
        return icon;
    }

    public Bitmap getIconBitmap() {
        return icon.getBitmap();
    }

}
