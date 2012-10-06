package com.chalmers.feedlr.activity;

import java.util.ArrayList;

import com.chalmers.feedlr.R;
import com.chalmers.feedlr.adapter.FeedAdapter;
import com.chalmers.feedlr.adapter.UsersAdapter;
import com.chalmers.feedlr.client.Clients;
import com.chalmers.feedlr.client.FacebookHelper;
import com.chalmers.feedlr.client.ClientHandler;
import com.chalmers.feedlr.database.databaseHelper;
import com.chalmers.feedlr.service.DataServiceHelper;
import com.chalmers.feedlr.listener.AuthListener;
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
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;
import android.widget.ViewFlipper;

public class FeedActivity extends FragmentActivity {

	private DataServiceHelper feedService;
	private ClientHandler clientHandler;

	// TODO implement this into the service handler
	private FacebookHelper facebookHelper;

	public static final String TWITTER_TIMELINE_UPDATED = "com.chalmers.feedlr.TWITTER_TIMELINE_UPDATED";
	public static final String TWITTER_USERS_UPDATED = "com.chalmers.feedlr.TWITTER_USERS_UPDATED";
	public static final String TWITTER_USER_TWEETS_UPDATED = "com.chalmers.feedlr.TWITTER_USER_TWEETS_UPDATED";
	public static final String FEED_UPDATED = "com.chalmers.feedlr.FEED_UPDATED";

	// Android system helpers
	private Resources res;
	private LocalBroadcastManager lbm;
	private LayoutInflater inflater;

	// Adapters
	private FeedAdapter adapter;
	private UsersAdapter userAdapter;

	// Views
	private ViewFlipper mainViewFlipper;
	private ViewPager feedViewSwiper;
	private ViewAnimator settingsViewFlipper;
	private ListView userListView;
	private LinearLayout userListLayout;

	private Button facebookAuthButton;
	private Button twitterAuthButton;
	private Button updateButton;

	private TextView feedTitleTextView;

	// Animations
	private Animation slideOutLeft;
	private Animation slideOutRight;
	private Animation slideInLeft;
	private Animation slideInRight;

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String broadcast = intent.getAction();
			Bundle b = intent.getExtras();

			String dialog;

			if (broadcast.equals(TWITTER_TIMELINE_UPDATED))
				dialog = "Twitter timeline updated!";
			else if (broadcast.equals(TWITTER_USERS_UPDATED))
				dialog = "Twitter users updated!";
			else if (broadcast.equals(TWITTER_USER_TWEETS_UPDATED))
				dialog = "Tweets for Twitter user with ID: "
						+ b.getInt("userID") + " updated!";
			else if (broadcast.equals(FEED_UPDATED))
				dialog = "Feed: " + b.getString("feedTitle") + " updated!";
			else
				dialog = "broadcast from unknown intent recieved!";

			Toast.makeText(context, dialog, Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view_flipper);

		// get helpers from android system
		res = getResources();
		lbm = LocalBroadcastManager.getInstance(this);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// find views inflated from xml
		mainViewFlipper = (ViewFlipper) findViewById(R.id.main_view_flipper);
		feedViewSwiper = (ViewPager) findViewById(R.id.feed_view_pager);
		settingsViewFlipper = (ViewAnimator) findViewById(R.id.settings_view_flipper);

		facebookAuthButton = (Button) findViewById(R.id.button_facebook);
		twitterAuthButton = (Button) findViewById(R.id.button_twitter);
		updateButton = (Button) findViewById(R.id.button_update);

		feedTitleTextView = (TextView) findViewById(R.id.feed_action_bar_title);

		// set adapters
		adapter = new FeedAdapter(getSupportFragmentManager(), this);
		feedViewSwiper.setAdapter(adapter);

