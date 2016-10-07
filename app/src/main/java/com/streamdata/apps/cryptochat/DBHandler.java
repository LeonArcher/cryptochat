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
    private static final String CREATE_TABLE_CONTACT = "CREATE TABLE "
            + TABLE_CONTACT + "(" + KEY_CONTACT_ID + " INTEGER PRIMARY KEY," + KEY_NAME
            + " TEXT," + KEY_ICON + " BLOB," + KEY_PUBLIC_KEY
            + " TEXT" + ")";

    // Message table create statement
    private static final String CREATE_TABLE_MESSAGE = "CREATE TABLE "
            + TABLE_MESSAGE + "(" + KEY_MESSAGE_ID + " INTEGER PRIMARY KEY," + KEY_TEXT
            + " TEXT," + KEY_DATE + "  DATETIME," + KEY_SENDER_ID
            + " INTEGER," + KEY_RECEIVER_ID + " INTEGER" + ")";

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
            IllegalArgumentException ex = new IllegalArgumentException(newVersion + " <= " + oldVersion);
            Log.e("DataBase", "", ex);
            throw ex;
        }

        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);

        // create new tables
        onCreate(db);
    }

    // Adding new Contact
    public void addContact(Contact contact) {
        Log.d("DataBase", "Add Contact to DataBase");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CONTACT_ID, contact.getId());
        values.put(KEY_NAME, contact.getName());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        contact.getIconBitmap().compress(Bitmap.CompressFormat.PNG, 0, bos);
        values.put(KEY_ICON, bos.toByteArray());
        values.put(KEY_PUBLIC_KEY, contact.getPublicKey());

        // Inserting Row
        db.insert(TABLE_CONTACT, null, values);
        db.close(); // Closing database connection
    }

    // Getting Contact by id
    public Contact getContact(int id) {
        Log.d("DataBase", "Get Contact from DataBase by id");
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACT, new String[] { KEY_CONTACT_ID,
                        KEY_NAME, KEY_ICON, KEY_PUBLIC_KEY}, KEY_CONTACT_ID + " =? ",
                        new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        // Achtung! Посмтореть как правильно обработать

        Contact contact = new Contact(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))),
                "Achtung",
                cursor.getString(cursor.getColumnIndex("name")),
                new DataIcon(cursor.getBlob(cursor.getColumnIndex("icon"))),
                cursor.getString(cursor.getColumnIndex("public_key")));

        cursor.close();
        db.close();

        return contact;
    }

    // Adding new Message
    public void addMessage(Message message) {
        Log.d("DataBase", "Add Message to DataBase");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_MESSAGE_ID, message.getId());
        values.put(KEY_SENDER_ID, message.getSender().getId());
        values.put(KEY_RECEIVER_ID, message.getReceiver().getId());
        values.put(KEY_DATE, DateUtils.dateToString(message.getDate()));
        values.put(KEY_TEXT, message.getText());

        // Inserting Row
        db.insert(TABLE_MESSAGE, null, values);
        db.close(); // Closing database connection
    }

    // Getting one message
    public Message getMessage(int id) {
        Log.d("DataBase", "Get Message from DataBase by id");
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MESSAGE, new String[] { KEY_MESSAGE_ID,
                        KEY_SENDER_ID, KEY_RECEIVER_ID, KEY_DATE, KEY_TEXT}, KEY_MESSAGE_ID + " =? ",
                        new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        // Achtung! Посмтореть как правильно обработать

        //  Make sender and receiver contact
        Contact sender = this.getContact(Integer.parseInt
                (cursor.getString(cursor.getColumnIndex("sender_id"))));
        Contact receiver = this.getContact(Integer.parseInt
                (cursor.getString(cursor.getColumnIndex("receiver_id"))));

        Message message = new Message(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))),
                sender,
                receiver,
                DateUtils.stringToDate(cursor.getString(cursor.getColumnIndex("date"))),
                cursor.getString(cursor.getColumnIndex("text")));

        cursor.close();
        db.close();
        return message;
    }

    // Getting All Message by senderId
    public List<Message> getAllMessageBySenderId(int senderId) {
        Log.d("DataBase", "Get all messages from DataBase by senderId");
        List<Message> messageList = new ArrayList<Message>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MESSAGE + " WHERE " +
                KEY_SENDER_ID + " =? ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,  new String[] { String.valueOf(senderId) });
        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                //  Make sender and receiver contact
                Contact sender = this.getContact(Integer.parseInt
                        (cursor.getString(cursor.getColumnIndex("sender_id"))));
                Contact receiver = this.getContact(Integer.parseInt
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
        db.close();
        // Return message list
        return messageList;
    }

    // Getting All Message by receiverId
    public List<Message> getAllMessageByReceiverId(int receiverId) {
        Log.d("DataBase", "Get all message from DataBase by receiverId");
        List<Message> messageList = new ArrayList<Message>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MESSAGE + " WHERE " +
                KEY_RECEIVER_ID + "=?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,  new String[] { String.valueOf(receiverId) });
        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //  Make sender and receiver contact
                Contact sender = this.getContact(Integer.parseInt
                        (cursor.getString(cursor.getColumnIndex("sender_id"))));
                Contact receiver = this.getContact(Integer.parseInt
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
        db.close();
        // Return message list
        return messageList;
    }

    // Getting All Message of a talk
    public List<Message> getAllMessageOfTalk(int senderId, int receiverId) {
        Log.d("DataBase", "Get all Messages of a talk by senderId and receiverId");
        List<Message> messageList = new ArrayList<Message>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MESSAGE + " WHERE " +
                KEY_SENDER_ID + "=? AND " +
                KEY_RECEIVER_ID + "=?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,  new String[] { String.valueOf(senderId), String.valueOf(receiverId) });
        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //  Make sender and receiver contact
                Contact sender = this.getContact(Integer.parseInt
                        (cursor.getString(cursor.getColumnIndex("sender_id"))));
                Contact receiver = this.getContact(Integer.parseInt
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
        db.close();
        // Return message list
        return messageList;
    }

    // Deleting a message
    public void deleteMessage(int id) {
        Log.d("DataBase", "Delete Message by id");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MESSAGE, KEY_MESSAGE_ID + " = ?",
                new String[] { String.valueOf(id)});
        db.close();
    }

    // Deleting a talk
    public void deleteTalk(int senderId, int receiverId) {
        Log.d("DataBase", "Delete talk by sender and receiverId");
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteQuery = KEY_SENDER_ID + "=? AND " + KEY_RECEIVER_ID + "=?";
        db.delete(TABLE_MESSAGE, deleteQuery,
                new String[] { String.valueOf(senderId), String.valueOf(receiverId) });

        db.close();
    }

    // Deleting message older 1 day
    public void deleteOldMessages() {
        Log.d("DataBase", "Delete all Messages");
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "DELETE FROM " + TABLE_MESSAGE + " WHERE " + KEY_DATE + " <= date('now','-1 day')";
        db.execSQL(sql);

        db.close();
    }

    public Contact getOwnerContact() {
        Log.d("DataBase", "Get contact of Owner");
        int selfId = 0;

        return getContact(selfId);
    }
}


