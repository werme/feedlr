package com.chalmers.feedlr.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ViewAnimator;

public class CreateFeedView extends ViewAnimator {

	public CreateFeedView(Context context) {
		super(context);
	}

	public CreateFeedView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setBackgroundColor(Color.DKGRAY);
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		
//		createFeedView = (ViewAnimator) findViewById(R.id.feed_view);
//		createFeedListView = (ListView) findViewById(R.id.create_feed_list);
//		createFeedListView.setAdapter(new ArrayAdapter<String>(this,
//				R.layout.feed_list_layout,
//				new String[] { "Facebook", "Twitter" }));
//
//		createFeedListView.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				Animation slideOutLeft = AnimationUtils.loadAnimation(
//						FeedActivity.this, R.anim.slide_out_left);
//				Animation slideInRight = AnimationUtils.loadAnimation(
//						FeedActivity.this, R.anim.slide_in_right);
//				createFeedView.setInAnimation(slideInRight);
//				createFeedView.setOutAnimation(slideOutLeft);
//				createFeedView.showNext();
//			}
//		});
	}

	@Override
	public void addView(View child, int index,
			android.view.ViewGroup.LayoutParams params) {
		// TODO Auto-generated method stub
		super.addView(child, index, params);
	}
}
