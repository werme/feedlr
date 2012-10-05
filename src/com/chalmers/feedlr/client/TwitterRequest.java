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

import org.scribe.exceptions.OAuthException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.chalmers.feedlr.listener.RequestListener;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Class description
 * 
 * @author Olle Werme
 */



public class TwitterRequest extends AsyncTask<String, Void, String> {

	public static final String VERIFY_CREDENTIALS = "https://api.twitter.com/1.1/account/verify_credentials.json";
	public static final String TIMELINE = "https://api.twitter.com/1/statuses/home_timeline.json?include_entities=false&exclude_replies=true&count=200&include_rts=false";
	public static final String USER_IDS = "https://api.twitter.com/1.1/friends/ids.json?screen_name=blueliine";
	public static final String USER_NAMES = "https://api.twitter.com/1.1/users/lookup.json?include_entities=false&user_id=";
	public static final String USER_TWEETS = "https://api.twitter.com/1.1/statuses/user_timeline.json?contributor_details=false&exclude_replies=true&trim_user=true&count=100&user_id=";

	private OAuthService service;
	private Token accessToken;

	private RequestListener listener;

	public TwitterRequest(OAuthService service, String requestURL,
			Token accessToken, RequestListener listener) {
		this.service = service;
		this.accessToken = accessToken;
		this.listener = listener;
		
		this.execute(requestURL);
	}

	protected String doInBackground(String... requestURL) {
		try {
			OAuthRequest request = new OAuthRequest(Verb.GET, requestURL[0]);
			service.signRequest(accessToken, request);
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

	protected void onPostExecute(String response) {
		listener.onComplete(response);
	}
}
