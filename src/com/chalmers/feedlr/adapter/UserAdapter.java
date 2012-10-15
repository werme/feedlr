package com.chalmers.feedlr.adapter;

import java.util.ArrayList;

import com.chalmers.feedlr.R;
import com.chalmers.feedlr.model.User;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

public class UserAdapter extends SimpleCursorAdapter {

	private Context context;

	public UserAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		// TODO Auto-generated constructor stub
	}

	
	
}