package com.chalmers.feedlr.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;

import com.chalmers.feedlr.R;
import com.chalmers.feedlr.fragments.FeedFragment;
import com.chalmers.feedlr.model.Feed;

public class FeedAdapter extends FragmentPagerAdapter {

	private int NUMBER_OF_FEEDS = 3;

	private static final String TAG_TEXT = "text";

	private Context context;

	public FeedAdapter(FragmentManager fm, Context context) {
		super(fm);
		this.context = context;
	}

	@Override
	public Fragment getItem(int index) {
		return getFeed(index);
	}

	private Fragment getFeed(int index) {

		ArrayList<HashMap<String, String>> feedData = getFeedData(index);

		ListFragment f = FeedFragment.newInstance("My Message " + index);

		FeedItemAdapter adapter = new FeedItemAdapter(context, feedData,
				R.layout.feed_item, new String[] { TAG_TEXT },
				new int[] { R.id.feed_item_text });

		f.setListAdapter(adapter);

		return f;
	}
	
	// For testing purposes only!
	private ArrayList<HashMap<String, String>> getFeedData(int index) {
		ArrayList<HashMap<String, String>> feedData = new ArrayList<HashMap<String, String>>();
		
		switch(index) {
			case 0: 
				HashMap<String, String> item = new HashMap<String, String>();
				item.put(TAG_TEXT, "awesome text");
				HashMap<String, String> item2 = new HashMap<String, String>();
				item.put(TAG_TEXT, "Another row!");
				feedData.add(item);
				feedData.add(item2);
				break;
			case 1: 
				HashMap<String, String> item3 = new HashMap<String, String>();
				item3.put(TAG_TEXT, "number two!");
				HashMap<String, String> item4 = new HashMap<String, String>();
				item4.put(TAG_TEXT, "Anasdasdasdher row!");
				feedData.add(item3);
				feedData.add(item4);
				break;
			default:
				HashMap<String, String> item5 = new HashMap<String, String>();
				item5.put(TAG_TEXT, "default");
				HashMap<String, String> item6 = new HashMap<String, String>();
				item6.put(TAG_TEXT, "default 2!");
				feedData.add(item5);
				feedData.add(item6);
		}
		
		return feedData;
	}

	@Override
	public int getCount() {
		return NUMBER_OF_FEEDS;
	}

	public void addFeed(Feed feed) {
		NUMBER_OF_FEEDS++;
	}
}