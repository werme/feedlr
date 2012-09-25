package com.chalmers.feedlr.services;

import com.chalmers.feedlr.services.TwitterService.TwitterServiceBinder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class TwitterClient {
	private Context context;
	private TwitterService service;
	private boolean isBound;

	public TwitterClient(Context context) {
		this.context = context;		
		isBound = false;	
	}

	public void bindService() {
		Intent intent = new Intent(context, TwitterService.class);
		isBound = context.bindService(intent, connection, Context.BIND_AUTO_CREATE);

	}

	public void unbindService() {
		if(isBound) {
			context.unbindService(connection);
			isBound = false;
		}
	}
	
	public void doStuff() {
		if(!isBound) return;
		service.doStuff();
	}
	
	private ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			TwitterServiceBinder binder = (TwitterServiceBinder) service;
	        TwitterClient.this.service = binder.getService();
	        isBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			service = null;
		}
	};
}
