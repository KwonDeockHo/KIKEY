/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.example.maple.kaka.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

	private static final String TAG = SQLiteHandler.class.getSimpleName();

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "id2558345_kiki";

	// Login table name
	private static final String TABLE_VENDOR_USER = "vendor_info";
    private static final String TABLE_MEMBER_USER = "member_info";

	// Login Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_PHONE = "phone";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_NUMBER = "number";
	private static final String KEY_CORNAME = "corname";
	private static final String KEY_NAME = "name";
	private static final String KEY_BIRTH = "birth";
	private static final String KEY_GENDOR = "gendor";
	private static final String KEY_ADDRESS = "address";
	private static final String KEY_FOLLOW = "follow";

	private static final String KEY_UID = "uid";
	private static final String KEY_CREATED_AT = "created_at";
	private static final String KEY_UPDATE_AT = "update_at";


	public SQLiteHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
        Log.e("SQLiteHandler", "CreateSQLiteHandler");

		String CREATE_VENDOR_TABLE = "CREATE TABLE " + TABLE_VENDOR_USER + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_PHONE + " TEXT," + KEY_EMAIL + " TEXT UNIQUE," + KEY_NUMBER + " TEXT," + KEY_CORNAME + " TEXT," + KEY_NAME + " TEXT," + KEY_BIRTH + " TEXT,"
				+ KEY_GENDOR + " TEXT," + KEY_ADDRESS+" TEXT,"+ KEY_FOLLOW + " TEXT,"+ KEY_UID  + " TEXT," + KEY_CREATED_AT + " TEXT" + ")";

        String CREATE_MEMBER_TABLE = "CREATE TABLE " + TABLE_MEMBER_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PHONE + " TEXT," + KEY_EMAIL + " TEXT UNIQUE,"  + KEY_NAME + " TEXT," + KEY_BIRTH + " TEXT," + KEY_GENDOR + " TEXT," + KEY_ADDRESS + " TEXT,"
				+ KEY_FOLLOW+" TEXT," + KEY_UID  + " TEXT," + KEY_CREATED_AT + " TEXT" + ")";

		db.execSQL(CREATE_VENDOR_TABLE);
        db.execSQL(CREATE_MEMBER_TABLE);

		Log.d(TAG, "Database tables created");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_VENDOR_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBER_USER);

		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */

	public void addVendor(String _phone, String _email, String _number, String _corname, String _name, String _birth, String _gendor, String _address, String _follow, String uid, String created_at) {
		Log.d(TAG, "addVendor");

		SQLiteDatabase db = this.getWritableDatabase();

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_VENDOR_USER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBER_USER);

		// Create tables again
		onCreate(db);

		ContentValues values = new ContentValues();
		values.put(KEY_PHONE, _phone); // Name
		values.put(KEY_EMAIL, _email); // Name
		values.put(KEY_NUMBER, _number); // Name
		values.put(KEY_CORNAME, _corname); // Name
        values.put(KEY_NAME, _name); // Name
		values.put(KEY_BIRTH, _birth); // Name
		values.put(KEY_GENDOR, _gendor); // Email
		values.put(KEY_ADDRESS, _address); // Name
		values.put(KEY_FOLLOW, _follow); // Email

		values.put(KEY_UID, uid); // Email
		values.put(KEY_CREATED_AT, created_at); // Created At

		// Inserting Row
		long id = db.insert(TABLE_VENDOR_USER, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New VENDOR user inserted into sqlite: " + id);
	}

    public void addMember(String _phone, String _email, String _name, String _birth, String _gendor, String _address, String _follow, String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

		Log.d(TAG, "addMember");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_VENDOR_USER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBER_USER);

		// Create tables again
		onCreate(db);
        ContentValues values = new ContentValues();
        values.put(KEY_PHONE, _phone); // Name
        values.put(KEY_EMAIL, _email); // Name
        values.put(KEY_NAME, _name); // Name
        values.put(KEY_BIRTH, _birth); // Name
        values.put(KEY_GENDOR, _gendor); // Email
        values.put(KEY_ADDRESS, _address); // Name
        values.put(KEY_FOLLOW, _follow); // Email

        values.put(KEY_UID, uid); // Email
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(TABLE_MEMBER_USER, null, values);

		db.close(); // Closing database connection

        Log.d(TAG, "New MEMBER user inserted into sqlite: " + id);
    }
	public void updateMember(String _phone, String _email, String _name, String _birth, String _gendor, String _address, String _follow, String uid) {
		SQLiteDatabase db = this.getWritableDatabase();

		Log.d(TAG, "updateMember");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_VENDOR_USER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBER_USER);

		// Create tables again
		onCreate(db);
		ContentValues values = new ContentValues();
		values.put(KEY_PHONE, _phone); // Name
		values.put(KEY_EMAIL, _email); // Name
		values.put(KEY_NAME, _name); // Name
		values.put(KEY_BIRTH, _birth); // Name
		values.put(KEY_GENDOR, _gendor); // Email
		values.put(KEY_ADDRESS, _address); // Name
		values.put(KEY_FOLLOW, _follow); // Email

		values.put(KEY_UID, uid); // Email

		// Inserting Row
		long id = db.insert(TABLE_MEMBER_USER, null, values);

		db.close(); // Closing database connection

		Log.d(TAG, "Update MEMBER user inserted into sqlite: " + id);
	}
	public void updateVendor(String _phone, String _email, String _number, String _corname, String _name, String _birth, String _gendor, String _address, String _follow, String uid) {
		SQLiteDatabase db = this.getWritableDatabase();

		Log.d(TAG, "updateVendor");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_VENDOR_USER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBER_USER);

		// Create tables again
		onCreate(db);
		ContentValues values = new ContentValues();
		values.put(KEY_PHONE, _phone); // Name
		values.put(KEY_EMAIL, _email); // Name
		values.put(KEY_NUMBER, _number); // Name
		values.put(KEY_CORNAME, _corname); // Name
		values.put(KEY_NAME, _name); // Name
		values.put(KEY_BIRTH, _birth); // Name
		values.put(KEY_GENDOR, _gendor); // Email
		values.put(KEY_ADDRESS, _address); // Name
		values.put(KEY_FOLLOW, _follow); // Email

		values.put(KEY_UID, uid); // Email

		// Inserting Row
		long id = db.insert(TABLE_VENDOR_USER, null, values);

		db.close(); // Closing database connection

		Log.d(TAG, "Update VENDOR user inserted into sqlite: " + id);
	}
	/**
	 * Getting user data from database
	 * */
	public HashMap<String, String> getVendorDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_VENDOR_USER;
		Log.d(TAG, "HashMap_Vendor");

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			user.put("phone", cursor.getString(1));
			user.put("email", cursor.getString(2));
			user.put("number", cursor.getString(3));
			user.put("corname", cursor.getString(4));
			user.put("name", cursor.getString(5));
			user.put("birth", cursor.getString(6));
			user.put("gendor", cursor.getString(7));
			user.put("address", cursor.getString(8));
			user.put("follow", cursor.getString(9));
			user.put("uid", cursor.getString(10));
			user.put("created_at", cursor.getString(11));
		}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

		return user;
	}
    public HashMap<String, String> getMemberDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_MEMBER_USER;
		Log.d(TAG, "HashMap_Member");

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("phone", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("name", cursor.getString(3));
            user.put("birth", cursor.getString(4));
            user.put("gendor", cursor.getString(5));
            user.put("address", cursor.getString(6));
            user.put("follow", cursor.getString(7));
            user.put("uid", cursor.getString(8));
            user.put("created_at", cursor.getString(9));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }
	/**
	 * Re crate database Delete all tables and create them again
	 * */
	public void deleteUsers() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
        db.delete(TABLE_VENDOR_USER, null, null);
        db.delete(TABLE_MEMBER_USER, null, null);
		String DELETE_VENDOR_TABLE = "DROP TABLE IF EXISTS "+TABLE_VENDOR_USER;
        String DELETE_MEMBER_TABLE = "DROP TABLE IF EXISTS "+TABLE_MEMBER_USER;

        db.execSQL(DELETE_VENDOR_TABLE);
        db.execSQL(DELETE_MEMBER_TABLE);

        db.close();
		Log.d(TAG, "DeleteUsers");

		Log.d(TAG, "Deleted all user info from sqlite");
	}

}
