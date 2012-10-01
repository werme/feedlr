package com.chalmers.feedlr.model;

import java.util.List;

import android.content.Context;

public class FeedHandler {
	
	private Context context;

	public FeedHandler(Context context) {
		this.context = context;
	}
	
	public void createFeed(String title, List<User> twitterUsers, List<User> facebookUsers) {
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
