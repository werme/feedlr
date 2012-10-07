package com.chalmers.feedlr.database;

import android.content.Context;
import android.database.Cursor;

public class FeedCursorLoader extends SimpleCursorLoader {

	private DatabaseHelper db;

	public FeedCursorLoader(Context context, DatabaseHelper db) {
		super(context);
		this.db = db;
	}

	@Override
	public Cursor loadInBackground() {
		Cursor cursor = db.getAllItems();
		return cursor;
	}

}