package com.streamdata.apps.cryptochat;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.streamdata.apps.cryptochat.models.Contact;
import com.streamdata.apps.cryptochat.models.Message;
import com.streamdata.apps.cryptochat.utils.DataIcon;
import com.streamdata.apps.cryptochat.utils.DateUtils;
import com.streamdata.apps.cryptochat.utils.Icon;
import com.streamdata.apps.cryptochat.utils.ResourceIcon;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


@WorkerThread
public class DBHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "DB";
    // Table name
    private static final String TABLE_CONTACT = "Contact";
    private static final String TABLE_MESSAGE = "Message";
    // Contacts Table Contacts names
    private static final String KEY_CONTACT_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ICON= "icon";
    private static final String KEY_PUBLIC_KEY= "public_key";
    // Table Messages names
    private static final String KEY_MESSAGE_ID = "id";
    private static final String KEY_TEXT = "text";
    private static final String KEY_DATE = "date";
    private static final String KEY_SENDER_ID = "sender_id";
    private static final String KEY_RECEIVER_ID = "receiver_id";

    // Table Create Statements
    // Contact table create statement
    private static final String CREATE_TABLE_CONTACT =
            String.format("CREATE TABLE %s ( %s  INTEGER PRIMARY KEY, %s  TEXT, %s BLOB, %s TEXT)",
                                                                                TABLE_CONTACT,
                                                                                KEY_CONTACT_ID,
                                                                                KEY_NAME,
                                                                                KEY_ICON,
                                                                                KEY_PUBLIC_KEY);

    // Message table create statement
    private static final String CREATE_TABLE_MESSAGE =
            String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s  TEXT, %s  DATETIME, %s INTEGER, %s INTEGER )",
                                                                                TABLE_MESSAGE,
                                                                                KEY_MESSAGE_ID,
                                                                                KEY_TEXT,
                                                                                KEY_DATE,
                                                                                KEY_SENDER_ID,
                                                                                KEY_RECEIVER_ID);

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DataBase", "Create DataBase");
        // creating required tables
        db.execSQL(CREATE_TABLE_CONTACT);
        db.execSQL(CREATE_TABLE_MESSAGE);

        Log.d("DataBase", "DataBase has created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DataBase", "Upgrade DataBase");

        if (newVersion <= oldVersion) {
            String exceptionMessage = String.format(Locale.US,"%d <= %d", newVersion, oldVersion);
            IllegalArgumentException ex = new IllegalArgumentException(exceptionMessage);
            Log.e("DataBase", "", ex);
            throw ex;
        }

        // on upgrade drop older tables
        db.execSQL(String.format(Locale.US,"DROP TABLE IF EXISTS %s", TABLE_CONTACT));
        db.execSQL(String.format(Locale.US,"DROP TABLE IF EXISTS %s", TABLE_MESSAGE));

        // create new tables
        onCreate(db);
    }

    // Adding new Contact
    public void addContact(Contact contact) {
        Log.d("DataBase", "Add Contact to DataBase");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CONTACT_ID, contact.getId());
        values.put(KEY_NAME, contact.getName());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        contact.getIconBitmap().compress(Bitmap.CompressFormat.PNG, 0, bos);
        values.put(KEY_ICON, bos.toByteArray());
        values.put(KEY_PUBLIC_KEY, contact.getPublicKey());

        // Inserting Row
        db.insert(TABLE_CONTACT, null, values);
    }

    // Getting Contact by id
    public Contact getContact(int id) {
        Log.d("DataBase", "Get Contact from DataBase by id");
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACT, new String[] { KEY_CONTACT_ID,
                        KEY_NAME, KEY_ICON, KEY_PUBLIC_KEY},
                        String.format(Locale.US, "%s  =? ", KEY_CONTACT_ID),
                        new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        // TODO: Посмтореть как правильно обработать

        Contact contact = new Contact(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))),
                "Achtung",
                cursor.getString(cursor.getColumnIndex("name")),
                new DataIcon(cursor.getBlob(cursor.getColumnIndex("icon"))),
                cursor.getString(cursor.getColumnIndex("public_key")));

        cursor.close();

        return contact;
    }

    // Adding new Message
    public void addMessage(Message message) {
        Log.d("DataBase", "Add Message to DataBase");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_MESSAGE_ID, message.getId());
        values.put(KEY_SENDER_ID, message.getSender().getId());
        values.put(KEY_RECEIVER_ID, message.getReceiver().getId());
        values.put(KEY_DATE, DateUtils.dateToString(message.getDate()));
        values.put(KEY_TEXT, message.getText());

        // Inserting Row
        db.insert(TABLE_MESSAGE, null, values);
    }

    // Getting one message
    public Message getMessage(int id) {
        Log.d("DataBase", "Get Message from DataBase by id");
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_MESSAGE, new String[] { KEY_MESSAGE_ID,
                        KEY_SENDER_ID, KEY_RECEIVER_ID, KEY_DATE, KEY_TEXT},
                        String.format(Locale.US,"%s  =?", KEY_MESSAGE_ID),
                        new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        // TODO: Посмтореть как правильно обработать

        //  Make sender and receiver contact
        Contact sender = getContact(Integer.parseInt
                (cursor.getString(cursor.getColumnIndex("sender_id"))));
        Contact receiver = getContact(Integer.parseInt
                (cursor.getString(cursor.getColumnIndex("receiver_id"))));

        Message message = new Message(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))),
                sender,
                receiver,
                DateUtils.stringToDate(cursor.getString(cursor.getColumnIndex("date"))),
                cursor.getString(cursor.getColumnIndex("text")));

        cursor.close();
        return message;
    }

    // Getting All Message by senderId
    public List<Message> getAllMessageBySenderId(int senderId) {
        Log.d("DataBase", "Get all messages from DataBase by senderId");
        List<Message> messageList = new ArrayList<Message>();
        // Select All Query


        String selectQuery = String.format(Locale.US,"SELECT * FROM %s WHERE %s =?",
                                                        TABLE_MESSAGE, KEY_SENDER_ID);
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,  new String[] { String.valueOf(senderId) });
        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                //  Make sender and receiver contact
                Contact sender = getContact(Integer.parseInt
                        (cursor.getString(cursor.getColumnIndex("sender_id"))));
                Contact receiver = getContact(Integer.parseInt
                        (cursor.getString(cursor.getColumnIndex("receiver_id"))));

                Message message = new Message(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))),
                        sender,
                        receiver,
                        DateUtils.stringToDate(cursor.getString(cursor.getColumnIndex("date"))),
                        cursor.getString(cursor.getColumnIndex("text")));

                // Adding message to list
                messageList.add(message);
            } while (cursor.moveToNext());
        }

        cursor.close();
        // Return message list
        return messageList;
    }

    // Getting All Message by receiverId
    public List<Message> getAllMessageByReceiverId(int receiverId) {
        Log.d("DataBase", "Get all message from DataBase by receiverId");
        List<Message> messageList = new ArrayList<Message>();
        // Select All Query
        String selectQuery = String.format(Locale.US, "SELECT * FROM %s WHERE %s =?",
                                                                    TABLE_MESSAGE,
                                                                    KEY_RECEIVER_ID);
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,  new String[] { String.valueOf(receiverId) });
        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //  Make sender and receiver contact
                Contact sender = getContact(Integer.parseInt
                        (cursor.getString(cursor.getColumnIndex("sender_id"))));
                Contact receiver = getContact(Integer.parseInt
                        (cursor.getString(cursor.getColumnIndex("receiver_id"))));

                Message message = new Message(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))),
                        sender,
                        receiver,
                        DateUtils.stringToDate(cursor.getString(cursor.getColumnIndex("date"))),
                        cursor.getString(cursor.getColumnIndex("text")));

                // Adding message to list
                messageList.add(message);
            } while (cursor.moveToNext());
        }

        cursor.close();
        // Return message list
        return messageList;
    }

    // Getting All Message of a talk
    public List<Message> getAllMessageOfTalk(int senderId, int receiverId) {
        Log.d("DataBase", "Get all Messages of a talk by senderId and receiverId");
        List<Message> messageList = new ArrayList<Message>();
        // Select All Query
        String selectQuery = String.format(Locale.US, "SELECT * FROM %s WHERE %s =? AND %s =?",
                                                                                TABLE_MESSAGE,
                                                                                KEY_SENDER_ID,
                                                                                KEY_RECEIVER_ID);

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,  new String[] { String.valueOf(senderId), String.valueOf(receiverId) });
        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //  Make sender and receiver contact
                Contact sender = getContact(Integer.parseInt
                        (cursor.getString(cursor.getColumnIndex("sender_id"))));
                Contact receiver = getContact(Integer.parseInt
                        (cursor.getString(cursor.getColumnIndex("receiver_id"))));

                Message message = new Message(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))),
                        sender,
                        receiver,
                        DateUtils.stringToDate(cursor.getString(cursor.getColumnIndex("date"))),
                        cursor.getString(cursor.getColumnIndex("text")));

                // Adding message to list
                messageList.add(message);
            } while (cursor.moveToNext());
        }

        cursor.close();
        // Return message list
        return messageList;
    }

    // Deleting a message
    public void deleteMessage(int id) {
        Log.d("DataBase", "Delete Message by id");
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_MESSAGE, String.format(Locale.US, "%s = ?", KEY_MESSAGE_ID),
                new String[] { String.valueOf(id)});
    }

    // Deleting a talk
    public void deleteTalk(int senderId, int receiverId) {
        Log.d("DataBase", "Delete talk by senderId and receiverId");
        SQLiteDatabase db = getWritableDatabase();

        String deleteQuery = String.format(Locale.US, "%s =? AND %s =?",
                                                        KEY_SENDER_ID,
                                                        KEY_RECEIVER_ID);
        db.delete(TABLE_MESSAGE, deleteQuery,
                new String[] { String.valueOf(senderId), String.valueOf(receiverId) });
    }

    // Deleting message older 1 day
    public void deleteOldMessages() {
        Log.d("DataBase", "Delete all Messages");
        SQLiteDatabase db = getWritableDatabase();


        String sql = String.format(Locale.US, "DELETE FROM %s WHERE %s <= date('now','-1 day')",
                                                                                TABLE_MESSAGE,
                                                                                KEY_DATE);
        db.execSQL(sql);
    }

    public Contact getOwnerContact() {
        Log.d("DataBase", "Get the contact of the Owner");
        int selfId = 0;

        return getContact(selfId);
    }
}


