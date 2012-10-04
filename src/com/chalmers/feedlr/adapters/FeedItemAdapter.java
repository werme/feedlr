package com.chalmers.feedlr.adapters;

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