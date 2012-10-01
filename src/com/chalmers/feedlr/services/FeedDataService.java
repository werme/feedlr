/**
 * Class description
 * 
 * @author Olle Werme
 */

package com.chalmers.feedlr.services;

import org.scribe.model.Token;

import com.chalmers.feedlr.activities.FeedActivity;
import com.chalmers.feedlr.listeners.RequestListener;
import com.chalmers.feedlr.twitter.Twitter;
import com.chalmers.feedlr.twitter.TwitterAuthHelper;
import com.chalmers.feedlr.twitter.TwitterRequest;
import com.chalmers.feedlr.util.ServiceStore;
import com.chalmers.feedlr.util.TwitterJSONParser;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

public class FeedDataService extends Service {
	private final IBinder binder = new TwitterServiceBinder();
	
	TwitterRequestListener listener = new TwitterRequestListener();
	
	TwitterAuthHelper twitter;

	public class TwitterServiceBinder extends Binder {
		FeedDataService getService() {
			return FeedDataService.this;
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}

	public void update() {
		
		// Have check authorized services here somehow
		updateTwitter();
	}
	
	private void updateTwitter() {
		Token accessToken = ServiceStore.getTwitterAccessToken(this);
		new TwitterRequest(Twitter.getInstance(), TwitterRequest.TIMELINE, accessToken, listener);
	}

	private class TwitterRequestListener implements RequestListener {

		@Override
		public void onComplete(String response) {
			
			// TODO make parse async
			TwitterJSONParser.parse(response);
			
			// Put stuff into database here
			
			LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(FeedDataService.this);

			Intent intent = new Intent();
			intent.setAction(FeedActivity.DATA_UPDATED);
			lbm.sendBroadcast(intent);
		}	
	}
}
