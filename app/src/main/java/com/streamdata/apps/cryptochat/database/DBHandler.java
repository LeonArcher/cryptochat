package com.streamdata.apps.cryptochat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.streamdata.apps.cryptochat.ApplicationContext;
import com.streamdata.apps.cryptochat.cryptography.Cryptographer;
import com.streamdata.apps.cryptochat.cryptography.CryptographerException;
import com.streamdata.apps.cryptochat.cryptography.CryptographerFactory;
import com.streamdata.apps.cryptochat.cryptography.RSACryptographerFactory;
import com.streamdata.apps.cryptochat.models.Contact;
import com.streamdata.apps.cryptochat.models.Message;
import com.streamdata.apps.cryptochat.utils.DataBaseUtils;
import com.streamdata.apps.cryptochat.utils.DataIcon;
import com.streamdata.apps.cryptochat.utils.DateUtils;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class DBHandler extends SQLiteOpenHelper {

    public static final String DB_LOG_TAG = "Database";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "DB";

    // Table name
    private static final String TABLE_CONTACT = "Contact";
    private static final String TABLE_MESSAGE = "Message";

    // Table Contacts names
    private static final String KEY_CONTACT_ID = "id";
    private static final String KEY_SERVER_ID = "server_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ICON= "icon";
    private static final String KEY_PUBLIC_KEY= "public_key";
    private static final String KEY_CRYPTOGRAPHER= "cryptographer";

    // Table Messages names
    private static final String KEY_MESSAGE_ID = "id";
    private static final String KEY_TEXT = "text";
    private static final String KEY_DATE = "date";
    private static final String KEY_SENDER_ID = "sender_id";
    private static final String KEY_RECEIVER_ID = "receiver_id";

    // Table Create Statements

    // Contact table create statement
    private static final String CREATE_TABLE_CONTACT = String.format(
            "CREATE TABLE %s ( %s  INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s  TEXT," +
                    " %s BLOB, %s TEXT, %s BLOB)",
            TABLE_CONTACT,
            KEY_CONTACT_ID,
            KEY_SERVER_ID,
            KEY_NAME,
            KEY_ICON,
            KEY_PUBLIC_KEY,
            KEY_CRYPTOGRAPHER
    );

    // Message table create statement
    private static final String CREATE_TABLE_MESSAGE = String.format(
            "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s  TEXT, %s  DATETIME," +
                    " %s INTEGER, %s INTEGER )",
            TABLE_MESSAGE,
            KEY_MESSAGE_ID,
            KEY_TEXT,
            KEY_DATE,
            KEY_SENDER_ID,
            KEY_RECEIVER_ID
    );

    // self contact id in database
    private static final int SELF_CONTACT_ID = 0;

    // database singleton implementation
    private static DBHandler instance = null;

    public static synchronized DBHandler getInstance() {
        if (instance == null) {
            instance = new DBHandler(ApplicationContext.getContext());
        }
        return instance;
    }

    @WorkerThread
    private DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @WorkerThread
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(DB_LOG_TAG, "Create DataBase");
        // Creating required tables
        db.execSQL(CREATE_TABLE_CONTACT);
        db.execSQL(CREATE_TABLE_MESSAGE);

        Log.d(DB_LOG_TAG, "DataBase has been created");
    }

    @WorkerThread
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(DB_LOG_TAG, "Upgrade DataBase");

        if (newVersion <= oldVersion) {
            String exceptionMessage = String.format(Locale.US,"%d <= %d", newVersion, oldVersion);
            IllegalArgumentException ex = new IllegalArgumentException(exceptionMessage);
            Log.e(DB_LOG_TAG, null, ex);
            throw ex;
        }

        // On upgrade drop older tables
        db.execSQL(String.format(Locale.US, "DROP TABLE IF EXISTS %s", TABLE_CONTACT));
        db.execSQL(String.format(Locale.US, "DROP TABLE IF EXISTS %s", TABLE_MESSAGE));

        // Create new tables
        onCreate(db);
    }

    // Adding new Contact
    @WorkerThread
    public synchronized void addContact(Contact contact) {
        Log.d(DB_LOG_TAG, "Add Contact to DataBase");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = contactToContentValues(contact);

        // Inserting Row
        db.insert(TABLE_CONTACT, null, values);
    }

    // Getting Contact by id
    @WorkerThread
    @Nullable
    public synchronized Contact getContact(int id) throws CryptographerException{
        Log.d(DB_LOG_TAG, "Get Contact from DataBase by id");
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_CONTACT,
                new String[] { KEY_CONTACT_ID, KEY_SERVER_ID, KEY_NAME, KEY_ICON, KEY_PUBLIC_KEY,
                        KEY_CRYPTOGRAPHER },
                String.format(Locale.US, "%s  =? ", KEY_CONTACT_ID),
                new String[] { String.valueOf(id) },
                null,
                null,
                null,
                null
        );

        if (!cursor.moveToFirst()) {
            return null;
        }

        Contact contact = cursorToContact(cursor);
        cursor.close();

        return contact;
    }

    @WorkerThread
    @Nullable
    public synchronized Contact getContactByServerId(String serverId) throws CryptographerException {
        Log.d(DB_LOG_TAG, "Get Contact from DataBase by id");
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_CONTACT,
                new String[] { KEY_CONTACT_ID, KEY_SERVER_ID, KEY_NAME, KEY_ICON, KEY_PUBLIC_KEY,
                        KEY_CRYPTOGRAPHER },
                String.format(Locale.US, "%s  =? ", KEY_SERVER_ID),
                new String[] { serverId },
                null,
                null,
                null,
                null
        );

        if (!cursor.moveToFirst()) {
            return null;
        }

        Contact contact = cursorToContact(cursor);
        cursor.close();

        return contact;
    }


    public synchronized List<Contact> getAllContacts() throws CryptographerException {

        Log.d(DB_LOG_TAG, "Get all Contacts from DataBase");
        List<Contact> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = String.format(Locale.US, "SELECT * FROM %s", TABLE_CONTACT);

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {});

        if (!cursor.moveToFirst()) {
            return null;
        }

        // Looping through all rows and adding to list
        do {
            Contact contact = cursorToContact(cursor);

            // Adding contact to list (if not self contact)
            if (contact.getId() != SELF_CONTACT_ID) {
                contactList.add(contact);
            }
        } while (cursor.moveToNext());

        cursor.close();
        // Return contact list
        return contactList;
    }

    public synchronized Contact getOwnerContact() throws CryptographerException{

        Log.d(DB_LOG_TAG, "Get the contact of the Owner");

        return getContact(SELF_CONTACT_ID);
    }

    public synchronized void setOwnerContact(Contact contact)throws CryptographerException {

        Log.d(DB_LOG_TAG, "Set the contact of the Owner");

        Contact newOwnerContact = new Contact(
                SELF_CONTACT_ID,
                contact.getServerId(),
                contact.getName(),
                contact.getIcon(),
                contact.getPublicKey(),
                null
        );

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = contactToContentValues(newOwnerContact);

        // delete old owner contact if already exists
        if (getOwnerContact() != null) {
            deleteContact(SELF_CONTACT_ID);
        }

        // Inserting Row
        db.insert(TABLE_CONTACT, null, values);
    }

    // Deleting a contact
    @WorkerThread
    public synchronized void deleteContact(int id) {
        Log.d(DB_LOG_TAG, "Delete Contact by id");
        SQLiteDatabase db = getWritableDatabase();

        db.delete(
                TABLE_CONTACT,
                String.format(Locale.US, "%s = ?", KEY_CONTACT_ID),
                new String[] { String.valueOf(id)}
        );
    }

    // Adding new Message
    @WorkerThread
    public synchronized void addMessage(Message message) {
        Log.d(DB_LOG_TAG, "Add Message to DataBase");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = messageToContentValues(message);

        // Inserting Row
        db.insert(TABLE_MESSAGE, null, values);
    }

    // Getting one message
    @WorkerThread
    @Nullable
    public synchronized Message getMessage(int id) throws SQLDataException {
        Log.d(DB_LOG_TAG, "Get Message from DataBase by id");
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_MESSAGE,
                new String[] { KEY_MESSAGE_ID, KEY_SENDER_ID, KEY_RECEIVER_ID, KEY_DATE, KEY_TEXT },
                String.format(Locale.US,"%s  =?", KEY_MESSAGE_ID),
                new String[] { String.valueOf(id) },
                null,
                null,
                null,
                null
        );

        if (!cursor.moveToFirst()) {
            return null;
        }

        //  Make sender and receiver contact
        Message message;

        try {
            message = cursorToMessage(cursor);
        } catch (ParseException ex) {
            throw new SQLDataException(ex);
        } finally {
            cursor.close();
        }

        return message;
    }

    // Getting All Messages by senderId
    @WorkerThread
    @Nullable
    public synchronized List<Message> getAllMessagesBySenderId(int senderId)
            throws SQLDataException {
        Log.d(DB_LOG_TAG, "Get all messages from DataBase by senderId");
        List<Message> messageList = new ArrayList<>();

        // Select All Queries
        String selectQuery = String.format(
                Locale.US,
                "SELECT * FROM %s WHERE %s =?",
                TABLE_MESSAGE,
                KEY_SENDER_ID
        );

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,  new String[] { String.valueOf(senderId) });

        if (!cursor.moveToFirst()) {
            return null;
        }

        try {
            // Looping through all rows and adding to list
            do {
                Message message = cursorToMessage(cursor);
                // Adding message to list
                messageList.add(message);
            } while (cursor.moveToNext());
        } catch (ParseException ex) {
            throw new SQLDataException(ex);
        } finally {
            cursor.close();
        }

        // Return message list
        return messageList;
    }

    // Getting All Messages by receiverId
    @WorkerThread
    @Nullable
    public synchronized List<Message> getAllMessagesByReceiverId(int receiverId)
            throws SQLDataException {

        Log.d(DB_LOG_TAG, "Get all message from DataBase by receiverId");
        List<Message> messageList = new ArrayList<>();

        // Select All Query
        String selectQuery = String.format(
                Locale.US, "SELECT * FROM %s WHERE %s =?",
                TABLE_MESSAGE,
                KEY_RECEIVER_ID
        );

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,  new String[] { String.valueOf(receiverId) });

        if (!cursor.moveToFirst()) {
            return null;
        }

        try {
            // Looping through all rows and adding to list
            do {
                Message message = cursorToMessage(cursor);
                // Adding message to list
                messageList.add(message);
            } while (cursor.moveToNext());
        } catch (ParseException ex) {
            throw new SQLDataException(ex);
        } finally {
            cursor.close();
        }

        // Return message list
        return messageList;
    }

    // Getting Last Message by senderId
    @WorkerThread
    @Nullable
    public synchronized Message getLastMessageBySenderId(int senderId) throws SQLDataException {
        Log.d(DB_LOG_TAG, "Get last message from DataBase by senderId");

        // select last message query
        String selectQuery = String.format(
                Locale.US, "SELECT * FROM %s WHERE %s =? ORDER BY %s DESC LIMIT 1",
                TABLE_MESSAGE,
                KEY_SENDER_ID,
                KEY_MESSAGE_ID
        );

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,  new String[] { String.valueOf(senderId) });

        if (!cursor.moveToFirst()) {
            return null;
        }

        // get the message
        Message message;

        try {
            message = cursorToMessage(cursor);
        } catch (ParseException ex) {
            throw new SQLDataException(ex);
        } finally {
            cursor.close();
        }

        return message;
    }

    // Getting All Messages of a talk
    @WorkerThread
    @Nullable
    public synchronized List<Message> getAllMessagesOfTalk(int contactId) throws SQLDataException {
        Log.d(DB_LOG_TAG, "Get all Messages of a talk by senderId and receiverId");
        List<Message> messageList = new ArrayList<>();

        // Select All Query
        String selectQuery = String.format(
                Locale.US, "SELECT * FROM %s WHERE %s =? OR %s =?",
                TABLE_MESSAGE,
                KEY_SENDER_ID,
                KEY_RECEIVER_ID
        );

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(
                selectQuery,
                new String[] { String.valueOf(contactId), String.valueOf(contactId) }
        );

        if (!cursor.moveToFirst()) {
           return null;
        }

        try {
            // Looping through all rows and adding to list
            do {
                Message message = cursorToMessage(cursor);
                // Adding message to list
                messageList.add(message);
            } while (cursor.moveToNext());
        } catch (ParseException ex) {
            throw new SQLDataException(ex);
        } finally {
            cursor.close();
        }

        // Return message list
        return messageList;
    }

    // Getting All Messages
    @WorkerThread
    @Nullable
    public synchronized List<Message> getAllMessages() throws SQLDataException {
        Log.d(DB_LOG_TAG, "Get all message from DataBase");
        List<Message> messageList = new ArrayList<>();
        // Select All Query
        String selectQuery = String.format(Locale.US, "SELECT * FROM %s", TABLE_MESSAGE);

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {  });

        if (!cursor.moveToFirst()) {
            return null;
        }

        try {
            // Looping through all rows and adding to list
            do {
                Message message = cursorToMessage(cursor);
                // Adding message to list
                messageList.add(message);
            } while (cursor.moveToNext());
        } catch (ParseException ex) {
            throw new SQLDataException(ex);
        } finally {
            cursor.close();
        }

        // Return message list
        return messageList;
    }

    // Deleting a message
    @WorkerThread
    public synchronized void deleteMessage(int id) {
        Log.d(DB_LOG_TAG, "Delete Message by id");
        SQLiteDatabase db = getWritableDatabase();
        db.delete(
                TABLE_MESSAGE,
                String.format(Locale.US, "%s = ?", KEY_MESSAGE_ID),
                new String[] { String.valueOf(id)}
        );
    }

    // Deleting a talk
    @WorkerThread
    public synchronized void deleteTalk(int senderId, int receiverId) {
        Log.d(DB_LOG_TAG, "Delete talk by senderId and receiverId");
        SQLiteDatabase db = getWritableDatabase();

        String deleteQuery = String.format(
                Locale.US,
                "%s =? AND %s =?",
                KEY_SENDER_ID,
                KEY_RECEIVER_ID
        );
        db.delete(
                TABLE_MESSAGE,
                deleteQuery,
                new String[] { String.valueOf(senderId), String.valueOf(receiverId) }
        );
    }

    // Deleting messages older than 1 day
    @WorkerThread
    public synchronized void deleteOldMessages() {
        Log.d(DB_LOG_TAG, "Delete all Messages");
        SQLiteDatabase db = getWritableDatabase();

        String sql = String.format(
                Locale.US, "DELETE FROM %s WHERE %s <= date('now','-1 day')",
                TABLE_MESSAGE,
                KEY_DATE
        );
        db.execSQL(sql);
    }

    private static ContentValues contactToContentValues(Contact contact) {

        ContentValues values = new ContentValues();
        // Add fields to ContentValues
        values.put(KEY_CONTACT_ID, contact.getId());
        values.put(KEY_SERVER_ID, contact.getServerId());
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PUBLIC_KEY, contact.getPublicKey());

        values.put(KEY_ICON, DataBaseUtils.iconToBytes(contact.getIcon()));
        values.put(KEY_CRYPTOGRAPHER, DataBaseUtils.cryptographerToBytes(contact.getCryptographer()));

        return values;
    }

    private static Contact cursorToContact(Cursor cursor) throws CryptographerException {
//        Serialize icon
        DataIcon icon = new DataIcon();
        icon.create(cursor.getBlob(cursor.getColumnIndex(KEY_ICON)));
//        Serialize cryptographer
        CryptographerFactory cryptographerFactory = new RSACryptographerFactory();
        byte[] blob =  cursor.getBlob(cursor.getColumnIndex(KEY_CRYPTOGRAPHER));
        Cryptographer cryptographer = cryptographerFactory.create(blob);


        return new Contact(
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_CONTACT_ID))),
                cursor.getString(cursor.getColumnIndex(KEY_SERVER_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                icon,
                cursor.getString(cursor.getColumnIndex(KEY_PUBLIC_KEY)),
                cryptographer
        );
    }

    private static ContentValues messageToContentValues(Message message) {
        ContentValues values = new ContentValues();
        values.put(KEY_SENDER_ID, message.getSenderId());
        values.put(KEY_RECEIVER_ID, message.getReceiverId());
        values.put(KEY_DATE, DateUtils.dateToString(message.getDate()));
        values.put(KEY_TEXT, message.getText());

        return values;
    }

    private static Message cursorToMessage(Cursor cursor) throws ParseException {
        return new Message(
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_MESSAGE_ID))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_SENDER_ID))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_RECEIVER_ID))),
                DateUtils.stringToDate(cursor.getString(cursor.getColumnIndex(KEY_DATE))),
                cursor.getString(cursor.getColumnIndex(KEY_TEXT))
        );
    }
}
