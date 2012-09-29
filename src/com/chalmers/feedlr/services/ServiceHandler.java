package com.chalmers.feedlr.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.chalmers.feedlr.listeners.AuthListener;
import com.chalmers.feedlr.twitter.TwitterAuthHelper;
import com.chalmers.feedlr.util.Services;

public class ServiceHandler {
	
	private TwitterAuthHelper twitterAuthHelper;

	public ServiceHandler(Context context) {
		
		twitterAuthHelper = new TwitterAuthHelper(context);	
		// For every service...
	}
	
	public void authorize(int service, AuthListener listener) {
		switch (service) {
			case Services.TWITTER: 
				twitterAuthHelper.authorize(listener); break;
	//		case Service.FACEBOOK:
	//			facebookAuthHelper.authorize(); break;
			default: 
				Log.w(getClass().getName(), "Unknown service");
		}
	}
	
	public boolean isAuthorized(int service) {
		switch (service) {
			case Services.TWITTER: 
				return twitterAuthHelper.isAuthorized();
//			case Service.FACEBOOK:
//				return facebookAuthHelper.isAuthorized();
			default: 
				Log.w(getClass().getName(), "Unknown service");
		}
		return false;
	}

	public void onTwitterAuthCallback(Intent data) {
		twitterAuthHelper.onAuthCallback(data);
	}
}
