/**
 * Class description
 * 
 * @author Olle Werme
 */

package com.chalmers.feedlr.client;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.chalmers.feedlr.listener.AuthListener;

public class ClientHandler {
	
	private TwitterAuthHelper twitterAuthHelper;

	public ClientHandler(Context context) {
		
		twitterAuthHelper = new TwitterAuthHelper(context);	
		// For every service...
	}
	
	public void authorize(int service, AuthListener listener) {
		switch (service) {
			case Clients.TWITTER: 
				twitterAuthHelper.authorize(listener); break;
	//		case Service.FACEBOOK:
	//			facebookAuthHelper.authorize(); break;
			default: 
				Log.w(getClass().getName(), "Unknown service");
		}
	}
	
	public boolean isAuthorized(int service) {
		switch (service) {
			case Clients.TWITTER: 
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
