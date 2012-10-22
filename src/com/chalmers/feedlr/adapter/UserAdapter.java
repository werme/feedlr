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

import com.chalmers.feedlr.R;
import com.chalmers.feedlr.database.DatabaseHelper;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class UserAdapter extends SimpleCursorAdapter {

	Context context;
	DatabaseHelper db;

	public UserAdapter(Context context, int layout, Cursor c, String[] from,
			int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		this.context = context;
		this.db = new DatabaseHelper(context);
	}

	/*
	 * Holds the children views for recycling.
	 */
	static class ViewHolder {
		public ImageView source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.widget.SimpleCursorAdapter#bindView(android.view.View,
	 * android.content.Context, android.database.Cursor)
	 */
	@Override
	public void bindView(View v, Context context, Cursor c) {
		super.bindView(v, context, c);

		ViewHolder viewHolder = (ViewHolder) v.getTag();

		int colNumSource = c.getColumnIndex(DatabaseHelper.USER_COLUMN_SOURCE);
		if (c.getString(colNumSource).equals("facebook")) {
			viewHolder.source
					.setBackgroundResource(R.drawable.source_facebook_small);
		} else {
			viewHolder.source
					.setBackgroundResource(R.drawable.source_twitter_small);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.widget.ResourceCursorAdapter#newView(android.content
	 * .Context, android.database.Cursor, android.view.ViewGroup)
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		super.newView(context, cursor, parent);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View tempView = inflater.inflate(R.layout.user_list_item, null);

		// Find views
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.source = (ImageView) tempView
				.findViewById(R.id.user_item_source_image);

		tempView.setTag(viewHolder);
		return tempView;
	}
}