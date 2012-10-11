package com.chalmers.feedlr.database;

import com.chalmers.feedlr.activity.FeedActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;

public class FeedCursorLoader extends SimpleCursorLoader {
	
	private String feedTitle;
	private DatabaseHelper db;

	public FeedCursorLoader(Context context, String feedTitle) {
		super(context);
		this.feedTitle = feedTitle;
		
		db = new DatabaseHelper(context);
	}

	@Override
	public Cursor loadInBackground() {
//		Cursor cursor = db.getFeedItems(feedTitle);
		Cursor cursor = db.getAllItems();
		return cursor;
	}
}