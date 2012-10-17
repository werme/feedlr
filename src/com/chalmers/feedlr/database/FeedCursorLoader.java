package com.chalmers.feedlr.database;

import com.chalmers.feedlr.model.Feed;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

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
		Cursor cursor = db.getItems(new Feed(feedTitle), 60);
		Log.i("loader", "items: " + cursor.getCount());
		return cursor;
	}
}