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

	static class ViewHolder {
		public ImageView source;
	}

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