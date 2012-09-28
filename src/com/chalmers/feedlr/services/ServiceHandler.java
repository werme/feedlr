package com.chalmers.feedlr.services;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.chalmers.feedlr.listeners.AuthListener;
import com.chalmers.feedlr.twitter.TwitterHelper;

public class ServiceHandler {
	
	private Context context;
	
	private TwitterAuthListener twitterAuthListener = new TwitterAuthListener();
	
	private TwitterHelper twitter;

	public ServiceHandler(Context context) {
		this.context = context;
		
		twitter = new TwitterHelper(context);
		twitter.setAuthListener(twitterAuthListener);
	}
	
	public void authorizeTwitter() {
		twitter.authorize();
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
		twitter.onAuthCallback(data);
	}
}
