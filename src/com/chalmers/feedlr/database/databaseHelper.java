/**
 * Class description
 * 
 * @user David Goransson
 */

package com.chalmers.feedlr.database;

import java.util.ArrayList;
import java.util.List;

import com.chalmers.feedlr.model.Feed;
import com.chalmers.feedlr.model.Item;
import com.chalmers.feedlr.model.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class databaseHelper extends SQLiteOpenHelper {

	// Database static variables
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "feedlrDatabase";

	// Declaring feed table
	private static final String TABLE_FEED = "feed";
	private static final String FEED_COLUMN_ID = "_id";
	private static final String FEED_COLUMN_NAME = "name";

	// Declaring feed-user bridge table
	private static final String TABLE_FEEDUSER = "feeduser";
	private static final String FEEDUSER_COLUMN_FEED_ID = "feed_ID";
	private static final String FEEDUSER_COLUMN_USER_ID = "user_ID";

	// Declaring user table
	private static final String TABLE_USER = "user";
	private static final String USER_COLUMN_ID = "_id";
	private static final String USER_COLUMN_USERNAME = "username";
	private static final String USER_COLUMN_USERID = "userid";
	private static final String USER_COLUMN_IMGURL = "ProfileImageURL";
	private static final String USER_COLUMN_SOURCE = "source";

	// Declaring item table
	private static final String TABLE_ITEM = "item";
	private static final String ITEM_COLUMN_ID = "_id";
	private static final String ITEM_COLUMN_TEXT = "text";
	private static final String ITEM_COLUMN_TIMESTAMP = "timestamp";
	private static final String ITEM_COLUMN_TYPE = "type";
	private static final String ITEM_COLUMN_USER_ID = "user_ID";

	public databaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase database) {

		// Creating feed table
		database.execSQL("CREATE TABLE " + TABLE_FEED + "(" + FEED_COLUMN_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + FEED_COLUMN_NAME
				+ " TEXT UNIQUE" + ")");

		// Creating feed-user bridge table
		database.execSQL("CREATE TABLE " + TABLE_FEEDUSER + "("
				+ FEEDUSER_COLUMN_FEED_ID + " INT NOT NULL,"
				+ FEEDUSER_COLUMN_USER_ID + " INT NOT NULL" + ")");

		// Creating user table
		database.execSQL("CREATE TABLE " + TABLE_USER + "(" + USER_COLUMN_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_COLUMN_USERNAME
				+ " TEXT," + USER_COLUMN_USERID + " TEXT," + USER_COLUMN_IMGURL
				+ " TEXT," + USER_COLUMN_SOURCE + " TEXT" + ")");

		// Creating item table
		database.execSQL("CREATE TABLE " + TABLE_ITEM + "(" + ITEM_COLUMN_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + ITEM_COLUMN_TEXT
				+ " TEXT," + ITEM_COLUMN_TIMESTAMP + " TEXT,"
				+ ITEM_COLUMN_TYPE + " TEXT," + ITEM_COLUMN_USER_ID
				+ " INT NOT NULL" + ")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Temporarily drops all tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEED);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDUSER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
		onCreate(db);
	}

	public void addFeed(Feed feed) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues temp = new ContentValues();
		
		temp.put(FEED_COLUMN_NAME, feed.getTitle());
		try{
		db.insertOrThrow(TABLE_FEED, null, temp);
		} catch (SQLiteConstraintException e){
			Log.d("ERROR", "Inserted feed is not UNIQUE!");
			//TODO Apply listener to notify the user that the feed name already exists!
		}
		
		db.close();
	}

	public void removeFeed(String title) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_FEED, FEED_COLUMN_NAME + "=?", new String[] { title });
		db.close();
	}

	public ArrayList<String> listFeeds() {
		final ArrayList<String> feeds = new ArrayList<String>();

		SQLiteDatabase DB = this.getWritableDatabase();
		Cursor c = DB.rawQuery("SELECT * FROM " + TABLE_FEED, null);
		while (c.moveToNext()) {
			String s = c.getString(1);
			feeds.add(s);
		}
		return feeds;
	}
}
