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

package com.chalmers.feedlr.model;

import java.util.List;

import android.content.Context;

public class FeedHandler {

	private Context context;

	public FeedHandler(Context context) {
		this.context = context;
	}

	public void createFeed(String title, List<User> twitterUsers,
			List<User> facebookUsers) {
		// insert feed into database
	}

	public List<Feed> getAllFeeds() {
		// get feeds from database
		List<Feed> feeds = null;
		return feeds;
	}

	public Feed getFeed(String title) {
		// query database for feed with title
		Feed feed = null;
		return feed;
	}
}
