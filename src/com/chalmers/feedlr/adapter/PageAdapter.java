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

	private int numberOfFeeds = 3;

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

		// ArrayList<HashMap<String, String>> feedData =
		// getFeedData(feeds.get(index));

		ListFragment f = FeedFragment.newInstance();
		
		Cursor cursor = db.getAllItems();

		String[] columns = new String[] { DatabaseHelper.ITEM_COLUMN_TEXT };
		int[] to = new int[] { R.id.feed_item_text };

		FeedAdapter tweetAdapter = new FeedAdapter(context,
				R.layout.feed_item, cursor, columns, to,
				CursorAdapter.NO_SELECTION);

		f.setListAdapter(tweetAdapter);

		return f;
	}

	private ArrayList<HashMap<String, String>> getFeedData(String feedTitle) {
		ArrayList<HashMap<String, String>> feedData = new ArrayList<HashMap<String, String>>();

		// get feed

		return null;
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