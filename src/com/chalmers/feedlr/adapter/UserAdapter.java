package com.chalmers.feedlr.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;

public class UserAdapter extends SimpleCursorAdapter {

	public UserAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		// TODO Auto-generated constructor stub
	}
}