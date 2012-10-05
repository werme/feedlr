package com.chalmers.feedlr.client;

import java.util.ArrayList;
import java.util.List;

import org.scribe.exceptions.OAuthException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.chalmers.feedlr.model.TwitterItem;
import com.chalmers.feedlr.model.User;
import com.chalmers.feedlr.util.TwitterJSONParser;

import android.util.Log;

public class TwitterHelper {

	private OAuthService twitter;
	private Token accessToken;

	public TwitterHelper(Token accessToken) {
		twitter = Clients.getTwitter();
		this.accessToken = accessToken;
	}

	public List<TwitterItem> getTimeline() {
		long time = System.currentTimeMillis();
		String response = request(TwitterRequest.TIMELINE);
		Log.i(TwitterJSONParser.class.getName(),
				"Timeline request time in millis: "
						+ (System.currentTimeMillis() - time));
		List<TwitterItem> timeline = TwitterJSONParser.parseTweets(response);
		return timeline;
	}

	public List<TwitterItem> getUserTweets(int userID) {
		long time = System.currentTimeMillis();
		StringBuilder url = new StringBuilder();
		url.append(TwitterRequest.USER_TWEETS).append(userID);
		String response = request(url.toString());
		Log.i(TwitterJSONParser.class.getName(),
				"User tweets request time in millis: "
						+ (System.currentTimeMillis() - time));
		List<TwitterItem> userTweets = TwitterJSONParser.parseTweets(response);
		return userTweets;
	}

	public List<User> getFollowing() {
		long time = System.currentTimeMillis();
		String response = request(TwitterRequest.USER_IDS);

		Log.i(TwitterJSONParser.class.getName(), "ID request time in millis: "
				+ (System.currentTimeMillis() - time));
		String[] ids = TwitterJSONParser.parseUserIDs(response);
		return getTwitterUserNamesFromID(ids);
	}

	private List<User> getTwitterUserNamesFromID(String[] ids) {
		final List<User> users = new ArrayList<User>();
		StringBuilder url = new StringBuilder();

		url.append(TwitterRequest.USER_NAMES);
		url.append(ids[0]);

		for (int i = 1; i < ids.length; i++) {
			if (i % 100 == 0) {
				String response = request(url.toString());
				users.addAll(TwitterJSONParser.parseUserNames(response));
				url = new StringBuilder();
				url.append(TwitterRequest.USER_NAMES);
				url.append(ids[i]);
			} else {
				url.append(",").append(ids[i]);
			}
		}
		String response = request(url.toString());
		users.addAll(TwitterJSONParser.parseUserNames(response));

		return users;
	}

	private String request(String requestURL) {
		try {
			OAuthRequest request = new OAuthRequest(Verb.GET, requestURL);
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