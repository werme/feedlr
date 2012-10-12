/*
 * Copyright 2012 Feedlr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chalmers.feedlr.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.chalmers.feedlr.activity.FeedActivity;
import com.chalmers.feedlr.client.FacebookHelper;
import com.chalmers.feedlr.client.TwitterHelper;
import com.chalmers.feedlr.database.DatabaseHelper;
import com.chalmers.feedlr.model.FacebookItem;
import com.chalmers.feedlr.listener.RequestListener;

import com.chalmers.feedlr.model.Feed;
import com.chalmers.feedlr.model.TwitterItem;
import com.chalmers.feedlr.model.User;
import com.chalmers.feedlr.parser.FacebookJSONParser;
import com.chalmers.feedlr.parser.TwitterJSONParser;
import com.chalmers.feedlr.util.ClientStore;
import com.facebook.android.FacebookError;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Class description
 * 
 * @author Olle Werme
 */

public class DataService extends Service {
	private final IBinder binder = new FeedServiceBinder();

	private LocalBroadcastManager lbm;

	private TwitterHelper twitter;
	private FacebookHelper facebook;

	private DatabaseHelper db;

	public class FeedServiceBinder extends Binder {
		DataService getService() {
			return DataService.this;
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		lbm = LocalBroadcastManager.getInstance(DataService.this);

		twitter = new TwitterHelper(ClientStore.getTwitterAccessToken(this));
		facebook = new FacebookHelper(ClientStore.getFacebookAccessToken(this));

		db = new DatabaseHelper(this);
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

				List<TwitterItem> twitterTimeline = twitter.getTimeline();

				// save to database
				db.clearItemTable(); // for testing purposes only
				db.addListOfItems(twitterTimeline);

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

				db.addUsers(users);

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

	public void updateFacebookUsers() {
		final long time = System.currentTimeMillis();

		facebook.getFriends(new com.facebook.android.AsyncFacebookRunner.RequestListener() {

			@Override
			public void onComplete(String response, Object state) {
				if (response != null) {
					List<User> facebookUsers = FacebookJSONParser
							.parseUsers(response);

					// save to database

					// Broadcast update to activity
					Intent intent = new Intent();
					intent.setAction(FeedActivity.FACEBOOK_USERS_UPDATED);
					lbm.sendBroadcast(intent);

					Log.i(FacebookJSONParser.class.getName(),
							"Time in millis for complete Facebook friends request: "
									+ (System.currentTimeMillis() - time));
				}
			}

			@Override
			public void onFacebookError(FacebookError e, final Object state) {
				Log.e("Parse", "Facebook Error:" + e.getMessage());
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					final Object state) {
				Log.e("Parse", "Resource not found:" + e.getMessage());
			}

			@Override
			public void onIOException(IOException e, final Object state) {
				Log.e("Parse", "Network Error:" + e.getMessage());
			}

			@Override
			public void onMalformedURLException(MalformedURLException e,
					final Object state) {
				Log.e("Parse", "Invalid URL:" + e.getMessage());
			}
		});
	}

	// No need to start a new thread, since Facebook makes the request Async
	// automatically when using AsyncFacebookRunner
	public void updateFacebookTimeline() {
		final long time = System.currentTimeMillis();

		facebook.getTimeline(new com.facebook.android.AsyncFacebookRunner.RequestListener() {

			@Override
			public void onComplete(String response, Object state) {
				if (response != null) {
					List<FacebookItem> facebookTimeline = FacebookJSONParser
							.parseFeed(response);

					// save to database

					// Broadcast update to activity
					Intent intent = new Intent();
					intent.setAction(FeedActivity.FACEBOOK_USERS_UPDATED);
					lbm.sendBroadcast(intent);

					Log.i(FacebookJSONParser.class.getName(),
							"Time in millis for complete Facebook friends request: "
									+ (System.currentTimeMillis() - time));
				}
			}

			@Override
			public void onFacebookError(FacebookError e, final Object state) {
				Log.e("Parse", "Facebook Error:" + e.getMessage());
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					final Object state) {
				Log.e("Parse", "Resource not found:" + e.getMessage());
			}

			@Override
			public void onIOException(IOException e, final Object state) {
				Log.e("Parse", "Network Error:" + e.getMessage());
			}

			@Override
			public void onMalformedURLException(MalformedURLException e,
					final Object state) {
				Log.e("Parse", "Invalid URL:" + e.getMessage());
			}
		});

		// List<FacebookItem> facebookTimeline = facebook.getTimeline();

		// Save to database

		// Broadcast update to activity
		Intent intent = new Intent();
		intent.setAction(FeedActivity.FACEBOOK_TIMELINE_UPDATED);
		lbm.sendBroadcast(intent);

		Log.i(FacebookJSONParser.class.getName(),
				"Time in millis for complete Facebook timeline request: "
						+ (System.currentTimeMillis() - time));

	}

	public void updateFacebookUserFeed(final long userID) {
		final long time = System.currentTimeMillis();

		facebook.getUserFeed(userID,
				new com.facebook.android.AsyncFacebookRunner.RequestListener() {

					@Override
					public void onComplete(String response, Object state) {
						if (response != null) {
							List<FacebookItem> facebookUsers = FacebookJSONParser
									.parseFeed(response);

							// save to database

							// Broadcast update to activity
							Intent intent = new Intent();
							intent.setAction(FeedActivity.FACEBOOK_USERS_UPDATED);
							lbm.sendBroadcast(intent);

							Log.i(FacebookJSONParser.class.getName(),
									"Time in millis for complete Facebook user feed request: "
											+ (System.currentTimeMillis() - time));
						}
					}

					@Override
					public void onFacebookError(FacebookError e,
							final Object state) {
						Log.e("Parse", "Facebook Error:" + e.getMessage());
					}

					@Override
					public void onFileNotFoundException(
							FileNotFoundException e, final Object state) {
						Log.e("Parse", "Resource not found:" + e.getMessage());
					}

					@Override
					public void onIOException(IOException e, final Object state) {
						Log.e("Parse", "Network Error:" + e.getMessage());
					}

					@Override
					public void onMalformedURLException(
							MalformedURLException e, final Object state) {
						Log.e("Parse", "Invalid URL:" + e.getMessage());
					}
				});
	}

	public void updateFeed(final Feed feed) {
		runAsync(new Runnable() {
			@Override
			public void run() {
				final long time = System.currentTimeMillis();

				final List<User> twitterUsersInFeed = db.getUsersInFeed(feed);
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

								db.addListOfItems(twitterItemsforUsers);

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