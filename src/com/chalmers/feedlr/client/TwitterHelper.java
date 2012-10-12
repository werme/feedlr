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

package com.chalmers.feedlr.client;

import java.util.ArrayList;
import java.util.List;

import org.scribe.exceptions.OAuthException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.chalmers.feedlr.listener.RequestListener;
import com.chalmers.feedlr.model.TwitterItem;
import com.chalmers.feedlr.model.User;
import com.chalmers.feedlr.parser.TwitterJSONParser;
import com.chalmers.feedlr.util.ClientStore;

import android.content.Context;
import android.util.Log;

public class TwitterHelper {

	public static final String CREDENTIALS = "https://api.twitter.com/1.1/account/verify_credentials.json";
	public static final String TIMELINE = "https://api.twitter.com/1/statuses/home_timeline.json?include_entities=false&exclude_replies=true&count=200&include_rts=false";	
	public static final String USER_IDS = "https://api.twitter.com/1.1/friends/ids.json?user_id=";
	public static final String USER_NAMES = "https://api.twitter.com/1.1/users/lookup.json?include_entities=false&user_id=";
	public static final String USER_TWEETS = "https://api.twitter.com/1.1/statuses/user_timeline.json?contributor_details=false&exclude_replies=true&trim_user=true&count=100&user_id=";

	private OAuthService twitter;
	private Context context;

	public TwitterHelper(Context context) {
		twitter = Clients.getTwitter();
		this.context = context;
	}

	public List<TwitterItem> getTimeline() {
		long time = System.currentTimeMillis();

		String response = request(TIMELINE);

		Log.i(TwitterJSONParser.class.getName(),
				"Timeline request time in millis: "
						+ (System.currentTimeMillis() - time));
		
		return new TwitterJSONParser().parseTweets(response);
	}

	public List<TwitterItem> getUserTweets(long userID) {
		long time = System.currentTimeMillis();
		StringBuilder url = new StringBuilder();
		url.append(USER_TWEETS).append(userID);

		String response = request(url.toString());

		Log.i(TwitterJSONParser.class.getName(),
				"User tweets request time in millis: "
						+ (System.currentTimeMillis() - time));

		return new TwitterJSONParser().parseTweets(response);
	}

	public List<User> getFollowing() {
		long time = System.currentTimeMillis();

		long userID = getAuthorizedUserID();
		
		StringBuilder url = new StringBuilder();
		url.append(USER_IDS).append(userID);
	
		String response = request(url.toString());

		Log.i(TwitterJSONParser.class.getName(), "ID request time in millis: "
				+ (System.currentTimeMillis() - time));

		String[] ids = new TwitterJSONParser().parseUserIDs(response);
		return getTwitterUserNamesFromID(ids);
	}

	private long getAuthorizedUserID() {
		long time = System.currentTimeMillis();

		String response = request(CREDENTIALS);

		Log.i(TwitterJSONParser.class.getName(), "Credentials request time in millis: "
				+ (System.currentTimeMillis() - time));

		return new TwitterJSONParser().parseCredentials(response);
	}

	private List<User> getTwitterUserNamesFromID(String[] ids) {
		final List<User> users = new ArrayList<User>();
		StringBuilder url = new StringBuilder();

		url.append(USER_NAMES);
		url.append(ids[0]);

		for (int i = 1; i < ids.length; i++) {
			if (i % 100 == 0) {
				String response = request(url.toString());
				users.addAll(new TwitterJSONParser().parseUserNames(response));
				url = new StringBuilder();
				url.append(USER_NAMES);
				url.append(ids[i]);
			} else {
				url.append(",").append(ids[i]);
			}
		}
		String response = request(url.toString());
		users.addAll(new TwitterJSONParser().parseUserNames(response));
		
		for (User u : users)
			u.setSource("twitter");

		return users;
	}

	public void getTweetsForUsers(List<User> twitterUsersInFeed,
			final RequestListener listener) {
		for (final User user : twitterUsersInFeed) {

			new Thread() {
				@Override
				public void run() {
					try {
						new Runnable() {
							@Override
							public void run() {
								List<TwitterItem> tweets = getUserTweets(user
										.getId());
								listener.onComplete(tweets);
							}
						}.run();
					} finally {

					}
				}
			}.start();
		}
	}

	private synchronized String request(String requestURL) {
		try {
			OAuthRequest request = new OAuthRequest(Verb.GET, requestURL);
			Token accessToken = ClientStore.getTwitterAccessToken(context);
			twitter.signRequest(accessToken, request);
			Response response = request.send();

			if (response.isSuccessful())
				return response.getBody();
			else
				Log.e(getClass().getName(), "Unsuccessful response returned");
		} catch (OAuthException e) {
			Log.e(getClass().getName(), "Problem establishing connection");
		}
		return null;
	}
}