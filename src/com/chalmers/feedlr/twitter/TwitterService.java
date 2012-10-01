/**
 * Class description
 * 
 * @author Olle Werme
 */

package com.chalmers.feedlr.twitter;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.oauth.OAuthService;

public class TwitterService {
	private static final String CONSUMER_KEY = "jvphpats1Hq3xEiZREoZw";
	private static final String CONSUMER_SECRET = "ERXqbK72CCGgZ4hR96PkSoe6ZciSd14VwQ2vsDdEtM";
	private static final String CALLBACK_URL = "feedlr://twitter";
	
	private static OAuthService instance;
	
	private TwitterService() {}
	
	public synchronized static OAuthService getInstance() {
		if(instance != null)
			return instance;
		
		init();
		return instance;
	}
	
	private static void init() {
		instance = new ServiceBuilder()
        .provider(TwitterApi.class)
        .apiKey(CONSUMER_KEY)
        .apiSecret(CONSUMER_SECRET)
        .callback(CALLBACK_URL)
        .build();
	}
}
