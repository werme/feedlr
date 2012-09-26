package com.chalmers.feedlr.services;

import com.chalmers.feedlr.FeedActivity;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class FeedDataService extends Service {
	private final IBinder binder = new TwitterServiceBinder();

	public class TwitterServiceBinder extends Binder {
		FeedDataService getService() {
			return FeedDataService.this;
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}

	public void updateData() {
		LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
		
		Intent intent = new Intent();
		intent.setAction(FeedActivity.DATA_UPDATED);
		lbm.sendBroadcast(intent);
		
		Log.i(getClass().getName(), "sent broadcast from updateData()");
	}
}
