package com.chalmers.feedlr.activities;

import java.util.ArrayList;

import com.chalmers.feedlr.R;
import com.chalmers.feedlr.adapters.FeedAdapter;
import com.chalmers.feedlr.adapters.UsersAdapter;
import com.chalmers.feedlr.facebook.FacebookHelper;
import com.chalmers.feedlr.helpers.ServiceHandler;
import com.chalmers.feedlr.listeners.AuthListener;
import com.chalmers.feedlr.services.FeedDataClient;
import com.chalmers.feedlr.util.Services;
import com.chalmers.feedlr.model.Feed;
import com.chalmers.feedlr.model.User;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewAnimator;
import android.widget.ViewFlipper;

public class FeedActivity extends FragmentActivity {

	private FeedDataClient feedData;
	private ServiceHandler serviceHandler;

	// TODO implement this into the service handler
	private FacebookHelper facebookHelper;

	public static final String DATA_UPDATED = "com.chalmers.feedlr.DATA_UPDATED";

	private Resources res;
	private LocalBroadcastManager lbm;
	private LayoutInflater inflater;

	private ViewFlipper mainViewFlipper;
	private ViewPager feedViewSwiper;
	private ViewAnimator createFeedView;

	private FeedAdapter adapter;

	private Button facebookAuthButton;
	private Button twitterAuthButton;
	private Button updateButton;

	private Animation slideOutLeft;
	private Animation slideOutRight;
	private Animation slideInLeft;
	private Animation slideInRight;

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Toast.makeText(context, "Feed data was updated", Toast.LENGTH_SHORT)
					.show();
			Log.i(getClass().getName(), "Recieved broadcast!");
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flipper);

		// get helpers from android system
		res = getResources();
		lbm = LocalBroadcastManager.getInstance(this);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// find views inflated from xml
		mainViewFlipper = (ViewFlipper) findViewById(R.id.flipper);
		feedViewSwiper = (ViewPager) findViewById(R.id.feed_view_pager);
		createFeedView = (ViewAnimator) findViewById(R.id.feed_view);

		facebookAuthButton = (Button) findViewById(R.id.button_facebook);
		twitterAuthButton = (Button) findViewById(R.id.button_twitter);
		updateButton = (Button) findViewById(R.id.button_update);

		// set adapters
		adapter = new FeedAdapter(getSupportFragmentManager(), this);
		feedViewSwiper.setAdapter(adapter);

		// Instanciate client and service helpers
		serviceHandler = new ServiceHandler(this);
		facebookHelper = new FacebookHelper(this);
		facebookHelper.init();

		feedData = new FeedDataClient(this);
		feedData.startService();

		// load animations from res/anim
		slideOutLeft = AnimationUtils
				.loadAnimation(this, R.anim.slide_out_left);
		slideOutRight = AnimationUtils.loadAnimation(this,
				R.anim.slide_out_right);
		slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
		slideInRight = AnimationUtils
				.loadAnimation(this, R.anim.slide_in_right);

		// misc
		createFeedView.setInAnimation(slideInRight);
		createFeedView.setOutAnimation(slideOutLeft);
	}

	@Override
	protected void onDestroy() {
		feedData.stopService();
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
		feedData.bindService();
	}

	@Override
	protected void onStop() {
		feedData.unbindService();
		super.onStop();
	}

	@Override
	protected void onResume() {
		super.onResume();

		boolean isFacebookAuthorized = facebookHelper.isAuthorized();
		facebookAuthButton.setText(isFacebookAuthorized ? res
				.getString(R.string.facebook_authorized) : res
				.getString(R.string.authorize_facebook));

		facebookAuthButton.setEnabled(!isFacebookAuthorized);

		boolean isTwitterAuthorized = serviceHandler
				.isAuthorized(Services.TWITTER);
		twitterAuthButton.setText(isTwitterAuthorized ? res
				.getString(R.string.twitter_authorized) : res
				.getString(R.string.authorize_twitter));

		twitterAuthButton.setEnabled(!isTwitterAuthorized);
		updateButton.setEnabled(isTwitterAuthorized);

		IntentFilter filter = new IntentFilter();
		filter.addAction(DATA_UPDATED);
		lbm.registerReceiver(receiver, filter);

		facebookHelper.extendTokenIfNeeded(this, null);
	}

	@Override
	protected void onPause() {
		lbm.unregisterReceiver(receiver);
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.feed_layout, menu);
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {

			switch (requestCode) {
			case Services.TWITTER:
				serviceHandler.onTwitterAuthCallback(data);
				break;
			case Services.FACEBOOK:
				facebookHelper.onAuthCallback(requestCode, resultCode, data);
				break;
			default:
				Log.wtf(getClass().getName(),
						"Result callback from unknown intent");
			}
		}
	}

	private class TwitterAuthListener implements AuthListener {
		public void onAuthorizationComplete() {
			Toast.makeText(FeedActivity.this,
					"Twitter authorization successful", Toast.LENGTH_SHORT)
					.show();
			twitterAuthButton.setText(res
					.getString(R.string.twitter_authorized));
			twitterAuthButton.setEnabled(false);
			updateButton.setEnabled(true);
		}

		public void onAuthorizationFail() {
			// TODO Auto-generated method stub
		}
	}

	// Methods called on button press. See xml files.
	public void initCreateFeedView(View v) {
		ListView lv = new ListView(this);
		lv.setAdapter(new ArrayAdapter<String>(this, R.layout.client_list_item,
				Services.getServices()));

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ListView lv = (ListView) inflater.inflate(
						R.layout.user_list_layout, null);

				ArrayList<User> users = new ArrayList<User>();
				users.add(new User("Yeah Buddy"));
				users.add(new User("Bacon"));

				lv.setAdapter(new UsersAdapter(FeedActivity.this,
						R.layout.user_list_item, users));

				createFeedView.addView(lv);
				createFeedView.showNext();
			}
		});

		createFeedView.addView(lv);
		createFeedView.showNext();
	}

	public void toggleSettingsView(View v) {
		if (mainViewFlipper.getCurrentView().getId() == R.id.first) {
			mainViewFlipper.setInAnimation(slideInLeft);
			mainViewFlipper.setOutAnimation(slideOutRight);
			mainViewFlipper.showNext();
		} else {
			mainViewFlipper.setInAnimation(slideInRight);
			mainViewFlipper.setOutAnimation(slideOutLeft);
			mainViewFlipper.showPrevious();
		}
	}

	public void authorizeFacebook(View v) {
		facebookHelper.authorize();
	}

	public void authorizeTwitter(View v) {
		serviceHandler.authorize(Services.TWITTER, new TwitterAuthListener());
	}

	public void updateFeed(View v) {
		feedData.update();
	}

	public void addFeed(View v) {
		Feed feed = new Feed("Yeah Buddy");
		adapter.addFeed(feed);
	}
}