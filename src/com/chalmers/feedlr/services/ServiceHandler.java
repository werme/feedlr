package com.chalmers.feedlr.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.chalmers.feedlr.listeners.AuthListener;
import com.chalmers.feedlr.twitter.TwitterAuthHelper;
import com.chalmers.feedlr.util.Services;

public class ServiceHandler {
		
	private Context context;
	
	private TwitterAuthListener twitterAuthListener = new TwitterAuthListener();
	
	private TwitterAuthHelper twitterAuthHelper;

	public ServiceHandler(Context context) {
		this.context = context;
		
		twitterAuthHelper = new TwitterAuthHelper(context);	
		// For every service...
	}
	
	public void authorize(int service) {
		switch (service) {
			case Services.TWITTER: 
				twitterAuthHelper.authorize(twitterAuthListener); break;
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

	private class TwitterAuthListener implements AuthListener {

		@Override
		public void onAuthorizationComplete() {
			Toast.makeText(context, "Twitter authorization successful", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onAuthorizationFail() {
			// TODO Auto-generated method stub
			
		}
		
	}

	public void onTwitterAuthCallback(Intent data) {
		twitterAuthHelper.onAuthCallback(data);
	}
}