		// Swipe testing, this is just a stub
		feedViewSwiper
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int feedIndex) {
						// String feedTitle = getFeedTitle(index);
						feedTitleTextView.setText("Feed: " + (feedIndex + 1));
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
						// TODO Auto-generated method stub

					}
				});

		// Instanciate client and service helpers
		clientHandler = new ClientHandler(this);
		facebookHelper = new FacebookHelper(this);
		facebookHelper.init();

		feedService = new DataServiceHelper(this);
		feedService.startService();

		// load animations from res/anim
		slideOutLeft = AnimationUtils
				.loadAnimation(this, R.anim.slide_out_left);
		slideOutRight = AnimationUtils.loadAnimation(this,
				R.anim.slide_out_right);
		slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
		slideInRight = AnimationUtils
				.loadAnimation(this, R.anim.slide_in_right);

		// misc
		settingsViewFlipper.setInAnimation(slideInRight);
		settingsViewFlipper.setOutAnimation(slideOutLeft);

		testDatabase();
	}

	public void testDatabase() {

		// Simple feed testing
		this.deleteDatabase("feedlrDatabase");
		databaseHelper db = new databaseHelper(this);
		Feed testFeed = new Feed("testfeed");
		db.addFeed(testFeed);
		// db.addFeed(testFeed);
		db.addFeed(testFeed);
		Log.d("Feeds:", "" + db.listFeeds());
		Log.d("FeedID:", "" + db.getFeedID(testFeed));
		// db.removeFeed(testFeed.getTitle());
		// Log.d("Feed removed:", testFeed.getTitle());
		Log.d("Feeds:", "" + db.listFeeds());

		// //Simple user testing
		// User testUser = new User(1, "David");
		// db.addUser(testUser);
		// db.addUser(new User(52, "Olle"));
		// //db.addFeed(testFeed);
		// db.addUser(testUser);
		// Log.d("Users:", "" + db.listUsers());
		// Log.d("UserID:", "" + db.getUserID(testUser));
		// db.removeUser(testUser);
		// Log.d("User removed:", testUser.getUserName());
		// Log.d("User:", "" + db.listUsers());

		User testUser = new User(1, "David");
		db.addUserToFeed(testUser, testFeed);
		db.addUserToFeed(new User(52, "Olle"), testFeed);
		db.addUserToFeed(testUser, testFeed);
		Log.d("Users:", "" + db.listUsers());
		Log.d("UserID:", "" + db.getUserID(testUser));
		Log.d("ListFeedUser:", "" + db.listFeedUser());
		db.removeFeed(testFeed);
		Log.d("User:", "" + db.listUsers());
		Log.d("ListFeedUser:", "" + db.listFeedUser());
	}

	@Override
	protected void onDestroy() {
		feedService.stopService();
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
		feedService.bindService();
	}

	@Override
	protected void onStop() {
		feedService.unbindService();
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

		boolean isTwitterAuthorized = clientHandler
				.isAuthorized(Clients.TWITTER);
		twitterAuthButton.setText(isTwitterAuthorized ? res
				.getString(R.string.twitter_authorized) : res
				.getString(R.string.authorize_twitter));

		twitterAuthButton.setEnabled(!isTwitterAuthorized);
		updateButton.setEnabled(isTwitterAuthorized);

		IntentFilter filter = new IntentFilter();
		filter.addAction(TWITTER_TIMELINE_UPDATED);
		filter.addAction(TWITTER_USERS_UPDATED);
		filter.addAction(TWITTER_USER_TWEETS_UPDATED);
		filter.addAction(FEED_UPDATED);
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
	public void onBackPressed() {
		if (mainViewFlipper.getCurrentView().getId() == R.id.settings_layout)
			if (settingsViewFlipper.getCurrentView().getId() == R.id.user_list_layout)
				settingsViewFlipper.showPrevious();
			else
				toggleSettingsView(null);
		else
			super.onBackPressed();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {

			switch (requestCode) {
			case Clients.TWITTER:
				clientHandler.onTwitterAuthCallback(data);
				break;
			case Clients.FACEBOOK:
				facebookHelper.onAuthCallback(requestCode, resultCode, data);
				break;
			default:
				Log.wtf(getClass().getName(),
						"Result callback from unknown intent");
			}
		}
	}

	// Methods called on button press below. See xml files.

	public void initCreateFeedView(View v) {
		userListLayout = (LinearLayout) inflater.inflate(
				R.layout.user_list_layout, null);
		userListView = (ListView) userListLayout
				.findViewById(R.id.user_list_view);

		// get users from database here

		// Stupid example
		ArrayList<User> users = new ArrayList<User>();
		users.add(new User(2, "Yeah Buddy"));
		users.add(new User(3, "Arne"));
		users.add(new User(4, "Holger"));
		users.add(new User(5, "Gottfrid"));
		users.add(new User(6, "Obama"));

		userAdapter = new UsersAdapter(this, R.layout.user_list_item, users);
		userListView.setAdapter(userAdapter);

		settingsViewFlipper.addView(userListLayout);
		settingsViewFlipper.showNext();
	}

	public void toggleSettingsView(View v) {
		int currentView = mainViewFlipper.getCurrentView().getId();
		int currentSettingView = settingsViewFlipper.getCurrentView().getId();

		if (currentView == R.id.main_layout) {
			if (currentSettingView == R.id.user_list_layout)
				settingsViewFlipper.showPrevious();
			mainViewFlipper.setInAnimation(slideInLeft);
			mainViewFlipper.setOutAnimation(slideOutRight);
			mainViewFlipper.showNext();
		} else {
			mainViewFlipper.setInAnimation(slideInRight);
			mainViewFlipper.setOutAnimation(slideOutLeft);
			mainViewFlipper.showPrevious();
		}
	}

	public void createFeed(View v) {
		toggleSettingsView(null);

		SparseBooleanArray checked = userListView.getCheckedItemPositions();
		EditText titleEditText = (EditText) userListLayout
				.findViewById(R.id.create_feed_action_bar_title);
		String feedTitle = titleEditText.getText().toString();

		Feed feed = new Feed(feedTitle);
		ArrayList<User> users = new ArrayList<User>();

		for (int i = 0; i < userListView.getCount(); i++)
			if (checked.get(i))
				users.add(userAdapter.getItem(i));

		// save user list as a feed in database here

		adapter.addFeed(feed);
		feedViewSwiper.setCurrentItem(adapter.getCount());

		Toast.makeText(FeedActivity.this, "Feed created: " + feed.getTitle(),
				Toast.LENGTH_SHORT).show();
	}

	public void authorizeTwitter(View v) {
		clientHandler.authorize(Clients.TWITTER, new AuthListener() {
			@Override
			public void onAuthorizationComplete() {
				Toast.makeText(FeedActivity.this,
						"Twitter authorization successful", Toast.LENGTH_SHORT)
						.show();
				twitterAuthButton.setText(res
						.getString(R.string.twitter_authorized));
				twitterAuthButton.setEnabled(false);
				updateButton.setEnabled(true);
			}

			@Override
			public void onAuthorizationFail() {
				Toast.makeText(FeedActivity.this,
						"Twitter authorization failed", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	public void authorizeFacebook(View v) {
		facebookHelper.authorize();
	}

	public void updateFeed(View v) {
		// feedService.updateAll();
		feedService.updateUsers();
	}
}