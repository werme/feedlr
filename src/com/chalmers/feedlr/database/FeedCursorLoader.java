package com.chalmers.feedlr.database;

import com.chalmers.feedlr.activity.FeedActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

public class FeedCursorLoader extends SimpleCursorLoader {

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String broadcast = intent.getAction();
			Bundle b = intent.getExtras();

			String dialog;

			if (broadcast.equals(FeedActivity.TWITTER_TIMELINE_UPDATED)) {
				dialog = "Twitter timeline updated in Loader!";
				loadInBackground();
			} else if (broadcast.equals(FeedActivity.TWITTER_USERS_UPDATED))
				dialog = "Twitter users updated!";
			else if (broadcast.equals(FeedActivity.TWITTER_USER_TWEETS_UPDATED))
				dialog = "Tweets for Twitter user with ID: "
						+ b.getInt("userID") + " updated!";
			else if (broadcast.equals(FeedActivity.FACEBOOK_TIMELINE_UPDATED))
				dialog = "Facebook timeline updated!";
			else if (broadcast.equals(FeedActivity.FACEBOOK_USERS_UPDATED))
				dialog = "Facebook users updated!";
			else if (broadcast.equals(FeedActivity.FACEBOOK_USER_NEWS_UPDATED))
				dialog = "News for Facebook user with ID: "
						+ b.getInt("userID") + " updated!";
			else if (broadcast.equals(FeedActivity.FEED_UPDATED))
				dialog = "Feed: " + b.getString("feedTitle") + " updated!";
			else
				dialog = "broadcast from unknown intent recieved!";

			Toast.makeText(context, dialog, Toast.LENGTH_SHORT).show();
		}
	};

	private DatabaseHelper db;

	public FeedCursorLoader(Context context, DatabaseHelper db) {
		super(context);
		this.db = db;

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
		Cursor cursor = db.getAllItems();
		return cursor;
	}

}