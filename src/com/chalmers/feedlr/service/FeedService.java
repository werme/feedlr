/**
 * Class description
 * 
 * @author Olle Werme
 */

package com.chalmers.feedlr.service;

import java.util.List;

import com.chalmers.feedlr.activity.FeedActivity;
import com.chalmers.feedlr.client.TwitterHelper;
import com.chalmers.feedlr.model.TwitterItem;
import com.chalmers.feedlr.model.User;
import com.chalmers.feedlr.util.ClientStore;
import com.chalmers.feedlr.util.TwitterJSONParser;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class FeedService extends Service {
	private final IBinder binder = new FeedServiceBinder();

	private LocalBroadcastManager lbm;

	private TwitterHelper twitter;

	public class FeedServiceBinder extends Binder {
		FeedService getService() {
			return FeedService.this;
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		lbm = LocalBroadcastManager.getInstance(FeedService.this);

		twitter = new TwitterHelper(ClientStore.getTwitterAccessToken(this));
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}

	private void runAsync(final Runnable runnable) {
		new Thread() {
			@Override
			public void run() {
				try {
					runnable.run();
				} finally {

				}
			}
		}.start();
	}

	public void updateTwitterTimeline() {
		runAsync(new Runnable() {
			@Override
			public void run() {
				long time = System.currentTimeMillis();

				List<TwitterItem> timeline = twitter.getTimeline();

				// save to database

				// Broadcast update to activity
				Intent intent = new Intent();
				intent.setAction(FeedActivity.TWITTER_TIMELINE_UPDATED);
				lbm.sendBroadcast(intent);

				Log.i(TwitterJSONParser.class.getName(),
						"Time in millis for complete Twitter timeline request: "
								+ (System.currentTimeMillis() - time));
			}
		});
	}

	public void updateTwitterUsers() {
		runAsync(new Runnable() {
			@Override
			public void run() {
				long time = System.currentTimeMillis();

				List<User> users = twitter.getFollowing();

				// save to database

				// Broadcast update to activity
				Intent intent = new Intent();
				intent.setAction(FeedActivity.TWITTER_USERS_UPDATED);
				lbm.sendBroadcast(intent);

				Log.i(TwitterJSONParser.class.getName(),
						"Time in millis for complete Twitter following request: "
								+ (System.currentTimeMillis() - time));
			}
		});
	}

	public void updateTwitterUserTweets(final int userID) {
		runAsync(new Runnable() {
			@Override
			public void run() {
				long time = System.currentTimeMillis();

				List<TwitterItem> userTweets = twitter.getUserTweets(userID);

				// save to database

				// Broadcast update to activity
				Intent intent = new Intent();
				intent.setAction(FeedActivity.TWITTER_USER_TWEETS_UPDATED);
				Bundle b = new Bundle();
				b.putInt("userID", userID);
				intent.putExtras(b);
				lbm.sendBroadcast(intent);

				Log.i(TwitterJSONParser.class.getName(),
						"Time in millis for complete Twitter user tweets request: "
								+ (System.currentTimeMillis() - time));
			}
		});
	}
}
