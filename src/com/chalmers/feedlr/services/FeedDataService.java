/**
 * Class description
 * 
 * @author Olle Werme
 */

package com.chalmers.feedlr.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.scribe.model.Token;

import com.chalmers.feedlr.activities.FeedActivity;
import com.chalmers.feedlr.facebook.FacebookRequest;
import com.chalmers.feedlr.listeners.RequestListener;
import com.chalmers.feedlr.twitter.TwitterAuthHelper;
import com.chalmers.feedlr.twitter.TwitterRequest;
import com.chalmers.feedlr.twitter.TwitterService;
import com.chalmers.feedlr.util.FacebookJSONParser;
import com.chalmers.feedlr.util.ServiceStore;
import com.chalmers.feedlr.util.TwitterJSONParser;
import com.facebook.android.FacebookError;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class FeedDataService extends Service {
	private final IBinder binder = new TwitterServiceBinder();

	TwitterRequestListener listener = new TwitterRequestListener();
	FacebookRequestListener facebookListener = new FacebookRequestListener();

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
		// updateTwitter();
		updateFacebook();
	}

	private void updateTwitter() {
		Token accessToken = ServiceStore.getTwitterAccessToken(this);
		new TwitterRequest(TwitterService.getInstance(),
				TwitterRequest.TIMELINE, accessToken, listener);
	}

	private void updateFacebook() {
		new FacebookRequest(FacebookRequest.FEED, facebookListener);
	}

	private class TwitterRequestListener implements RequestListener {

		@Override
		public void onComplete(String response) {

			// TODO make parse async
			TwitterJSONParser.parse(response);

			// Put stuff into database here

			LocalBroadcastManager lbm = LocalBroadcastManager
					.getInstance(FeedDataService.this);

			Intent intent = new Intent();
			intent.setAction(FeedActivity.DATA_UPDATED);
			lbm.sendBroadcast(intent);
		}
	}

	private class FacebookRequestListener implements
			com.facebook.android.AsyncFacebookRunner.RequestListener {

		@Override
		public void onComplete(String response, Object state) {

			// TODO make parse async
			FacebookJSONParser.parse(response);

			// Put stuff into database here

			LocalBroadcastManager lbm = LocalBroadcastManager
					.getInstance(FeedDataService.this);

			Intent intent = new Intent();
			intent.setAction(FeedActivity.DATA_UPDATED);
			lbm.sendBroadcast(intent);

		}

		public void onFacebookError(FacebookError e, final Object state) {
			Log.e("stream", "Facebook Error:" + e.getMessage());
		}

		public void onFileNotFoundException(FileNotFoundException e,
				final Object state) {
			Log.e("stream", "Resource not found:" + e.getMessage());
		}

		public void onIOException(IOException e, final Object state) {
			Log.e("stream", "Network Error:" + e.getMessage());
		}

		public void onMalformedURLException(MalformedURLException e,
				final Object state) {
			Log.e("stream", "Invalid URL:" + e.getMessage());
		}
	}
}
