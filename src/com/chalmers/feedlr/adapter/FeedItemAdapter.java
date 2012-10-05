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

import java.util.HashMap;
import java.util.List;

import com.chalmers.feedlr.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class FeedItemAdapter extends SimpleAdapter {

	Context context;
	private List<HashMap<String, String>> data;

	public FeedItemAdapter(Context context, List<HashMap<String, String>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null)
			convertView = View.inflate(context, R.layout.feed_item, null);

		View feedItem = convertView;

		TextView text = (TextView) convertView
				.findViewById(R.id.feed_item_text);
		//text.setText(data.get(position).getText());
		text.setText("data: " + data.get(position).get("text"));

		return (feedItem);
	}
}