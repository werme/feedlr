package com.chalmers.feedlr.database;

import java.util.ArrayList;

import com.chalmers.feedlr.model.Feed;
import com.chalmers.feedlr.model.User;

import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteException;
import android.test.AndroidTestCase;
import android.util.Log;

public class DatabaseHelperTest extends AndroidTestCase {

	DatabaseHelper db;

	protected void setUp() throws Exception {
		super.setUp();
		getContext().deleteDatabase("feedlrDatabase");
		db = new DatabaseHelper(getContext());
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testAddFeed() {
		db.addFeed(new Feed("testFeed"));
		try{
			db.addFeed(new Feed("testFeed"));
			fail("2 Feeds with identical name");
		}
		catch(SQLiteConstraintException e){
			assertTrue(true);
		}
		
	}
	
	public void testRemoveFeed() {
		db.addFeed(new Feed("testFeed"));
		db.removeFeed(new Feed("testFeed"));
		assertTrue(true);
		try{
			db.removeFeed(new Feed("testFeed"));
			fail("Removed a feed which does not exist");
		}
		catch(IllegalArgumentException e){
			assertTrue(true);
		}
	}

	public void testListFeeds() {
		db.addFeed(new Feed("Feed1"));
		db.addFeed(new Feed("Feed2"));
		ArrayList<String> feeds = db.listFeeds();
		if(feeds.get(0).equals("Feed1") && feeds.get(1).equals("Feed2")){
			assertTrue(true);
		}else {
			fail("ListFeeds not showing correct feeds");
		}
	}

	public void testAddUser() {
		User u1 = new User(1, "Olle");
		User u2 = new User(2, "David");
		User u3 = new User(1, "Daniel");
		db.addUser(u1);
		db.addUser(u2);
		db.addUser(u3);
		Cursor c =  db.listUsers();
		final ArrayList<String> users = new ArrayList<String>();
		while (c.moveToNext()) {
			String s = c.getString(1);
			users.add(s);
		}
		Log.i("a:", "" + users);
	}
/*
	public void testAddFeedUserBridge() {
		fail("Not yet implemented");
	}

	public void testRemoveUserFromFeed() {
		fail("Not yet implemented");
	}

	public void testListFeedUser() {
		fail("Not yet implemented");
	}

	public void testUpdateUser() {
		fail("Not yet implemented");
	}

	public void testAddListOfItems() {
		fail("Not yet implemented");
	}

	public void testGetUsersInFeed() {
		fail("Not yet implemented");
	}

	public void testGetAllItems() {
		fail("Not yet implemented");
	}

	public void testGetItemTableSize() {
		fail("Not yet implemented");
	}

	public void testClearItemTable() {
		fail("Not yet implemented");
	}

	public void testClearUserTable() {
		fail("Not yet implemented");
	}

	public void testClearFeeds() {
		fail("Not yet implemented");
	}

	public void testAddUsers() {
		fail("Not yet implemented");
	}

	public void testGetAllUsers() {
		fail("Not yet implemented");
	}

	public void testGetItems() {
		fail("Not yet implemented");
	}
	*/
}
