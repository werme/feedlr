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
	private static final String ITEM_COLUMN_URL = "URL";
	private static final String ITEM_COLUMN_IMGURL = "imgURL";
	private static final String ITEM_COLUMN_USER_ID = "user_ID";

	public databaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase database) {
		// @formatter:off
		// Creating feed table
		database.execSQL("CREATE TABLE " + TABLE_FEED + "(" 
				+ FEED_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ FEED_COLUMN_NAME + " TEXT UNIQUE" + ")");

		// Creating feed-user bridge table
		database.execSQL("CREATE TABLE " + TABLE_FEEDUSER + "("
				+ FEEDUSER_COLUMN_FEED_ID + " INT NOT NULL,"
				+ FEEDUSER_COLUMN_USER_ID + " INT NOT NULL" + ")");

		// Creating user table
		// TODO Should username be the unique idenifier of a user?!
		database.execSQL("CREATE TABLE " + TABLE_USER + "(" 
				+ USER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ USER_COLUMN_USERNAME + " TEXT NOT NULL," 
				+ USER_COLUMN_USERID + " TEXT UNIQUE,"
				+ USER_COLUMN_IMGURL + " TEXT," 
				+ USER_COLUMN_SOURCE + " TEXT NOT NULL" + ")");

		// Creating item table
		database.execSQL("CREATE TABLE " + TABLE_ITEM + "(" + 
				ITEM_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ ITEM_COLUMN_TEXT + " TEXT," 
				+ ITEM_COLUMN_TIMESTAMP + " TEXT,"
				+ ITEM_COLUMN_TYPE + " TEXT," 
				+ ITEM_COLUMN_URL + " TEXT,"
				+ ITEM_COLUMN_IMGURL + " TEXT," 
				+ ITEM_COLUMN_USER_ID + " INT NOT NULL" + ")");
		// @formatter:on
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
		try {
			db.insertOrThrow(TABLE_FEED, null, temp);
		} catch (SQLiteConstraintException e) {
			Log.d("ERROR", "Inserted feed is not UNIQUE!");
			// TODO Apply listener to notify the user that the feed name already
			// exists!
		}

		db.close();
	}

	public void removeFeed(Feed feed) {
		String title = feed.getTitle();
		long id = getFeedID(feed);

		removeFeedBridge(id);

		SQLiteDatabase db = this.getWritableDatabase();

		db.delete(TABLE_FEED, FEED_COLUMN_NAME + "=?", new String[] { title });
		db.close();
	}

	public ArrayList<String> listFeeds() {
		final ArrayList<String> feeds = new ArrayList<String>();

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_FEED, null);
		while (c.moveToNext()) {
			String s = c.getString(1);
			feeds.add(s);
		}
		c.close();
		db.close();
		return feeds;
	}

	public long getFeedID(Feed feed) {
		String feedTitle = feed.getTitle();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.query(TABLE_FEED, new String[] { FEED_COLUMN_ID },
				FEED_COLUMN_NAME + "=?", new String[] { feedTitle }, null,
				null, null);
		c.moveToNext();
		Long id = Long.parseLong(c.getString(0));
		c.close();
		db.close();
		return id;
	}

	public long getUserID(User user) {
		long id = user.getID();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.query(TABLE_USER, new String[] { USER_COLUMN_ID },
				USER_COLUMN_ID + "=?", new String[] { id + "" }, null, null,
				null);
		c.moveToNext();
		Long id1 = Long.parseLong(c.getString(0));
		c.close();
		db.close();
		return id1;
	}

	public ArrayList<String> listUsers() {
		final ArrayList<String> users = new ArrayList<String>();

		SQLiteDatabase db = this.getWritableDatabase();

		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_USER, null);

		while (c.moveToNext()) {
			String s = c.getString(1);
			users.add(s);
		}
		db.close();
		return users;
	}

	public void addUserToFeed(User user, Feed feed) {
		long FeedID = getFeedID(feed);
		long UserID = addUser(user);

		// Add bridge connection
		if (UserID != -1) {
			addFeedUserBridge(FeedID, UserID);
		}
	}

	public long addUser(User user) {
		// TODO Check if the user already exists!!
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues temp = new ContentValues();

		temp.put(USER_COLUMN_USERNAME, user.getUserName());
		temp.put(USER_COLUMN_USERID, user.getID());
		temp.put(USER_COLUMN_IMGURL, user.getProfileImageURL());
		// TODO implement source on user?

		long userID = db.insert(TABLE_USER, null, temp);

		db.close();
		return userID;
	}

	public void addFeedUserBridge(long feedID, long userID) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues temp = new ContentValues();

		temp.put(FEEDUSER_COLUMN_FEED_ID, feedID);
		temp.put(FEEDUSER_COLUMN_USER_ID, userID);

		db.insert(TABLE_FEEDUSER, null, temp);

		db.close();
	}

	public void removeUserFromFeed(Feed feed, User user) {
		long feedID = getFeedID(feed);
		long userID = getUserID(user);

		removeFeedUserBridge(feedID, userID);
	}

	private void removeUser(User user) {
		SQLiteDatabase db = this.getWritableDatabase();
		long id = user.getID();
		db.delete(TABLE_USER, USER_COLUMN_ID + "=?", new String[] { id + "" });
		db.close();
	}

	private void removeFeedUserBridge(long feedID, long userID) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_FEEDUSER, FEEDUSER_COLUMN_FEED_ID + "=?" + " and "
				+ FEEDUSER_COLUMN_USER_ID + "=?", new String[] { feedID + "",
				userID + "" });
		db.close();
	}

	public ArrayList<String> listFeedUser() {
		final ArrayList<String> feeduser = new ArrayList<String>();

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_FEEDUSER, null);
		while (c.moveToNext()) {
			feeduser.add(c.getString(0) + " " + c.getString(1));
		}
		c.close();
		db.close();
		return feeduser;
	}

	private void removeFeedBridge(Long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_FEEDUSER, FEEDUSER_COLUMN_FEED_ID + "=?",
				new String[] { id + "" });
		db.close();
	}

	public void updateUser(long userID) {
		// TODO implement this method
	}

	public void addListOfItems(List<Item> itemList) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		for (Item i : itemList) {

			ContentValues temp = new ContentValues();
			temp.put(ITEM_COLUMN_TEXT, i.getText());
			temp.put(ITEM_COLUMN_TIMESTAMP, i.getTimestamp());
			temp.put(ITEM_COLUMN_TYPE, i.getText());
			temp.put(ITEM_COLUMN_URL, i.getURL());
			temp.put(ITEM_COLUMN_IMGURL, i.getIMGURL());
			temp.put(ITEM_COLUMN_USER_ID, i.getUser().getID());
			db.insert(TABLE_ITEM, null, temp);

		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}

	public Cursor getAllItems() {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c = db.query(TABLE_ITEM, new String[] { ITEM_COLUMN_TEXT,
				ITEM_COLUMN_TIMESTAMP, ITEM_COLUMN_TYPE, ITEM_COLUMN_URL,
				ITEM_COLUMN_IMGURL, ITEM_COLUMN_USER_ID }, null, null, null, null, null);
		db.close();
		return c;
	}
}
