package com.chalmers.feedlr.services;

import com.chalmers.feedlr.FeedActivity;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class TwitterService extends Service {
	private final IBinder binder = new TwitterServiceBinder();

	public class TwitterServiceBinder extends Binder {
		TwitterService getService() {
			return TwitterService.this;
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}

	public void doStuff() {
		LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
		
		Intent intent = new Intent();
		intent.setAction(FeedActivity.BROADCAST_ACTION);
		lbm.sendBroadcast(intent);
		
		Log.wtf(getClass().getName(), "Result callback from unknown intent");
	}
}
