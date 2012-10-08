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

package com.chalmers.feedlr.ui;

import com.chalmers.feedlr.R;
import com.chalmers.feedlr.adapter.FeedAdapter;
import com.chalmers.feedlr.database.DatabaseHelper;
import com.chalmers.feedlr.database.FeedCursorLoader;
import com.chalmers.feedlr.model.Feed;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FeedFragment extends ListFragment implements LoaderCallbacks<Cursor> {

	public static FeedFragment newInstance(Bundle args) {
		FeedFragment pageFragment = new FeedFragment();
		pageFragment.setArguments(args);
		return pageFragment;
	}

	private FeedAdapter adapter;
	private String feedTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle arg = getArguments();
		feedTitle = arg.getString("title");
	}

	@Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

		String[] columns = new String[] { DatabaseHelper.ITEM_COLUMN_TEXT };
		int[] to = new int[] { R.id.feed_item_text };

		adapter = new FeedAdapter(getActivity(),
				R.layout.feed_item, null, columns, to,
				CursorAdapter.NO_SELECTION);

		setListAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.feed_layout, container, false);
		return view;
	}

	@Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new FeedCursorLoader(getActivity(), feedTitle);
    }

	@Override
	public void onLoadFinished(Loader<Cursor> arg0,
			Cursor data) {
		adapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);
	}
	
	public void update() {
		getLoaderManager().restartLoader(0, null, this);
	}
}