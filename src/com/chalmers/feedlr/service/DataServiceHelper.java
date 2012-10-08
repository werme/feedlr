/*
 * Copyright 2012 Feedlr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chalmers.feedlr.service;

import com.chalmers.feedlr.model.Feed;
import com.chalmers.feedlr.service.DataService.FeedServiceBinder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Class description
 * 
 * @author Olle Werme
 */

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
		// feedService.updateFacebooklbasldbasd();
	}

	public void updateUsers() {
		dataService.updateTwitterUsers();
		// dataService.updateFacebookUsers();
		// dataService.updateFacebookTimeline();
		// feedService.updateFacebookUsers();
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
		isBound = context.bindService(intent, connection,
				Context.BIND_AUTO_CREATE);
	}

	public void unbindService() {
		if (isBound) {
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