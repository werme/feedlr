/**
 * Class description
 * 
 * @author Olle Werme
 */

package com.chalmers.feedlr.service;

import java.util.ArrayList;
import java.util.List;

import com.chalmers.feedlr.activity.FeedActivity;
import com.chalmers.feedlr.client.TwitterHelper;
import com.chalmers.feedlr.listener.RequestListener;
import com.chalmers.feedlr.model.Feed;
import com.chalmers.feedlr.model.TwitterItem;
import com.chalmers.feedlr.model.User;
import com.chalmers.feedlr.parser.TwitterJSONParser;
import com.chalmers.feedlr.util.ClientStore;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class DataService extends Service {
	private final IBinder binder = new FeedServiceBinder();

	private LocalBroadcastManager lbm;

	private TwitterHelper twitter;

	public class FeedServiceBinder extends Binder {
		DataService getService() {
			return DataService.this;
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		lbm = LocalBroadcastManager.getInstance(DataService.this);

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

	public void updateTweetsByUser(final int userID) {
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

	public void updateFeed(final Feed feed) {
		runAsync(new Runnable() {
			@Override
			public void run() {
				final long time = System.currentTimeMillis();

				final List<User> twitterUsersInFeed = feed.getTwitterUsers();
				final List<TwitterItem> twitterItemsforUsers = new ArrayList<TwitterItem>();

				twitter.getTweetsForUsers(twitterUsersInFeed,
						new RequestListener() {
							private int responses = 0;

							@SuppressWarnings("unchecked")
							@Override
							public void onComplete(Object response) {
								if (response != null)
									twitterItemsforUsers
											.addAll((List<TwitterItem>) response);
								
								responses++;
								if (responses == twitterUsersInFeed.size())
									onAllComplete();
							}

							private void onAllComplete() {

								// save to database

								// Broadcast update to activity

								Intent intent = new Intent();
								intent.setAction(FeedActivity.FEED_UPDATED);
								Bundle b = new Bundle();
								b.putString("feedTitle", feed.getTitle());
								intent.putExtras(b);
								lbm.sendBroadcast(intent);

								Log.i(TwitterJSONParser.class.getName(),
										"Time in millis for complete update of feed \""
												+ feed.getTitle()
												+ "\" request: "
												+ (System.currentTimeMillis() - time));
							}
						});
			}
		});
	}
}