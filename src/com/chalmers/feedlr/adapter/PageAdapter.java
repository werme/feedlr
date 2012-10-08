package com.chalmers.feedlr.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.chalmers.feedlr.listener.FeedListener;
import com.chalmers.feedlr.model.Feed;
import com.chalmers.feedlr.ui.FeedFragment;

public class PageAdapter extends FragmentPagerAdapter {

	private static final String TAG_TEXT = "text";

	private int numberOfFeeds = 1;

	private List<Feed> feeds;

	private FeedListener listener;

	public PageAdapter(FragmentManager fm, FeedListener listener /* ,List<Feed> feeds */) {
		super(fm);
		this.listener = listener;
		
		feeds = new ArrayList<Feed>();
		feeds.add(new Feed("Yeah buddy"));
	}

	@Override
	public Fragment getItem(int index) {
		Bundle bundle = new Bundle();
		bundle.putString("title", feeds.get(index).getTitle());
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
		feeds.add(feed);
	}
}