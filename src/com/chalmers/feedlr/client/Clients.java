package com.chalmers.feedlr.client;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.oauth.OAuthService;

public class Clients {
	public static final int TWITTER = 0;
	public static final int FACEBOOK = 1;

	// Twitter constants
	private static final String TWITTER_CONSUMER_KEY = "jvphpats1Hq3xEiZREoZw";
	private static final String TWITTER_CONSUMER_SECRET = "ERXqbK72CCGgZ4hR96PkSoe6ZciSd14VwQ2vsDdEtM";
	private static final String TWITTER_CALLBACK_URL = "feedlr://twitter";

	// Facebook constants here

	private static OAuthService twitter;

	public static String[] getServices() {
		return new String[] { "Twitter", "Facebook" };
	}

	public synchronized static OAuthService getTwitter() {
		if (twitter != null)
			return twitter;

		twitter = new ServiceBuilder().provider(TwitterApi.class)
				.apiKey(TWITTER_CONSUMER_KEY)
				.apiSecret(TWITTER_CONSUMER_SECRET)
				.callback(TWITTER_CALLBACK_URL).build();

		return twitter;
	}
}
