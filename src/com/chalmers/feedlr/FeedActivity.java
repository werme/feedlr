package com.chalmers.feedlr;

import com.chalmers.facebook.FacebookHelper;
import com.chalmers.twitter.TwitterHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class FeedActivity extends Activity {

	private TwitterHelper twitter;

	private FacebookHelper facebookHelper;

	private TextView twitterStatusTV;
	private TextView facebookStatusTV;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed_layout);

		twitterStatusTV = (TextView) findViewById(R.id.twitter_status);
		facebookStatusTV = (TextView) findViewById(R.id.facebook_status);
		initServiceHelpers();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.feed_layout, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (twitter.isAuthorized())
			twitterStatusTV.setText("Twitter is authorized!");
		if (facebookHelper.isAuthorized())
			facebookStatusTV.setText("Facebook is authorized!");

		// Check for different service callbacks here.

		if (isCallback() && !twitter.isAuthorized()) {

			if (getIntent().getData().getQueryParameter("oauth_verifier") != null) {
				twitterStatusTV.setText("Twitter is authorized!");
				twitter.onAuthCallback(getIntent().getData().getQueryParameter(
						"oauth_verifier"));
			}
		}
		facebookHelper.extendTokenIfNeeded(this, null);
	}

	private boolean isCallback() {
		return getIntent().getData() != null;
	}

	private void initServiceHelpers() {
		twitter = new TwitterHelper(this);
		facebookHelper = new FacebookHelper(this);

	}

	// This is called on "authorize twitter" button press
	public void authorizeTwitter(View v) {
		twitter.authorize();
	}

	public void authorizeFacebook(View v) {
		facebookHelper.init();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		facebookHelper.onAuthCallback(requestCode, resultCode, data);
	}
}
