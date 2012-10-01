package com.chalmers.feedlr.activities;

import java.util.ArrayList;
import java.util.List;

import com.chalmers.feedlr.R;
import com.chalmers.feedlr.database.ItemDatabaseHelper;
import com.chalmers.feedlr.facebook.FacebookHelper;
import com.chalmers.feedlr.gui.DisplayItems;
import com.chalmers.feedlr.helpers.ServiceHandler;
import com.chalmers.feedlr.listeners.AuthListener;
import com.chalmers.feedlr.model.Item;
import com.chalmers.feedlr.model.TwitterItem;
import com.chalmers.feedlr.model.User;
import com.chalmers.feedlr.services.FeedDataClient;
import com.chalmers.feedlr.util.Services;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class FeedActivity extends Activity {

	private FeedDataClient feedData;
	private ServiceHandler serviceHandler;

	// TODO implement this into the service handler
	private FacebookHelper facebookHelper;

	public static final String DATA_UPDATED = "com.chalmers.feedlr.DATA_UPDATED";

	private Resources res;
	private LocalBroadcastManager lbm;

	private ViewFlipper flipper;

	private Button facebookAuthButton;
	private Button twitterAuthButton;
	private Button updateButton;
	private Button slideLeftButton;
	private Button slideRightButton;


	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Toast.makeText(context, "Feed data was updated", Toast.LENGTH_SHORT).show();
			Log.i(getClass().getName(), "Recieved broadcast!");
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flipper);

		facebookAuthButton = (Button) findViewById(R.id.button_facebook);
		twitterAuthButton = (Button) findViewById(R.id.button_twitter);
		updateButton = (Button) findViewById(R.id.button_update);
		slideLeftButton = (Button) findViewById(R.id.button_slide_left);
		slideRightButton = (Button) findViewById(R.id.button_slide_right);

		res = getResources();
		lbm = LocalBroadcastManager.getInstance(this);

		serviceHandler = new ServiceHandler(this);
		facebookHelper = new FacebookHelper(this);
		facebookHelper.init();

		feedData = new FeedDataClient(this);
		feedData.startService();

		databaseTest();

		flipper = (ViewFlipper) findViewById(R.id.flipper);
		
		

		slideLeftButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Animation slideInLeft = AnimationUtils.loadAnimation(FeedActivity.this, R.anim.slide_in_left);
				Animation slideOutRight = AnimationUtils.loadAnimation(FeedActivity.this, R.anim.slide_out_right);
				flipper.setInAnimation(slideInLeft);
				flipper.setOutAnimation(slideOutRight);
				flipper.showNext();      
			}
		});
		slideRightButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Animation slideOutLeft = AnimationUtils.loadAnimation(FeedActivity.this, R.anim.slide_out_left);
				Animation slideInRight = AnimationUtils.loadAnimation(FeedActivity.this, R.anim.slide_in_right);
				flipper.setInAnimation(slideInRight);
				flipper.setOutAnimation(slideOutLeft);
				flipper.showPrevious();      
			}
		});

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

		Log.i("sd", " " + facebookHelper.isAuthorized());
		boolean isFacebookAuthorized = facebookHelper.isAuthorized();
		facebookAuthButton.setText(isFacebookAuthorized ? 
				res.getString(R.string.facebook_authorized) : 
					res.getString(R.string.authorize_facebook));

		facebookAuthButton.setEnabled(!isFacebookAuthorized);

		boolean isTwitterAuthorized = serviceHandler.isAuthorized(Services.TWITTER);
		twitterAuthButton.setText(isTwitterAuthorized ? 
				res.getString(R.string.twitter_authorized) : 
					res.getString(R.string.authorize_twitter));

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
				serviceHandler.onTwitterAuthCallback(data); break;
			case Services.FACEBOOK:
				facebookHelper.onAuthCallback(requestCode, resultCode, data); break;
			default:
				Log.wtf(getClass().getName(), "Result callback from unknown intent");
			}
		}
	}

	private class TwitterAuthListener implements AuthListener {
		public void onAuthorizationComplete() {
			Toast.makeText(FeedActivity.this, "Twitter authorization successful", Toast.LENGTH_SHORT).show();
			twitterAuthButton.setText(res.getString(R.string.twitter_authorized));
			twitterAuthButton.setEnabled(false);
			updateButton.setEnabled(true);
		}

		public void onAuthorizationFail() {
			// TODO Auto-generated method stub

		}
	}

	private void databaseTest() {
		this.deleteDatabase("ItemDatabase.db");
		ItemDatabaseHelper database = new ItemDatabaseHelper(this);

		Log.d("Adding ", "Adding rows");
		List<Item> itemList = new ArrayList<Item>();
		
		for (int i = 0; i == 10; i++){
			TwitterItem t = new TwitterItem();
			t.setText("Post " + i);
			User u = new User();
			u.setUserName("User " + i);
			t.setUser(u);
			itemList.add(t);
		}
				
		database.addItem("David Göransson", "Hej, David här är din body!", "12:47", "Facebook");
		database.addItem("Olle Werme", "Hej, Olle här är din body!", "13:37", "Twitter");
		database.addListOfItems(itemList);

		Log.d("NumberOfItems: ", "" + database.getNumberOfItems());
		//Log.d("String of ID", database.getRow(1));
	}

	// Methods called on button press. See feed_layout.xml
	public void authorizeFacebook(View v) {
		facebookHelper.authorize();
	}

	public void authorizeTwitter(View v) {
		serviceHandler.authorize(Services.TWITTER, new TwitterAuthListener());
	}
	public void updateFeed(View v) {
		feedData.update();		
	}

	public void displayItems(View v){
		Intent intent = new Intent(this, DisplayItems.class);
		startActivity(intent);
	}
}