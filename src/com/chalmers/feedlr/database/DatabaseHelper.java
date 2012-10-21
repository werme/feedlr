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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	// Database static variables

	private static final int DATABASE_VERSION = 9;

	private static final String DATABASE_NAME = "feedlrDatabase";

	// Declaring feed table
	public static final String TABLE_FEED = "feed";
	public static final String FEED_COLUMN_ID = "_id";
	public static final String FEED_COLUMN_NAME = "name";

	// Declaring feed-user bridge table
	public static final String TABLE_FEEDUSER = "feeduser";
	public static final String FEEDUSER_COLUMN_FEED_ID = "feed_ID";
	public static final String FEEDUSER_COLUMN_USER_ID = "user_ID";

	// Declaring user table
	public static final String TABLE_USER = "user";
	private static final String USER_COLUMN_ID = "_id";
	public static final String USER_COLUMN_USERNAME = "username";
	public static final String USER_COLUMN_USERID = "userid";
	public static final String USER_COLUMN_IMGURL = "profile_image_URL";
	public static final String USER_COLUMN_SOURCE = "source";

	// Declaring item table
	public static final String TABLE_ITEM = "item";
	private static final String ITEM_COLUMN_ID = "_id";
	public static final String ITEM_COLUMN_ITEMID = "itemid";
	public static final String ITEM_COLUMN_TEXT = "text";
	public static final String ITEM_COLUMN_TIMESTAMP = "timestamp";
	private static final String ITEM_COLUMN_TYPE = "type";
	private static final String ITEM_COLUMN_URL = "URL";
	public static final String ITEM_COLUMN_IMGURL = "imgURL";
	public static final String ITEM_COLUMN_USER_ID = "user_ID";
	public static final String ITEM_COLUMN_USERNAME = "username";

	private SQLiteDatabase db = this.getWritableDatabase();

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase database) {
		// @formatter:off
		// Creating feed table
		database.execSQL("CREATE TABLE " + TABLE_FEED + "(" + FEED_COLUMN_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + FEED_COLUMN_NAME
				+ " TEXT UNIQUE" + ")");

		// Creating feed-user bridge table
		database.execSQL("CREATE TABLE " + TABLE_FEEDUSER + "("
				+ FEEDUSER_COLUMN_FEED_ID + " INT NOT NULL,"
				+ FEEDUSER_COLUMN_USER_ID + " INT NOT NULL" + ")");

		// Creating user table
		// TODO Should username be the unique idenifier of a user?!
		database.execSQL("CREATE TABLE " + TABLE_USER + "(" + USER_COLUMN_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_COLUMN_USERNAME
				+ " TEXT NOT NULL," + USER_COLUMN_USERID + " TEXT UNIQUE,"
				+ USER_COLUMN_IMGURL + " TEXT," + USER_COLUMN_SOURCE
				+ " TEXT NOT NULL" + ")");

		// Creating item table
		database.execSQL("CREATE TABLE " + TABLE_ITEM + "(" + ITEM_COLUMN_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + ITEM_COLUMN_ITEMID
				+ " INT UNIQUE," + ITEM_COLUMN_TEXT + " TEXT,"
				+ ITEM_COLUMN_TIMESTAMP + " INT," + ITEM_COLUMN_TYPE + " TEXT,"
				+ ITEM_COLUMN_URL + " TEXT," + ITEM_COLUMN_IMGURL + " TEXT,"
				+ ITEM_COLUMN_USER_ID + " INT NOT NULL," + ITEM_COLUMN_USERNAME
				+ " TEXT" + ")");
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

	//
	//
	// Feed related methods:
	//

	/**
	 * Adds a feed to the database.
	 * 
	 * @param feed
	 *            the feed to be added in the database.
	 * @return true if the feed was added to the database.
	 */
	public boolean addFeed(Feed feed) throws SQLiteException {
		if (feedExist(feed))
			return false;
		db.insert(TABLE_FEED, null, feedCV(feed));
		return true;
	}

	/**
	 * Removes a feed from the database.
	 * 
	 * @param feed
	 *            the feed to be removed.
	 * @return true if the feed was removed.
	 */
	public boolean removeFeed(Feed feed) {
		if (feedExist(feed)) {
			long id = getFeed_id(feed);
			removeFeedBridge(id);
			db.delete(TABLE_FEED, FEED_COLUMN_ID + "=?",
					new String[] { id + "" });
			return true;
		}
		return false;
	}

	/**
	 * Method to see if a feed exists.
	 * 
	 * @param feed
	 *            the feed to be searched for.
	 * @return true if feed exists.
	 */
	public boolean feedExist(Feed feed) {
		if (feed.getTitle() == null) {
			return false;
		}
		Cursor c = db.query(TABLE_FEED, new String[] { FEED_COLUMN_NAME },
				FEED_COLUMN_NAME + " = ?", new String[] { feed.getTitle() },
				null, null, null);
		if (c.getCount() == 0)
			return false;
		return true;
	}

	/**
	 * Removes all feeds from the database.
	 */
	public void clearFeeds() {
		db.delete(TABLE_FEED, null, null);
		db.delete(TABLE_FEEDUSER, null, null);
	}

	/**
	 * Returns the size of the feed table.
	 * 
	 * @return number of feeds in the database as a long.
	 */
	public long getFeedTableSize() {
		return DatabaseUtils.queryNumEntries(db, TABLE_FEED);
	}

	/**
	 * Lists all the feeds in the database.
	 * 
	 * @return A ArrayList of string with all the names of the feeds in the
	 *         database.
	 */
	public ArrayList<String> listFeeds() {
		final ArrayList<String> feeds = new ArrayList<String>();

		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_FEED, null);
		while (c.moveToNext()) {
			String s = c.getString(1);
			feeds.add(s);
		}
		c.close();
		return feeds;
	}

	/**
	 * Returns the ID of the feed.
	 * 
	 * @param feed
	 *            whose ID is searched for
	 * @return the id of the searched feed. -1 if the feed doesn't exist.
	 */
	public long getFeed_id(Feed feed) throws IllegalArgumentException {
		String feedTitle = feed.getTitle();
		Long feed_id;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.query(TABLE_FEED, new String[] { FEED_COLUMN_ID },
				FEED_COLUMN_NAME + "=?", new String[] { feedTitle }, null,
				null, null);

		if (c.moveToNext()) {
			feed_id = Long.parseLong(c.getString(0));
			c.close();
			return feed_id;
		}
		return -1;
	}

	//
	//
	// User related methods
	//

	/**
	 * Adds a user to the database.
	 * 
	 * @param user
	 *            the user to be added in the database.
	 * @return true if user was added or updated in the database.
	 */
	public boolean addUser(User user) {
		if (user.getId() == 0 || user.getUserName() == null) {
			return false;
		}
		if (updateUser(user)) {
			return true;
		}
		db.insert(TABLE_USER, null, userCV(user));
		return true;
	}

	/**
	 * Adds a list of users to the database.
	 * 
	 * @param users
	 *            the lists of users to be added in the database.
	 */
	public void addUsers(List<? extends User> users) {
		db.beginTransaction();
		// TODO Make sure addUsers are using addUser method instead of a insert
		// to db.
		for (User u : users) {
			addUser(u);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	/**
	 * Updates the representation of user in the database.
	 * 
	 * @param user
	 *            the user to be updated.
	 * @return the database ID of the user added.
	 */
	public boolean updateUser(User user) {
		if (userExist(user)) {
			db.update(TABLE_USER, userCV(user), USER_COLUMN_USERID + " = "
					+ user.getId(), null);
			return true;
		}
		return false;
	}

	/**
	 * Removes a user from the database.
	 * 
	 * @param user
	 *            the user to be removed.
	 * @return result of action, true if user removed.
	 */
	public boolean removeUser(User user) {
		if (userExist(user)) {
			db.delete(TABLE_USER, USER_COLUMN_USERID + "=?",
					new String[] { user.getId() + "" });
			return true;
		}
		return false;
	}

	/**
	 * Check if a user exists in the database.
	 * 
	 * @param user
	 *            the user to check.
	 * @return the result of the query, true if user exists.
	 */
	public boolean userExist(User user) {
		if (user.getId() == 0 || user.getUserName() == null) {
			return false;
		}
		Cursor c = db.query(TABLE_USER, new String[] { USER_COLUMN_USERID },
				USER_COLUMN_USERID + " = ?",
				new String[] { user.getId() + "" }, null, null, null);
		if (c.getCount() == 0)
			return false;
		return true;
	}

	/**
	 * Removes all the users from the database.
	 */
	public void clearUserTable() {
		db.delete(TABLE_USER, null, null);
	}

	/*
	 * Returns the size of the user table.
	 * 
	 * @return number of users in the database.
	 */
	public long getUserTableSize() {
		return DatabaseUtils.queryNumEntries(db, TABLE_USER);
	}

	/**
	 * Returns a cursor of all the users in the database.
	 * 
	 * @return a cursor pointing at all the users in the database.
	 */
	public Cursor listUsers() {
		return db.rawQuery("SELECT * FROM " + TABLE_USER, null);
	}

	/**
	 * Returns the database ID of a user.
	 * 
	 * @param user
	 *            the user which ID is searched for.
	 * @return the database ID of the user.
	 * @throws IllegalArgumentException
	 *             if the user does not exists within the database.
	 */
	private long getUser_id(User user) {
		long id = user.getId();
		long user_id;
		Cursor c = db.query(TABLE_USER, new String[] { USER_COLUMN_ID },
				USER_COLUMN_ID + "=?", new String[] { id + "" }, null, null,
				null);
		if (c.moveToNext()) {
			user_id = Long.parseLong(c.getString(0));
			c.close();
			return user_id;
		}
		return -1;

	}

	/**
	 * Return a cursor pointing at the requested user.
	 * 
	 * @param userID
	 *            the userID of the user that is requested.
	 * @return a cursor pointing at the requested user.
	 */
	public Cursor getUser(String userID) {
		return db.query(TABLE_USER, null, USER_COLUMN_USERID + " = ?",
				new String[] { userID }, null, null, null);
	}

	/**
	 * Return a cursor pointing at all the users in a feed.
	 * 
	 * @param feed
	 *            the feed which users are part of.
	 * @return a cursor with all the users in the feed.
	 */
	public Cursor getUsers(Feed feed) {
		return db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE "
				+ USER_COLUMN_USERID + " IN (SELECT " + FEEDUSER_COLUMN_USER_ID
				+ " FROM " + TABLE_FEEDUSER + " WHERE "
				+ FEEDUSER_COLUMN_FEED_ID + " = " + getFeed_id(feed) + ")",
				null);
	}

	/**
	 * Returns a cursor with all users in a feed from a certain source.
	 * 
	 * @param feed
	 *            the feed which the users are part of.
	 * 
	 * @param source
	 *            which source the users in the feed come from.
	 * @return a cursor with all the selected users.
	 */
	public Cursor getUsers(Feed feed, String source) {
		if (source == null)
			return null;
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE "
				+ USER_COLUMN_SOURCE + " = ?" + " AND " + USER_COLUMN_USERID
				+ " IN (SELECT " + FEEDUSER_COLUMN_USER_ID + " FROM "
				+ TABLE_FEEDUSER + " WHERE " + FEEDUSER_COLUMN_FEED_ID + " = "
				+ getFeed_id(feed) + ")" + " ORDER BY " + USER_COLUMN_USERNAME
				+ " ASC", new String[] { source });
		return c;
	}

	/**
	 * Returns a cursor with all the users in the database.
	 * 
	 * @return a cursor with all users.
	 */
	public Cursor getAllUsers() {
		return db.query(TABLE_USER, new String[] { USER_COLUMN_ID,
				USER_COLUMN_USERNAME, USER_COLUMN_USERID }, null, null, null,
				null, USER_COLUMN_USERNAME + " ASC");
	}

	//
	//
	// Item related methods
	//

	/**
	 * Adds a item to the database.
	 * 
	 * @param item
	 *            the item to be added.
	 * @return
	 */
	public boolean addItem(Item item) {
		if (item.getId() == null || item.getUser() == null) {
			return false;
		}
		if (updateItem(item)) {
			return true;
		}
		db.insert(TABLE_ITEM, null, itemCV(item));
		return true;
	}

	/**
	 * Method to update a item in the item table.
	 * 
	 * @param item
	 *            the item to be updated.
	 * @return true if the item got updated, false if the item does not exist.
	 */
	public boolean updateItem(Item item) {
		if (itemExist(item)) {
			db.update(TABLE_ITEM, itemCV(item), ITEM_COLUMN_ITEMID + " = "
					+ item.getId(), null);
			return true;
		}
		return false;
	}

	/**
	 * Method to check if a item exists in the database.
	 * 
	 * @param item
	 *            the item to be searched.
	 * @return true if item exists, false if the item does not exist.
	 */
	public boolean itemExist(Item item) {
		if (!(item.getId() == null)) {
			Cursor c = db.query(TABLE_ITEM,
					new String[] { ITEM_COLUMN_ITEMID }, ITEM_COLUMN_ITEMID
							+ " = ?", new String[] { item.getId() + "" }, null,
					null, null);
			if (c.getCount() > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds a list of items in the database in a fast manner.
	 * 
	 * @param itemList
	 *            the list of items to be added.
	 */
	public void addListOfItems(List<? extends Item> itemList) {
		db.beginTransaction();
		for (Item i : itemList) {
			addItem(i);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	/**
	 * Removes all the items from the table.
	 */
	public void clearItemTable() {
		db.delete(TABLE_ITEM, null, null);
	}

	/**
	 * Returns the size of the ItemTable.
	 * 
	 * @return the total number of items in the database as a long.
	 */
	public long getItemTableSize() {
		return DatabaseUtils.queryNumEntries(db, TABLE_ITEM);
	}

	/**
	 * Returns a cursor with all the items in a feed.
	 * 
	 * @param feed
	 *            which feed's items that will be returned.
	 * @param int the amount of items returned.
	 * @return a cursor with the requested items.
	 */
	public Cursor getItems(Feed feed, int amount) {
		return db.query(TABLE_ITEM, null, ITEM_COLUMN_USER_ID + " IN (SELECT "
				+ FEEDUSER_COLUMN_USER_ID + " FROM " + TABLE_FEEDUSER
				+ " WHERE " + FEEDUSER_COLUMN_FEED_ID + " = "
				+ getFeed_id(feed) + ")", null, null, null,
				ITEM_COLUMN_TIMESTAMP + " DESC", amount + "");
	}

	/**
	 * Returns a cursor with all the items in the database.
	 * 
	 * @return a cursor with all items.
	 */
	public Cursor getAllItems() {
		return db.query(TABLE_ITEM, new String[] { ITEM_COLUMN_ID,
				ITEM_COLUMN_TEXT, ITEM_COLUMN_TIMESTAMP, ITEM_COLUMN_TYPE,
				ITEM_COLUMN_URL, ITEM_COLUMN_IMGURL, ITEM_COLUMN_USER_ID,
				ITEM_COLUMN_USERNAME }, null, null, null, null, null);
	}

	//
	//
	// Bridge methods:
	//

	/**
	 * Adds a connection with a user to a feed.
	 * 
	 * @param feedID
	 *            the feedID which the userID will be connected with.
	 * @param userID
	 *            the userID to be connected with a feedID
	 * 
	 */
	public void addFeedUserBridge(long feedID, long userID) {
		ContentValues cv = new ContentValues();
		cv.put(FEEDUSER_COLUMN_FEED_ID, feedID);
		cv.put(FEEDUSER_COLUMN_USER_ID, userID);
		db.insert(TABLE_FEEDUSER, null, cv);
	}

	/**
	 * Removes the connection with a feed and user.
	 * 
	 * @param feedID
	 *            the feedID which userID is connected with.
	 * @param userID
	 *            the userID which connection will be removed.
	 */
	private void removeFeedUserBridge(long feedID, long userID) {
		db.delete(TABLE_FEEDUSER, FEEDUSER_COLUMN_FEED_ID + "=?" + " and "
				+ FEEDUSER_COLUMN_USER_ID + "=?", new String[] { feedID + "",
				userID + "" });
	}

	/**
	 * Removes the connection with a feed and users.
	 * 
	 * @param feed
	 *            the feed which user is connected with.
	 * 
	 *            the user which connection will be removed.
	 */
	private void removeFeedBridge(Long feedID) {
		db.delete(TABLE_FEEDUSER, FEEDUSER_COLUMN_FEED_ID + "=?",
				new String[] { feedID + "" });
	}

	/**
	 * Returns all users in a feed.
	 * 
	 * @param feed
	 *            the feed which the users are part of.
	 * @return a cursor with all the users in the feed.
	 */

	//
	//
	// ContentValue creaters for each table
	//

	/**
	 * Convenience method for loading values of a feed.
	 * 
	 * @param feed
	 *            the feed to be made a ContentValues object.
	 * @return ContentValues object created
	 */
	private ContentValues feedCV(Feed feed) {
		ContentValues cv = new ContentValues();
		cv.put(FEED_COLUMN_NAME, feed.getTitle());
		return cv;
	}

	/**
	 * Convenience method for loading values of a user.
	 * 
	 * @param user
	 *            the user to be made a ContentValues object.
	 * @return ContentValues object created
	 */
	private ContentValues userCV(User user) {
		ContentValues cv = new ContentValues();

		cv.put(USER_COLUMN_USERNAME, user.getUserName());
		cv.put(USER_COLUMN_USERID, user.getId());
		cv.put(USER_COLUMN_IMGURL, user.getProfileImageURL());
		cv.put(USER_COLUMN_SOURCE, user.getSource());
		return cv;
	}

	/**
	 * Convenience method for loading values of a item.
	 * 
	 * @param item
	 *            the feed to be made a ContentValues object.
	 * @return ContentValues object created
	 */
	private ContentValues itemCV(Item item) {
		ContentValues cv = new ContentValues();
		cv.put(ITEM_COLUMN_ITEMID, item.getId());
		cv.put(ITEM_COLUMN_TEXT, item.getText());
		cv.put(ITEM_COLUMN_TIMESTAMP, item.getTimestamp());
		cv.put(ITEM_COLUMN_TYPE, item.getType());
		cv.put(ITEM_COLUMN_URL, item.getURL());
		cv.put(ITEM_COLUMN_IMGURL, item.getIMGURL());
		cv.put(ITEM_COLUMN_USER_ID, item.getUser().getId());
		return cv;
	}
}
