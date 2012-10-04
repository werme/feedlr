/**
 * Class description
 * 
 * @author Olle Werme
 */

package com.chalmers.feedlr.service;

import org.scribe.model.Token;

import com.chalmers.feedlr.activity.FeedActivity;
import com.chalmers.feedlr.client.Clients;
import com.chalmers.feedlr.client.TwitterAuthHelper;
import com.chalmers.feedlr.client.TwitterRequest;
import com.chalmers.feedlr.listener.RequestListener;
import com.chalmers.feedlr.util.ClientStore;
import com.chalmers.feedlr.util.TwitterJSONParser;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

public class FeedService extends Service {
	private final IBinder binder = new FeedServiceBinder();
	
	TwitterRequestListener listener = new TwitterRequestListener();
	
	TwitterAuthHelper twitter;

	public class FeedServiceBinder extends Binder {
		FeedService getService() {
			return FeedService.this;
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
		Token accessToken = ClientStore.getTwitterAccessToken(this);
		new TwitterRequest(Clients.getTwitter(), TwitterRequest.TIMELINE, accessToken, listener);
	}

	private class TwitterRequestListener implements RequestListener {

		@Override
		public void onComplete(String response) {
			
			// TODO make parse async
			TwitterJSONParser.parse(response);
			
			// Put stuff into database here
			LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(FeedService.this);

			Intent intent = new Intent();
			intent.setAction(FeedActivity.DATA_UPDATED);
			lbm.sendBroadcast(intent);
		}	
	}
}
