package com.chalmers.feedlr.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

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
}
