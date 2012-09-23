package com.chalmers.feedlr;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class FeedActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_layout, menu);
        return true;
    }
}
