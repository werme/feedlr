/**
 * Class description
 * 
 * @author Olle Werme
 */

package com.chalmers.feedlr.helpers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.chalmers.feedlr.facebook.FacebookAuthHelper;
import com.chalmers.feedlr.listeners.AuthListener;
import com.chalmers.feedlr.twitter.TwitterAuthHelper;
import com.chalmers.feedlr.util.Services;

public class ServiceHandler {

	private TwitterAuthHelper twitterAuthHelper;
	private FacebookAuthHelper facebookAuthHelper;

	public ServiceHandler(Context context) {

		twitterAuthHelper = new TwitterAuthHelper(context);
		facebookAuthHelper = new FacebookAuthHelper(context);
		// For every service...
	}

	public void authorize(int service, AuthListener listener) {
		switch (service) {
		case Services.TWITTER:
			twitterAuthHelper.authorize(listener);
			break;
		case Services.FACEBOOK:
			facebookAuthHelper.authorize(listener);
			break;
		default:
			Log.w(getClass().getName(), "Unknown service");
		}
	}

	public boolean isAuthorized(int service) {
		switch (service) {
		case Services.TWITTER:
			return twitterAuthHelper.isAuthorized();
		case Services.FACEBOOK:
			return facebookAuthHelper.isAuthorized();
		default:
			Log.w(getClass().getName(), "Unknown service");
		}
		return false;
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