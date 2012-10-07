package com.chalmers.feedlr.ui;

import com.chalmers.feedlr.R;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FeedFragment extends ListFragment {

	public static FeedFragment newInstance() {
		FeedFragment pageFragment = new FeedFragment();
		return pageFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.feed_layout, container, false);
		return view;
	}
}