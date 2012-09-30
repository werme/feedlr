package com.chalmers.feedlr.activities;

import com.chalmers.feedlr.R;
import com.chalmers.feedlr.database.ItemDatabaseHelper;
import com.chalmers.feedlr.facebook.FacebookHelper;
import com.chalmers.feedlr.helpers.ServiceHandler;
import com.chalmers.feedlr.listeners.AuthListener;
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
import android.widget.Button;
import android.widget.Toast;

public class FeedActivity extends Activity {

	private FeedDataClient feedData;
	private ServiceHandler serviceHandler;
	
	// TODO implement this into the service handler
	private FacebookHelper facebookHelper;

	public static final String DATA_UPDATED = "com.chalmers.feedlr.DATA_UPDATED";

	private Resources res;
	private LocalBroadcastManager lbm;

	private Button facebookAuthButton;
	private Button twitterAuthButton;
	private Button updateButton;

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
        setContentView(R.layout.feed_layout);
        
        facebookAuthButton = (Button) findViewById(R.id.button_facebook);
        twitterAuthButton = (Button) findViewById(R.id.button_twitter);
        updateButton = (Button) findViewById(R.id.button_update);
        
        res = getResources();
        lbm = LocalBroadcastManager.getInstance(this);
        
        serviceHandler = new ServiceHandler(this);
        facebookHelper = new FacebookHelper(this);
        
        feedData = new FeedDataClient(this);
        feedData.startService();
        
        databaseTest();
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
    	facebookAuthButton.setText(isFacebookAuthorized ? 
        		res.getString(R.string.facebook_authorized) : 
        		res.getString(R.string.authorize_facebook));
        	
    	facebookAuthButton.setEnabled(!isFacebookAuthorized);
    	updateButton.setEnabled(isFacebookAuthorized);
		
    	
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
		@Override
		public void onAuthorizationComplete() {
			Toast.makeText(FeedActivity.this, "Twitter authorization successful", Toast.LENGTH_SHORT).show();
			twitterAuthButton.setText(res.getString(R.string.twitter_authorized));
			twitterAuthButton.setEnabled(false);
			updateButton.setEnabled(true);
		}
		@Override
		public void onAuthorizationFail() {
			// TODO Auto-generated method stub
			
		}
	}
	
	private void databaseTest() {
		this.deleteDatabase("ItemDatabase.db");
        ItemDatabaseHelper database = new ItemDatabaseHelper(this);

        Log.d("Adding ", "Adding rows");
        
        database.addItem("David Göransson", "Hej, David här är din body!", "12:47", "Facebook");
        database.addItem("Olle Werme", "Hej, Olle här är din body!", "13:37", "Twitter");
        
        Log.d("NumberOfItems: ", "" + database.getNumberOfItems());
        Log.d("String of ID", database.getRow(1));
	}
	
	// Methods called on button press. See feed_layout.xml
	public void authorizeFacebook(View v) {
		facebookHelper.init();
	}
	
	public void authorizeTwitter(View v) {
		serviceHandler.authorize(Services.TWITTER, new TwitterAuthListener());
	}
	public void updateFeed(View v) {
		feedData.update();		
	}
}