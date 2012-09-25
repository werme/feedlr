package com.chalmers.feedlr;

import com.chalmers.feedlr.services.TwitterClient;
import com.chalmers.feedlr.twitter.TwitterHelper;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class FeedActivity extends Activity {

	private TwitterHelper twitter;
	
	private TextView twitterStatusTV;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_layout);
        
        twitterStatusTV = (TextView) findViewById(R.id.twitter_status);
        initServiceHelpers();
        
        TwitterClient client = new TwitterClient(this);
        client.bindService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_layout, menu);
        return true;
    }
	@Override
    protected void onResume() {
        super.onResume();
        
        if (twitter.isAuthorized()) {
        	twitterStatusTV.setText("Twitter is authorized!");
        }

        // Check for different service callbacks here.
        
        if (isCallback() && !twitter.isAuthorized()) {  	   	
        	
        	if(getIntent().getData().getQueryParameter("oauth_verifier") != null) {
        		twitterStatusTV.setText("Twitter is authorized!");
	        	twitter.onAuthCallback(getIntent().getData().getQueryParameter("oauth_verifier"));
        	}
        }
    }

	private boolean isCallback() {
		return getIntent().getData() != null;
	}
	
	private void initServiceHelpers() {
		twitter = new TwitterHelper(this);
	}

	// This is called on "authorize twitter" button press
	public void authorizeTwitter(View v) {
        twitter.authorize();
	}
}