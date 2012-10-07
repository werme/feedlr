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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.os.Bundle;
import android.util.Log;

import com.chalmers.feedlr.model.FacebookItem;
import com.chalmers.feedlr.model.User;
import com.chalmers.feedlr.parser.FacebookJSONParser;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;

public class FacebookHelper {

	public static final String TIMELINE = "me/home";
	public static final String FRIENDS = "me/friends";
	public static final String FRIENDSLISTS = "me/friendslists";

	private String accessToken;

	AsyncFacebookRunner asyncFacebookRunner = new AsyncFacebookRunner(
			Clients.getFacebook());

	public FacebookHelper(String accessToken) {
		this.accessToken = accessToken;

	}

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

	public void getFriends(RequestListener listener) {
		long time = System.currentTimeMillis();

		Bundle params = new Bundle();
		params.putString("fields", "name, id");
		request(FRIENDS, params, listener);

		Log.i(FacebookJSONParser.class.getName(),
				"Timeline request time in millis: "
						+ (System.currentTimeMillis() - time));
	}

	public void getFriendsLists(RequestListener listener) {
		long time = System.currentTimeMillis();

		Bundle params = new Bundle();
		params.putString("fields", "members, name");
		request(FRIENDSLISTS, params, listener);

		Log.i(FacebookJSONParser.class.getName(),
				"Friendslists request time in millis: "
						+ (System.currentTimeMillis() - time));
	}

	public void getUserFeed(long userID, RequestListener listener) {
		long time = System.currentTimeMillis();

		// Can this be done smoother?
		String values = "uid(" + userID + "), feed";
		Bundle params = new Bundle();
		params.putString("fields", values);
		request(FRIENDS, params, listener);

		Log.i(FacebookJSONParser.class.getName(),
				"Friendslists request time in millis: "
						+ (System.currentTimeMillis() - time));
	}

	public void getFeedForUsers(List<User> facebookUsersInFeed,
			RequestListener listener) {

		for (final User user : facebookUsersInFeed) {
//			List<FacebookItem> feed = getUserFeed(user.getId(), listener);

		}
	}

	private void request(String requestURL, Bundle params,
			RequestListener listener) {
		asyncFacebookRunner.request(requestURL, params, listener);
	}

	// Gets small version of profile picture. Add ?type=large for big version
	public URL getProfileImageURL(String userId) {
		long time = System.currentTimeMillis();

		URL url = null;
		try {
			url = new URL("http://graph.facebook.com/" + userId + "/picture");
			Log.i(FacebookJSONParser.class.getName(),
					"Profile image URL request time in millis: "
							+ (System.currentTimeMillis() - time));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;

	}
}
