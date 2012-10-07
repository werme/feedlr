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

package com.chalmers.feedlr.util;

import org.scribe.model.Token;

import com.facebook.android.Facebook;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ClientStore {

	private static final String APP_NAME = "feedlr";

	private static final String TWITTER_ACCESS_TOKEN = "twitter_access_token";
	private static final String TWITTER_ACCESS_SECRET = "twitter_access_secret";
	private static final String TWITTER_REQUEST_TOKEN = "twitter_request_token";
	private static final String TWITTER_REQUEST_SECRET = "twitter_request_secret";

	private static final String FACEBOOK_ACCESS_TOKEN = "access_token";
	private static final String FACEBOOK_ACCESS_TOKEN_EXPIRES = "access_expires";

	/*
	 * Save the Twitter access token and secret to shared preferences.
	 */

	public static boolean saveTwitterAccessToken(Token token, Context context) {
		Editor editor = context.getSharedPreferences(APP_NAME,
				Context.MODE_PRIVATE).edit();
		editor.putString(TWITTER_ACCESS_TOKEN, token.getToken());
		editor.putString(TWITTER_ACCESS_SECRET, token.getSecret());
		return editor.commit();
	}

	/*
	 * Save the Twitter request token and secret to shared preferences.
	 */

	public static boolean saveTwitterRequestToken(Token token, Context context) {
		Editor editor = context.getSharedPreferences(APP_NAME,
				Context.MODE_PRIVATE).edit();
		editor.putString(TWITTER_REQUEST_TOKEN, token.getToken());
		editor.putString(TWITTER_REQUEST_SECRET, token.getSecret());
		return editor.commit();
	}

	/*
	 * Get Twitter access token from shared preferences.
	 * 
	 * @return copy of access token
	 */
	public static Token getTwitterAccessToken(Context context) {
		SharedPreferences settings = context.getSharedPreferences(APP_NAME,
				Context.MODE_PRIVATE);
		String accessToken = settings.getString(TWITTER_ACCESS_TOKEN, null);
		String accessSecret = settings.getString(TWITTER_ACCESS_SECRET, null);
		return new Token(accessToken, accessSecret);
	}

	/*
	 * Get Twitter request token from shared preferences.
	 * 
	 * @return copy of request token
	 */
	public static Token getTwitterRequestToken(Context context) {
		SharedPreferences settings = context.getSharedPreferences(APP_NAME,
				Context.MODE_PRIVATE);
		String requestToken = settings.getString(TWITTER_REQUEST_TOKEN, null);
		String requestSecret = settings.getString(TWITTER_REQUEST_SECRET, null);
		return new Token(requestToken, requestSecret);
	}

	/*
	 * Save the Facebook OAuth 2.0 access token to shared preferences.
	 */

	public static boolean saveFacebookAccessToken(Facebook facebook,
			Context context) {
		Editor editor = context.getSharedPreferences(APP_NAME,
				Context.MODE_PRIVATE).edit();
		editor.putString(FACEBOOK_ACCESS_TOKEN, facebook.getAccessToken());
		return editor.commit();
	}

	/*
	 * Save the Facebook OAuth 2.0 access expiration token to shared
	 * preferences.
	 */
	public static boolean saveFacebookAccessTokenExpires(Facebook facebook,
			Context context) {
		Editor editor = context.getSharedPreferences(APP_NAME,
				Context.MODE_PRIVATE).edit();
		editor.putLong(FACEBOOK_ACCESS_TOKEN_EXPIRES,
				facebook.getAccessExpires());
		return editor.commit();
	}

	/*
	 * Get Facebook OAuth 2.0 access token from shared preferences.
	 * 
	 * @return access token
	 */
	public static String getFacebookAccessToken(Context context) {
		SharedPreferences settings = context.getSharedPreferences(APP_NAME,
				Context.MODE_PRIVATE);
		String accessToken = settings.getString(FACEBOOK_ACCESS_TOKEN, null);
		return accessToken;
	}

	/*
	 * Get Facebook OAuth 2.0 access token expiration time from shared
	 * preferences. Time expressed in milliseconds
	 * 
	 * @return session expiration time
	 */
	public static Long getFacebookAccessTokenExpires(Context context) {
		SharedPreferences settings = context.getSharedPreferences(APP_NAME,
				Context.MODE_PRIVATE);
		long accessTokenExpires = settings.getLong(
				FACEBOOK_ACCESS_TOKEN_EXPIRES, 0);
		return accessTokenExpires;
	}
}
