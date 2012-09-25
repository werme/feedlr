package com.chalmers.feedlr;
import com.chalmers.feedlr.twitter.TwitterHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class FeedActivity extends Activity {
	
	public static final int TWITTER_AUTHORIZATION = 1;

	private TwitterHelper twitter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_layout);
        
        initServiceHelpers();
        
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
        twitter.authorize();
	}
}