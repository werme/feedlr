package com.chalmers.feedlr.services;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;

public class TwitterClient {
	private Context context;
	private Messenger service;
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
	
	public void callService(View v) {
	    if (!isBound) return;
	    
	    Message msg = Message.obtain(null, TwitterService.MSG_TEST, 0, 0);
	    try {
	        service.send(msg);
	    } catch (RemoteException e) {
	        e.printStackTrace();
	    }
	}
	
	private ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
	        TwitterClient.this.service = new Messenger (service);
	        isBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			service = null;
			isBound = false;
		}
	};
}