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