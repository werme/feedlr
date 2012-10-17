package com.chalmers.feedlr.client;

import java.util.List;

import com.chalmers.feedlr.model.TwitterItem;
import com.chalmers.feedlr.model.User;

import android.test.AndroidTestCase;

public class TwitterHelperTest extends AndroidTestCase  {
	private TwitterHelper twitter;

	protected void setUp() throws Exception {
		super.setUp();
		twitter = new TwitterHelper(getContext());
	}
	
	public void testGetUserTweets() {
		long userID = 38895958;
		List<TwitterItem> userTweets = twitter.getUserTweets(userID);
		assertNotNull(userTweets);
	}
	
	public void testGetFollowing() {
		List<User> following = twitter.getFollowing();
		assertNotNull(following);
	}
}
