/**
 * Class description
 * 
 * @author Olle Werme
 */

package com.chalmers.feedlr.twitter;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.oauth.OAuthService;

import android.content.Context;
import android.content.Intent;

public class TwitterHelper {
	
	private static final String CONSUMER_KEY = "jvphpats1Hq3xEiZREoZw";
	private static final String CONSUMER_SECRET = "ERXqbK72CCGgZ4hR96PkSoe6ZciSd14VwQ2vsDdEtM";
	private static final String CALLBACK_URL = "feedlr://twitter";

	private OAuthService service;
	private TwitterAuthHelper authHelper;
	
	public TwitterHelper(Context context) {
		init();
		authHelper = new TwitterAuthHelper(context, service);
	}
	
	public void init() {
		service = new ServiceBuilder()
        .provider(TwitterApi.class)
        .apiKey(CONSUMER_KEY)
        .apiSecret(CONSUMER_SECRET)
        .callback(CALLBACK_URL)
        .build();
	}
	
	public void request(String query) {
		new TwitterRequest(service, query, authHelper.getAccessToken());
	}
	
	public void authorize() {
		authHelper.startProcess();
	}
	
	public void onAuthCallback(Intent data) {
		authHelper.onCallback(data);
	}
	
	public boolean isAuthorized() {
		return authHelper.getAccessToken() != null;
	}
}