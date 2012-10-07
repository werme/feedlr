package com.chalmers.feedlr.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;

import com.chalmers.feedlr.R;
import com.chalmers.feedlr.database.DatabaseHelper;
import com.chalmers.feedlr.model.Feed;
import com.chalmers.feedlr.model.TwitterItem;
import com.chalmers.feedlr.ui.FeedFragment;

public class PageAdapter extends FragmentPagerAdapter {

	private static final String TAG_TEXT = "text";

	private int numberOfFeeds = 1;

	private Context context;

	private List<String> feeds;

	private DatabaseHelper db;

	public PageAdapter(FragmentManager fm, Context context /* ,database */) {
		super(fm);
		this.context = context;

		db = new DatabaseHelper(context);

		// set number of feeds to number of feeds in database
		// populate feeds ArrayList with feed titles from database
	}

	@Override
	public Fragment getItem(int index) {

		ListFragment f = FeedFragment.newInstance();
		
		return f;
	}

	@Override
	public int getCount() {
		return numberOfFeeds;
	}

	public void addFeed(Feed feed) {
		numberOfFeeds++;
		feeds.add(feed.getTitle());
	}
}