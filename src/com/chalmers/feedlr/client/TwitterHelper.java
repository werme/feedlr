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
import com.chalmers.feedlr.parser.TwitterJSONParser;

import android.util.Log;

public class TwitterHelper {

	public static final String VERIFY_CREDENTIALS = "https://api.twitter.com/1.1/account/verify_credentials.json";
	public static final String TIMELINE = "https://api.twitter.com/1/statuses/home_timeline.json?include_entities=false&exclude_replies=true&count=200&include_rts=false";
	public static final String USER_IDS = "https://api.twitter.com/1.1/friends/ids.json?screen_name=blueliine";
	public static final String USER_NAMES = "https://api.twitter.com/1.1/users/lookup.json?include_entities=false&user_id=";
	public static final String USER_TWEETS = "https://api.twitter.com/1.1/statuses/user_timeline.json?contributor_details=false&exclude_replies=true&trim_user=true&count=100&user_id=";

	private OAuthService twitter;
	private Token accessToken;

	public TwitterHelper(Token accessToken) {
		twitter = Clients.getTwitter();
		this.accessToken = accessToken;
	}

	public List<TwitterItem> getTimeline() {
		long time = System.currentTimeMillis();
		
		String response = request(TIMELINE);
		
		Log.i(TwitterJSONParser.class.getName(),
				"Timeline request time in millis: "
						+ (System.currentTimeMillis() - time));
		
		return TwitterJSONParser.parseTweets(response);
	}

	public List<TwitterItem> getUserTweets(int userID) {
		long time = System.currentTimeMillis();
		
		StringBuilder url = new StringBuilder();
		url.append(USER_TWEETS).append(userID);
		
		String response = request(url.toString());
		
		Log.i(TwitterJSONParser.class.getName(),
				"User tweets request time in millis: "
						+ (System.currentTimeMillis() - time));
		
		return TwitterJSONParser.parseTweets(response);
	}

	public List<User> getFollowing() {
		long time = System.currentTimeMillis();
		
		String response = request(USER_IDS);

		Log.i(TwitterJSONParser.class.getName(), "ID request time in millis: "
				+ (System.currentTimeMillis() - time));
		
		String[] ids = TwitterJSONParser.parseUserIDs(response);
		return getTwitterUserNamesFromID(ids);
	}

	private List<User> getTwitterUserNamesFromID(String[] ids) {
		final List<User> users = new ArrayList<User>();
		StringBuilder url = new StringBuilder();

		url.append(USER_NAMES);
		url.append(ids[0]);

		for (int i = 1; i < ids.length; i++) {
			if (i % 100 == 0) {
				String response = request(url.toString());
				users.addAll(TwitterJSONParser.parseUserNames(response));
				url = new StringBuilder();
				url.append(USER_NAMES);
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