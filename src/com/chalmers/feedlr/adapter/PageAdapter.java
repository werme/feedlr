package com.chalmers.feedlr.adapter;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.chalmers.feedlr.database.DatabaseHelper;
import com.chalmers.feedlr.listener.FeedListener;
import com.chalmers.feedlr.model.Feed;
import com.chalmers.feedlr.ui.FeedFragment;

public class PageAdapter extends FragmentPagerAdapter {

	private int numberOfFeeds = 0;

	private List<String> feedTitles;

	private FeedListener listener;

	public PageAdapter(FragmentManager fm, DatabaseHelper db, FeedListener listener) {
		super(fm);
		this.listener = listener;
		
		db.clearFeeds();
		feedTitles = db.listFeeds();
		numberOfFeeds = feedTitles.size();
	}

	@Override
	public Fragment getItem(int index) {
		
		
		Bundle bundle = new Bundle();
		bundle.putString("title", feedTitles.get(index));
		FeedFragment f = FeedFragment.newInstance(bundle);
		f.setUpdateRequestListener(listener);
		return f;
	}

	@Override
	public int getCount() {
		return numberOfFeeds;
	}

	public void addFeed(Feed feed) {
		numberOfFeeds++;
		feedTitles.add(feed.getTitle());
	}
	
	public String getFeedTitle(int index) {
		return feedTitles.get(index);
	}
}