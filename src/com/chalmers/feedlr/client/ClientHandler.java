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

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.chalmers.feedlr.listener.AuthListener;

/**
 * Class description
 * 
 * @author Olle Werme
 */

public class ClientHandler {

	private TwitterAuthHelper twitterAuthHelper;
	private FacebookAuthHelper facebookAuthHelper;

	public ClientHandler(Context context) {
		twitterAuthHelper = new TwitterAuthHelper(context);
		facebookAuthHelper = new FacebookAuthHelper(context);
		// For every service...
	}

	/*
	 * Sends an authorize call to the service given as a parameter.
	 */
	public void authorize(int service, AuthListener listener) {
		switch (service) {
		case Clients.TWITTER:
			twitterAuthHelper.authorize(listener);
			break;
		case Clients.FACEBOOK:
			facebookAuthHelper.authorize(listener);
			break;
		default:
			Log.w(getClass().getName(), "Unknown service");
		}
	}

	public void onTwitterAuthCallback(Intent data) {
		twitterAuthHelper.onAuthCallback(data);
	}

	public void onFacebookAuthCallback(int requestCode, int resultCode,
			Intent data) {
		facebookAuthHelper.onAuthCallback(requestCode, resultCode, data);
	}

	public void extendFacebookAccessTokenIfNeeded() {
		facebookAuthHelper.extendTokenIfNeeded();
	}
}
