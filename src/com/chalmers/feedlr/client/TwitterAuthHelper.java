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

import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.chalmers.feedlr.activity.TwitterWebActivity;
import com.chalmers.feedlr.listener.AuthListener;
import com.chalmers.feedlr.util.ClientStore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

/**
 * Class description
 * 
 * @author Olle Werme
 */



public class TwitterAuthHelper {

	private Context context;
	private OAuthService twitter;
	private AuthListener authListener;

	public TwitterAuthHelper(Context context) {
		this.context = context;
		twitter = Clients.getTwitter();
	}

	public void authorize(AuthListener listener) {
		authListener = listener;
		new GetTwitterRequestTokenTask().execute();
	}

	public boolean isAuthorized() {
		Token accessToken = ClientStore.getTwitterAccessToken(context);
		return (accessToken.getToken() != null && accessToken.getSecret() != null);
	}

	public void onAuthCallback(Intent data) {
		String verifier = (String) data.getExtras().get("oauth_verifier");
		new GetTwitterAccessTokenTask().execute(verifier);
	}

	private class GetTwitterRequestTokenTask extends
			AsyncTask<Void, Void, Token> {
		protected Token doInBackground(Void... params) {
			return twitter.getRequestToken();
		}

		protected void onPostExecute(Token requestToken) {
			ClientStore.saveTwitterRequestToken(requestToken, context);
			new GetTwitterAuthUriTask().execute();
		}
	}

	private class GetTwitterAuthUriTask extends AsyncTask<Void, Void, String> {
		protected String doInBackground(Void... params) {
			Token requestToken = ClientStore.getTwitterRequestToken(context);
			return twitter.getAuthorizationUrl(requestToken);
		}

		protected void onPostExecute(String authURL) {
			// Send to twitter authorization page for user input
			Intent intent = new Intent(context, TwitterWebActivity.class);
			intent.putExtra("URL", authURL);
			((Activity) context).startActivityForResult(intent,
					Clients.TWITTER);
		}
	}

	private class GetTwitterAccessTokenTask extends
			AsyncTask<String, Void, Token> {
		protected Token doInBackground(String... verifier) {
			Token requestToken = ClientStore.getTwitterRequestToken(context);
			Token result = twitter.getAccessToken(requestToken, new Verifier(
					verifier[0]));
			return result;
		}

		protected void onPostExecute(Token accessToken) {
			ClientStore.saveTwitterAccessToken(accessToken, context);
			authListener.onAuthorizationComplete();
		}
	}
}