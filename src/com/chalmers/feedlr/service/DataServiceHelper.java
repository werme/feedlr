/**
 * Class description
 * 
 * @author Olle Werme
 */

package com.chalmers.feedlr.service;

import com.chalmers.feedlr.model.Feed;
import com.chalmers.feedlr.service.DataService.FeedServiceBinder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class DataServiceHelper {
	
	private Context context;
	
	private DataService dataService;

	private boolean isBound;

	public DataServiceHelper(Context context) {
		this.context = context;
		isBound = false;		
	}
	
	public void updateAll() {
		dataService.updateTwitterTimeline();
//		feedService.updateFacebooklbasldbasd();
	}
	
	public void updateUsers() {
		dataService.updateTwitterUsers();
//		feedService.updateFacebookUsers();
	}
	
	public void updateFeed(Feed feed) {
		dataService.updateFeed(feed);
	}
	
	public void startService() {
		Intent intent = new Intent(context, DataService.class);
		context.startService(intent);
	}
	public void stopService() {
		Intent intent = new Intent(context, DataService.class);
		context.stopService(intent);
	}
	
	public void bindService() {
		Intent intent = new Intent(context, DataService.class);
		isBound = context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
	}
	public void unbindService() {
		if(isBound) {
			context.unbindService(connection);
			isBound = false;
		}
	}
	
	private ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			FeedServiceBinder binder = (FeedServiceBinder) service;
	        dataService = binder.getService();
	        isBound = true;
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
			dataService = null;
		}
	};
}