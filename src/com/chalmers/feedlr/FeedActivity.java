package com.chalmers.feedlr;
import com.chalmers.feedlr.services.FeedDataClient;
import com.chalmers.feedlr.twitter.TwitterHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class FeedActivity extends Activity {
	
	public static final int TWITTER_AUTHORIZATION = 1;

	private TwitterHelper twitter;
	private FeedDataClient feedData;

	public static final String DATA_UPDATED = "com.chalmers.feedlr.DATA_UPDATED";

	private LocalBroadcastManager lbm;

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
        
        lbm = LocalBroadcastManager.getInstance(this);
        
        initServiceHelpers();
        feedData = new FeedDataClient(this);
        feedData.startService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_layout, menu);
        return true;
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
    protected void onPause() {
        lbm.unregisterReceiver(receiver);
        super.onPause();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	IntentFilter filter = new IntentFilter();
        filter.addAction(DATA_UPDATED);
        lbm.registerReceiver(receiver, filter);
    }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			
			switch (requestCode) {
				case TWITTER_AUTHORIZATION:
					twitter.onAuthCallback(data);
					break;
				default:
					Log.wtf(getClass().getName(), "Result callback from unknown intent");
			}
		}
	}
	
	private void initServiceHelpers() {
		twitter = new TwitterHelper(this);
	}

	// Methods called on button press. See feed_layout.xml
	public void authorizeTwitter(View v) {
		twitter.authorize();
	}
	public void updateFeed(View v) {
		feedData.update();		
	}
}