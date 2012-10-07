package com.chalmers.feedlr.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;

import com.chalmers.feedlr.R;
import com.chalmers.feedlr.model.Feed;
import com.chalmers.feedlr.ui.FeedFragment;

public class PageAdapter extends FragmentPagerAdapter {

	private static final String TAG_TEXT = "text";
	
	private int numberOfFeeds = 3;

	private Context context;
	
	private List<String> feeds;

	public PageAdapter(FragmentManager fm, Context context /* ,database */) {
		super(fm);
		this.context = context;
		
		// set number of feeds to number of feeds in database
		// populate feeds ArrayList with feed titles from database
	}

	@Override
	public Fragment getItem(int index) {
		
		ArrayList<HashMap<String, String>> feedData = getFeedData(feeds.get(index));

		ListFragment f = FeedFragment.newInstance();

//		// Example cursor below
//		Cursor cursor = getContentResolver().query(People.CONTENT_URI, new String[] {People._ID, People.NAME, People.NUMBER}, null, null, null);
//      startManagingCursor(cursor);
//
//      // the desired columns to be bound
//      String[] columns = new String[] { People.NAME, People.NUMBER };
//      // the XML defined views which the data will be bound to
//      int[] to = new int[] { R.id.name_entry, R.id.number_entry };
//
//      // create the adapter using the cursor pointing to the desired data as well as the layout information
//      SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this, R.layout.list_example_entry, cursor, columns, to);
//		
//		// SET THIS ADAPTER AS YOUR LISTACTIVITY'S ADAPTER
//      setListAdapter(mAdapter);

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