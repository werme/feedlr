package com.chalmers.feedlr.database;

import java.util.ArrayList;

import com.chalmers.feedlr.model.Feed;
import com.chalmers.feedlr.model.User;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

public class DatabaseHelperTest extends AndroidTestCase {

	DatabaseHelper dbHelper;
	SQLiteDatabase db;

	protected void setUp() throws Exception {
		super.setUp();
		getContext().deleteDatabase("feedlrDatabase");
		dbHelper = new DatabaseHelper(getContext());
		db = dbHelper.getWritableDatabase();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testAddFeed() {
		// Testing if the feed gets added to the table.
		String feedName = "testFeed";
		Feed f1 = new Feed(feedName);
		dbHelper.addFeed(f1);
		long feedTableSize = DatabaseUtils.queryNumEntries(db,
				DatabaseHelper.TABLE_FEED);
		assertTrue(feedTableSize == 1);

		// Checking the name if the name is saved correctly.
		Cursor c = db.query(DatabaseHelper.TABLE_FEED,
				new String[] { DatabaseHelper.FEED_COLUMN_NAME },
				DatabaseHelper.FEED_COLUMN_ID + " = ?",
				new String[] { dbHelper.getFeedID(f1) + "" }, null, null, null);
		c.moveToFirst();
		assertTrue(c.getString(0).equals(feedName));
		
		// Checking if adding the same feed again is possible.
		assertFalse(dbHelper.addFeed(new Feed(feedName)));
	}

	public void testRemoveFeed() {
		String feedName = "testFeed";
		Feed f1 = new Feed(feedName);
		dbHelper.addFeed(f1);
		assertTrue(dbHelper.removeFeed(f1));
		
		// Checks that no feeds are left.
		long feedTableSize = DatabaseUtils.queryNumEntries(db,
				DatabaseHelper.TABLE_FEED);
		assertTrue(feedTableSize == 0);
		
		// Trying to remove a feed that does not exist.
		assertFalse(dbHelper.removeFeed(f1));
	}

	public void testListFeeds() {
		dbHelper.addFeed(new Feed("Feed1"));
		dbHelper.addFeed(new Feed("Feed2"));
		ArrayList<String> feeds = dbHelper.listFeeds();
		assertTrue(feeds.get(0).equals("Feed1") && feeds.get(1).equals("Feed2"));
	}

	public void testAddUser() {
		User u1 = new User(1, "Olle");
		
		dbHelper.addUser(u1);
	}
}
