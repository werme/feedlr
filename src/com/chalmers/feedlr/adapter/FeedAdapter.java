package com.chalmers.feedlr.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;

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