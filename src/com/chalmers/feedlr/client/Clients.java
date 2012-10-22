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

import java.util.ArrayList;
import java.util.List;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import android.content.Context;
import android.util.Log;

import com.chalmers.feedlr.util.ClientStore;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;

public class Clients {
	public static final int TWITTER = 0;
	public static final int FACEBOOK = 1;

	// Twitter constants
	private static final String TWITTER_CONSUMER_KEY = "jvphpats1Hq3xEiZREoZw";
	private static final String TWITTER_CONSUMER_SECRET = "ERXqbK72CCGgZ4hR96PkSoe6ZciSd14VwQ2vsDdEtM";
	private static final String TWITTER_CALLBACK_URL = "feedlr://twitter";

	// Facebook constants
	private static final String FACEBOOK_APP_ID = "477102822323129";

	private static OAuthService twitter;
	private static Facebook facebook;
	private static AsyncFacebookRunner asyncFacebookRunner;

	private Clients() {
	}

	/*
	 * @return array of availible clients
	 */
	public static String[] getClients() {
		return new String[] { "Twitter", "Facebook" };
	}

	/*
	 * @return OAuthService for twitter
	 */
	public synchronized static OAuthService getTwitter() {
		if (twitter != null) {
			return twitter;
		}

		twitter = new ServiceBuilder().provider(TwitterApi.class)
				.apiKey(TWITTER_CONSUMER_KEY)
				.apiSecret(TWITTER_CONSUMER_SECRET)
				.callback(TWITTER_CALLBACK_URL).build();

		return twitter;
	}

	/*
	 * @return Facebook object
	 */
	public synchronized static Facebook getFacebook() {
		if (facebook != null) {
			return facebook;
		}

		facebook = new Facebook(FACEBOOK_APP_ID);
		return facebook;
	}

	/*
	 * @return Asynchronized facebookrunner to be used for asynchronized
	 * requests
	 */
	public synchronized static AsyncFacebookRunner getAsyncFacebookRunner() {
		if (asyncFacebookRunner != null) {
			return asyncFacebookRunner;
		}

		asyncFacebookRunner = new AsyncFacebookRunner(facebook);
		return asyncFacebookRunner;
	}

	/*
	 * Decides if a client is authorized or not
	 * 
	 * @See <code>Clients</code>
	 * 
	 * @param client integer representation of the client.
	 * 
	 * @param context context
	 */
	public synchronized static boolean isAuthorized(int client, Context context) {
		switch (client) {
		case TWITTER:
			Token accessToken = ClientStore.getTwitterAccessToken(context);
			return (accessToken.getToken() != null && accessToken.getSecret() != null);
		case FACEBOOK:
			return (ClientStore.getFacebookAccessToken(context) != null);
		default:
			Log.wtf("Client authorization", "Client does not exist");
			return false;
		}
	}

	/*
	 * Get all authorized clients
	 * 
	 * @See <code>Clients</code>
	 * 
	 * @param context context
	 * 
	 * @return list of integer representations of authorized clients
	 */
	public static List<Integer> getAuthorizedClients(Context context) {
		List<Integer> authorizedClients = new ArrayList<Integer>();

		if (isAuthorized(TWITTER, context)) {
			authorizedClients.add(TWITTER);
		}
		if (isAuthorized(FACEBOOK, context)) {
			authorizedClients.add(FACEBOOK);
		}

		return authorizedClients;
	}

}
