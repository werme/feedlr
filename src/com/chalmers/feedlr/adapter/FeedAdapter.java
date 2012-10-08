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

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FeedAdapter extends SimpleCursorAdapter {

	Context context;

	public FeedAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		// TODO Auto-generated constructor stub
	}


//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//
//		if (convertView == null)
//			convertView = View.inflate(context, R.layout.feed_item, null);
//
//		View feedItem = convertView;
//
//		TextView text = (TextView) convertView
//				.findViewById(R.id.feed_item_text);
//		//text.setText(data.get(position).getText());
//		text.setText("data: " + data.get(position).get("text"));
//
//		return (feedItem);
//	}
}