package com.streamdata.apps.cryptochat.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;

import com.streamdata.apps.cryptochat.models.Contact;
import com.streamdata.apps.cryptochat.models.Message;
import com.streamdata.apps.cryptochat.utils.DataIcon;
import com.streamdata.apps.cryptochat.utils.DateUtils;

import java.io.ByteArrayOutputStream;

/**
 * Auxiliary functional for the database.
 */
class DatabaseAdapter {

    /**
     * Transform Contact to ContentValues of the DataBase.
     * @param contact Message
     * @return ContentValues for the Database
     */
    static ContentValues contactToContentValues(Contact contact) {

        ContentValues values = new ContentValues();

        values.put(DBHandler.KEY_CONTACT_ID, contact.getId());
        values.put(DBHandler.KEY_SERVER_ID, contact.getServerId());
        values.put(DBHandler.KEY_NAME, contact.getName());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        contact.getIconBitmap().compress(Bitmap.CompressFormat.PNG, 0, bos);
        values.put(DBHandler.KEY_ICON, bos.toByteArray());

        return values;
    }

    /**
     * Transform Cursor of the DataBase to Contact instance.
     * @param cursor Cursor
     * @return Contact instance.
     */
    static Contact cursorToContact(Cursor cursor) {
        DataIcon icon = new DataIcon();
        icon.create(cursor.getBlob(cursor.getColumnIndex(DBHandler.KEY_ICON)));

        return new Contact(
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBHandler.KEY_CONTACT_ID))),
                cursor.getString(cursor.getColumnIndex(DBHandler.KEY_SERVER_ID)),
                cursor.getString(cursor.getColumnIndex(DBHandler.KEY_NAME)),
                icon
        );
    }

    /**
     * Transform Message to ContentValues of the DataBase.
     * @param message Message
     * @return ContentValues for the Database
     */
    static ContentValues messageToContentValues(Message message) {
        ContentValues values = new ContentValues();
        values.put(DBHandler.KEY_SENDER_ID, message.getSenderId());
        values.put(DBHandler.KEY_RECEIVER_ID, message.getReceiverId());
        values.put(DBHandler.KEY_DATE, DateUtils.dateToString(message.getDate()));
        values.put(DBHandler.KEY_TEXT, message.getText());

        return values;
    }

    /**
     * Transform Cursor of the DataBase to Message value.
     * @param cursor Cursor
     * @return Message instance.
     */
    static Message cursorToMessage(Cursor cursor) {
        return new Message(
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBHandler.KEY_MESSAGE_ID))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBHandler.KEY_SENDER_ID))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBHandler.KEY_RECEIVER_ID))),
                DateUtils.stringToDate(cursor.getString(cursor.getColumnIndex(DBHandler.KEY_DATE))),
                cursor.getString(cursor.getColumnIndex(DBHandler.KEY_TEXT))
        );
    }
}
