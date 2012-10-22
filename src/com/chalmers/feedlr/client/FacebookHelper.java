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

import java.util.List;

import android.os.Bundle;
import android.util.Log;

import com.chalmers.feedlr.model.User;
import com.chalmers.feedlr.parser.FacebookJSONParser;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;

public class FacebookHelper {

	// Constants
	public static final String TIMELINE = "me/home";
	public static final String FRIENDS = "me/friends";
	public static final String FRIENDSLISTS = "me/friendslists";

	private AsyncFacebookRunner asyncFacebookRunner;

	public FacebookHelper() {
		asyncFacebookRunner = new AsyncFacebookRunner(Clients.getFacebook());
	}

	/*
	 * Sends a requst to Facebook's graph API for the "timeline". This includes
	 * everything viewed on the user's /home screen in Facebook.
	 * 
	 * This method is currently not in use.
	 * 
	 * @param listener the listener to be used in the request
	 */
	public void getTimeline(RequestListener listener) {
		long time = System.currentTimeMillis();

		Bundle params = new Bundle();
		params.putString("fields",
				"type, link, from, message, picture, name, description, created_time");
		request(TIMELINE, params, listener);

		Log.i(FacebookJSONParser.class.getName(),
				"Timeline request time in millis: "
						+ (System.currentTimeMillis() - time));
	}

	/*
	 * Sends a request to Facebook's graph API for the registered user's
	 * friends.
	 * 
	 * @param listener the listener to be used in the request
	 */
	public void getFriends(RequestListener listener) {
		long time = System.currentTimeMillis();

		Bundle params = new Bundle();
		params.putString("fields", "name, id");
		request(FRIENDS, params, listener);

		Log.i(FacebookJSONParser.class.getName(),
				"Timeline request time in millis: "
						+ (System.currentTimeMillis() - time));
	}

	/*
	 * Sends a request to Facebook's graph API for the registered user's friends
	 * lists.
	 * 
	 * This method is currently not in use.
	 * 
	 * @param listener the listener to be used in the request
	 */
	public void getFriendsLists(RequestListener listener) {
		long time = System.currentTimeMillis();

		Bundle params = new Bundle();
		params.putString("fields", "members, name");
		request(FRIENDSLISTS, params, listener);

		Log.i(FacebookJSONParser.class.getName(),
				"Friendslists request time in millis: "
						+ (System.currentTimeMillis() - time));
	}

	/*
	 * Sends a request to Facebook's graph API for the feed of the user given as
	 * parameter. Returns only statuses, no likes, photos and other types of
	 * updates.
	 * 
	 * @param userID String containing the user id of the feed
	 * 
	 * @param listener the listener to be used in the request
	 */
	public void getUserFeed(String userID, RequestListener listener) {
		long time = System.currentTimeMillis();

		Bundle params = new Bundle();
		params.putString("uid", userID);
		params.putString("fields", "statuses");
		request(FRIENDS, params, listener);

		Log.i(FacebookJSONParser.class.getName(),
				"User feed request time in millis: "
						+ (System.currentTimeMillis() - time));
	}

	/*
	 * Uses <code>getUserFeed</code> to request the statuses of a list of users.
	 * 
	 * @param facebookUsersInFeed list of users
	 * 
	 * @param listener the listener to be used in the request
	 */
	public void getFeedsForUsers(List<User> facebookUsersInFeed,
			final RequestListener listener) {
		for (final User user : facebookUsersInFeed) {
			getUserFeed(user.getId(), listener);
		}
	}

	/*
	 * Is called by all request methods, to use an asynchronized request caller.
	 * 
	 * @param requestURL URL for the request
	 * 
	 * @param params Bundle of parameters needed for the request
	 * 
	 * @param listener the listener to be used in the request
	 */
	private void request(String requestURL, Bundle params,
			RequestListener listener) {
		asyncFacebookRunner.request(requestURL, params, listener);
	}

	/*
	 * Creates a URL of the profile picture for a specific user.
	 * 
	 * @param id String containing the user id
	 * 
	 * @return complete url for small version of profile picture
	 */
	public String getProfileImageURL(String id) {
		// Gets small version of profile picture. Add ?type=large for big
		// version
		return ("http://graph.facebook.com/" + id + "/picture");
	}
}
