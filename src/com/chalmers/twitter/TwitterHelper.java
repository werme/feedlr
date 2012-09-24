package com.chalmers.twitter;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.oauth.OAuthService;

import android.content.Context;

public class TwitterHelper {
	
	private static final String CONSUMER_KEY = "jvphpats1Hq3xEiZREoZw";
	private static final String CONSUMER_SECRET = "ERXqbK72CCGgZ4hR96PkSoe6ZciSd14VwQ2vsDdEtM";
	private static final String CALLBACK_URL = "feedlr://twitter";

	private OAuthService twitterService;
	private TwitterAuthHelper authHelper;
	
	public TwitterHelper(Context context) {
		init();
		authHelper = new TwitterAuthHelper(context, twitterService);
	}
	
	public void init() {
		twitterService = new ServiceBuilder()
        .provider(TwitterApi.class)
        .apiKey(CONSUMER_KEY)
        .apiSecret(CONSUMER_SECRET)
        .callback(CALLBACK_URL)
        .build();
	}
	
	public void authorize() {
		authHelper.startProcess();
	}
	
	public void onAuthCallback(String data) {
		authHelper.onCallback(data);
	}
	
	public boolean isAuthorized() {
		return authHelper.getAccessToken() != null;
	}
}