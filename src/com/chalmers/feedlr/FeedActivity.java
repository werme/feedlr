package com.chalmers.feedlr;
import com.chalmers.feedlr.services.TwitterClient;
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
	private TwitterClient client;

	public static final String BROADCAST_ACTION = "com.chalmers.feedlr.BROADCAST_ACTION";

	private LocalBroadcastManager lbm;

	private BroadcastReceiver receiver;
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_layout);
        
        lbm = LocalBroadcastManager.getInstance(this);
        
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, "received", Toast.LENGTH_SHORT).show();
                Log.wtf(getClass().getName(), "Result callback from unknown intent");
            }
        };
        
        IntentFilter filter = new IntentFilter();
        filter.addAction(BROADCAST_ACTION);
        lbm.registerReceiver(receiver, filter);
        
        initServiceHelpers();
        client = new TwitterClient(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_layout, menu);
        return true;
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	client.bindService();
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	client.unbindService();
    }

    @Override
    protected void onPause() {
        lbm.unregisterReceiver(receiver);
        super.onPause();
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

	// This is called on "authorize twitter" button press
	public void authorizeTwitter(View v) {
        client.doStuff();
		//twitter.authorize();
	}
}