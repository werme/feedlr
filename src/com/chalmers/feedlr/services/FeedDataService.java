package com.chalmers.feedlr.services;

import org.scribe.model.Token;

import com.chalmers.feedlr.FeedActivity;
import com.chalmers.feedlr.listeners.RequestListener;
import com.chalmers.feedlr.parser.TwitterJSONParser;
import com.chalmers.feedlr.twitter.Twitter;
import com.chalmers.feedlr.twitter.TwitterHelper;
import com.chalmers.feedlr.twitter.TwitterRequest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

public class FeedDataService extends Service {
	private final IBinder binder = new TwitterServiceBinder();
	
	TwitterRequestListener listener = new TwitterRequestListener();
	
	TwitterHelper twitter;

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

	public void updateData() {
		updateTwitter();
	}
	
	private void updateTwitter() {
		Token accessToken = ServiceDataStore.getTwitterAccessToken(this);
		new TwitterRequest(Twitter.getInstance(), TwitterRequest.TIMELINE, accessToken, listener);
	}

	private class TwitterRequestListener implements RequestListener {

		@Override
		public void onComplete(String response) {
			TwitterJSONParser.parse(response);
			
			LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(FeedDataService.this);

			Intent intent = new Intent();
			intent.setAction(FeedActivity.DATA_UPDATED);
			lbm.sendBroadcast(intent);
		}	
	}
}
