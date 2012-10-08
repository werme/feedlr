package com.chalmers.feedlr.database;

import com.chalmers.feedlr.activity.FeedActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;

public class FeedCursorLoader extends SimpleCursorLoader {

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			FeedCursorLoader.this.onContentChanged();
		}
	};
	
	private String feedTitle;
	private DatabaseHelper db;

	public FeedCursorLoader(Context context, String feedTitle) {
		super(context);
		this.feedTitle = feedTitle;
		
		db = new DatabaseHelper(context);

		LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(context);

		IntentFilter filter = new IntentFilter();
		filter.addAction(FeedActivity.TWITTER_TIMELINE_UPDATED);
		filter.addAction(FeedActivity.TWITTER_USERS_UPDATED);
		filter.addAction(FeedActivity.TWITTER_USER_TWEETS_UPDATED);
		filter.addAction(FeedActivity.FACEBOOK_TIMELINE_UPDATED);
		filter.addAction(FeedActivity.FACEBOOK_USERS_UPDATED);
		filter.addAction(FeedActivity.FACEBOOK_USER_NEWS_UPDATED);
		filter.addAction(FeedActivity.FEED_UPDATED);
		lbm.registerReceiver(receiver, filter);
	}

	@Override
	public Cursor loadInBackground() {
//		Cursor cursor = db.getFeedItems(feedTitle);
		Cursor cursor = db.getAllItems();
		return cursor;
	}
}