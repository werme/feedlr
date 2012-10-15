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
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	// Database static variables

	private static final int DATABASE_VERSION = 7;

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
	public static final String USER_COLUMN_IMGURL = "ProfileImageURL";
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

	/**
	 * Adds a feed to the database.
	 * 
	 * @param feed
	 *            the feed to be added to the database.
	 * @throws SQLiteException
	 *             If the name already exists within the database.
	 */
	public void addFeed(Feed feed) throws SQLiteException {
		db.insertOrThrow(TABLE_FEED, null, feedCV(feed));
	}

	/**
	 * Removes a feed from the database.
	 * 
	 * @param feed
	 *            the feed that will be removed from the database.
	 * @throws IllegalArgumentException
	 *             if the feed to remove does not exist.
	 */
	public void removeFeed(Feed feed) throws IllegalArgumentException {
		String title = feed.getTitle();
		long id = getFeed_id(feed);

		removeFeedBridge(id);

		db.delete(TABLE_FEED, FEED_COLUMN_NAME + "=?", new String[] { title });
	}

	/**
	 * Removes all feeds from the database.
	 */
	public void clearFeeds() {
		db.delete(TABLE_FEED, null, null);
		db.delete(TABLE_FEEDUSER, null, null);
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
	 * @return the id of the searched feed.
	 * @throws IllegalArgumentException
	 *             if no such feed exists in the database.
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
		} else {
			throw (new IllegalArgumentException("Feed does not exist!"));
		}
		c.close();
		return feed_id;
	}

	// User related methods
	/**
	 * Adds a user to the database.
	 * 
	 * @param user
	 *            the user to be added in the database.
	 * @return the database ID of the user.
	 */
	public long addUser(User user) {
		Long userID;
		try {
			userID = getUser_id(user);
			updateUser(user);
		} catch (IllegalArgumentException e) {
			userID = db.insert(TABLE_USER, null, userCV(user));
		}
		return userID;
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
			db.insertWithOnConflict(TABLE_USER, null, userCV(u),
					SQLiteDatabase.CONFLICT_IGNORE);
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

	public long updateUser(User user) {
		return db.update(TABLE_USER, userCV(user), USER_COLUMN_ID + " = "
				+ user.getId(), null);
	}

	/**
	 * Removes a user from the database.
	 * 
	 * @param user
	 *            the user to be removed from the database.
	 */
	private void removeUser(User user) {
		long id = user.getId();
		db.delete(TABLE_USER, USER_COLUMN_ID + "=?", new String[] { id + "" });
	}

	/**
	 * Removes all the users from the database.
	 */
	public void clearUserTable() {
		db.delete(TABLE_USER, null, null);
	}

	/**
	 * Returns a cursor of all the users in the database.
	 * 
	 * @return
	 */
	public Cursor listUsers() {
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_USER, null);
		return c;
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
	private long getUser_id(User user) throws IllegalArgumentException {
		long id = user.getId();
		long user_id;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.query(TABLE_USER, new String[] { USER_COLUMN_ID },
				USER_COLUMN_ID + "=?", new String[] { id + "" }, null, null,
				null);
		if (c.moveToNext()) {
			user_id = Long.parseLong(c.getString(0));
		} else {
			throw (new IllegalArgumentException("User does not exist!"));
		}
		c.close();
		return user_id;
	}

	// Item related methods
	/**
	 * Adds a item to the database.
	 * 
	 * @param the
	 *            item to be added.
	 */
	public long addItem(Item item) {
		return db.insert(TABLE_ITEM, null, itemCV(item));
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

	// Bridge methods:
	/**
	 * Adds a connection with a user to a feed.
	 * 
	 * @param feed
	 *            the feed which the user will be connected with.
	 * @param user
	 *            the user to be connected with a feed
	 * 
	 */
	public void addUserToFeed(Feed feed, User user) {
		long FeedID = getFeed_id(feed);
		long UserID = addUser(user);

		// Add bridge connection
		if (UserID != -1) {
			addFeedUserBridge(FeedID, UserID);
		}
	}

	/**
	 * Removes the connection with a feed and user.
	 * 
	 * @param feed
	 *            the feed which user is connected with.
	 * @param user
	 *            the user which connection will be removed.
	 */
	public void removeUserFromFeed(Feed feed, User user) {
		long feedID = getFeed_id(feed);
		long userID = getUser_id(user);

		removeFeedUserBridge(feedID, userID);
	}

	public void addFeedUserBridge(long feedID, long userID) {
		ContentValues temp = new ContentValues();

		temp.put(FEEDUSER_COLUMN_FEED_ID, feedID);
		temp.put(FEEDUSER_COLUMN_USER_ID, userID);

		db.insert(TABLE_FEEDUSER, null, temp);
	}

	private void removeFeedUserBridge(long feedID, long userID) {
		db.delete(TABLE_FEEDUSER, FEEDUSER_COLUMN_FEED_ID + "=?" + " and "
				+ FEEDUSER_COLUMN_USER_ID + "=?", new String[] { feedID + "",
				userID + "" });
	}

	private void removeFeedBridge(Long id) {
		db.delete(TABLE_FEEDUSER, FEEDUSER_COLUMN_FEED_ID + "=?",
				new String[] { id + "" });
	}

	public Cursor getUsers(Feed feed, String source) {

		Cursor c;
		if (source.equals(null))
			c = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE "
					+ USER_COLUMN_USERID + " IN (SELECT "
					+ FEEDUSER_COLUMN_USER_ID + " FROM " + TABLE_FEEDUSER
					+ " WHERE " + FEEDUSER_COLUMN_FEED_ID + " = "
					+ getFeed_id(feed) + ")", null);
		else {
			c = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE "
					+ USER_COLUMN_SOURCE + " = ?" + " AND "
					+ USER_COLUMN_USERID + " IN (SELECT "
					+ FEEDUSER_COLUMN_USER_ID + " FROM " + TABLE_FEEDUSER
					+ " WHERE " + FEEDUSER_COLUMN_FEED_ID + " = "
					+ getFeed_id(feed) + ")", new String[] { source });
		}
		return c;
	}

	public Cursor getItems(Feed feed) {
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_ITEM + " WHERE "
				+ ITEM_COLUMN_USER_ID + " IN (SELECT "
				+ FEEDUSER_COLUMN_USER_ID + " FROM " + TABLE_FEEDUSER
				+ " WHERE " + FEEDUSER_COLUMN_FEED_ID + " = "
				+ getFeed_id(feed) + ") ORDER BY " + ITEM_COLUMN_TIMESTAMP
				+ " DESC", null);
		return c;
	}

	public Cursor getAllItems() {
		Cursor c = db.query(TABLE_ITEM, new String[] { ITEM_COLUMN_ID,
				ITEM_COLUMN_TEXT, ITEM_COLUMN_TIMESTAMP, ITEM_COLUMN_TYPE,
				ITEM_COLUMN_URL, ITEM_COLUMN_IMGURL, ITEM_COLUMN_USER_ID,
				ITEM_COLUMN_USERNAME }, null, null, null, null, null);
		return c;
	}

	public Cursor getAllUsers() {
		Cursor c = db.query(TABLE_USER, new String[] { USER_COLUMN_ID,
				USER_COLUMN_USERNAME, USER_COLUMN_USERID }, null, null, null,
				null, null);
		return c;
	}

	public Cursor getUser(String userID) {
		Cursor c = db.query(TABLE_USER, new String[] { USER_COLUMN_USERNAME,
				USER_COLUMN_USERID, USER_COLUMN_IMGURL, USER_COLUMN_SOURCE },
				USER_COLUMN_USERID + " = ?", new String[] { userID }, null,
				null, null);
		return c;
	}

	public long getItemTableSize() {
		long l = DatabaseUtils.queryNumEntries(db, TABLE_ITEM);
		return l;
	}

	// Contentvalue creaters for each table

	private ContentValues feedCV(Feed feed) {
		ContentValues cv = new ContentValues();
		cv.put(FEED_COLUMN_NAME, feed.getTitle());
		return cv;
	}

	private ContentValues userCV(User user) {
		ContentValues cv = new ContentValues();

		cv.put(USER_COLUMN_USERNAME, user.getUserName());
		cv.put(USER_COLUMN_USERID, user.getId());
		cv.put(USER_COLUMN_IMGURL, user.getProfileImageURL());
		cv.put(USER_COLUMN_SOURCE, user.getSource());
		return cv;
	}

	private ContentValues itemCV(Item i) {
		ContentValues cv = new ContentValues();
		cv.put(ITEM_COLUMN_ITEMID, i.getId());
		cv.put(ITEM_COLUMN_TEXT, i.getText());
		cv.put(ITEM_COLUMN_TIMESTAMP, i.getTimestamp());
		cv.put(ITEM_COLUMN_TYPE, i.getText());
		cv.put(ITEM_COLUMN_URL, i.getURL());
		cv.put(ITEM_COLUMN_IMGURL, i.getIMGURL());
		cv.put(ITEM_COLUMN_USER_ID, i.getUser().getId());
		return cv;
	}
}
