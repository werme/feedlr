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

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.oauth.OAuthService;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;

public class Clients {
	public static final int TWITTER = 0;
	public static final int FACEBOOK = 1;

	// Twitter constants
	private static final String TWITTER_CONSUMER_KEY = "jvphpats1Hq3xEiZREoZw";
	private static final String TWITTER_CONSUMER_SECRET = "ERXqbK72CCGgZ4hR96PkSoe6ZciSd14VwQ2vsDdEtM";
	private static final String TWITTER_CALLBACK_URL = "feedlr://twitter";

	// Facebook constants here
	private static final String FACEBOOK_APP_ID = "477102822323129";

	private static OAuthService twitter;
	private static Facebook facebook;
	private static AsyncFacebookRunner asyncFacebookRunner;

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

	public synchronized static Facebook getFacebook() {
		if (facebook != null)
			return facebook;

		facebook = new Facebook(FACEBOOK_APP_ID);
		return facebook;
	}

	public synchronized static AsyncFacebookRunner getAsyncFacebookRunner() {
		if (asyncFacebookRunner != null)
			return asyncFacebookRunner;

		asyncFacebookRunner = new AsyncFacebookRunner(facebook);
		return asyncFacebookRunner;
	}
}
