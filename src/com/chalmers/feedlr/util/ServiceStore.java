package com.chalmers.feedlr.util;

import org.scribe.model.Token;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ServiceStore {

	private static final String APP_NAME = "feedlr";

	private static final String TWITTER_ACCESS_TOKEN = "twitter_access_token";
	private static final String TWITTER_ACCESS_SECRET = "twitter_access_secret";
	private static final String TWITTER_REQUEST_TOKEN = "twitter_request_token";
	private static final String TWITTER_REQUEST_SECRET = "twitter_request_secret";

	public static boolean saveTwitterAccessToken(Token token, Context context) {
		Editor editor = context.getSharedPreferences(APP_NAME,
				Context.MODE_PRIVATE).edit();
		editor.putString(TWITTER_ACCESS_TOKEN, token.getToken());
		editor.putString(TWITTER_ACCESS_SECRET, token.getSecret());
		return editor.commit();
	}

	public static boolean saveTwitterRequestToken(Token token, Context context) {
		Editor editor = context.getSharedPreferences(APP_NAME,
				Context.MODE_PRIVATE).edit();
		editor.putString(TWITTER_REQUEST_TOKEN, token.getToken());
		editor.putString(TWITTER_REQUEST_SECRET, token.getSecret());
		return editor.commit();
	}

	public static Token getTwitterAccessToken(Context context) {
		SharedPreferences settings = context.getSharedPreferences(APP_NAME,
				Context.MODE_PRIVATE);
		String accessToken = settings.getString(TWITTER_ACCESS_TOKEN, null);
		String accessSecret = settings.getString(TWITTER_ACCESS_SECRET, null);
		return new Token(accessToken, accessSecret);
	}

	public static Token getTwitterRequestToken(Context context) {
		SharedPreferences settings = context.getSharedPreferences(APP_NAME,
				Context.MODE_PRIVATE);
		String requestToken = settings.getString(TWITTER_REQUEST_TOKEN, null);
		String requestSecret = settings.getString(TWITTER_REQUEST_SECRET, null);
		return new Token(requestToken, requestSecret);
	}
}
