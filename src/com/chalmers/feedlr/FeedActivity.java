package com.chalmers.feedlr;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class FeedActivity extends Activity {

	private static final String CONSUMER_KEY = "jvphpats1Hq3xEiZREoZw";
	private static final String CONSUMER_SECRET = "ERXqbK72CCGgZ4hR96PkSoe6ZciSd14VwQ2vsDdEtM";
	private static final String CALLBACK_URL = "feedlr://twitter";
	
	private SharedPreferences settings;
	private OAuthService twitterService;
	
	private TextView twitterStatusTV;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_layout);
        
        settings = getSharedPreferences("feedlr", 0);
        twitterStatusTV = (TextView) findViewById(R.id.twitter_status);
        
        initServices();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_layout, menu);
        return true;
    }
	@Override
    protected void onResume() {
        super.onResume();
        
        // Horrid code below. You have been warned.
        if (getAccessToken().getToken() != null)
        	twitterStatusTV.setText("Twitter is authorized!");
        	
        if (isCallback() && getAccessToken().getToken() == null) {   
        	if(getIntent().getData().getQueryParameter("oauth_verifier") != null) {
	        	new GetTwitterAccessTokenTask().execute(getIntent().getData().getQueryParameter("oauth_verifier"));
        	}
        } 
    }

	private boolean isCallback() {
		return getIntent().getData() != null;
	}
	
	private void initServices() {
		twitterService = new ServiceBuilder()
	            .provider(TwitterApi.class)
	            .apiKey(CONSUMER_KEY)
	            .apiSecret(CONSUMER_SECRET)
	            .callback(CALLBACK_URL)
	            .build();
	}

	public void authorizeTwitter(View v) {
		if (getAccessToken().getToken() != null)
        	new GetTwitterRequestTokenTask().execute();
	}
	
	private void saveAccessToken(Token token) { 
    	SharedPreferences.Editor editor = settings.edit();
    	editor.putString("accessToken", token.getToken());
    	editor.putString("accessSecret", token.getSecret());
		editor.commit();
	}
	private void saveRequestToken(Token token) { 
    	SharedPreferences.Editor editor = settings.edit();
    	editor.putString("requestToken", token.getToken());
    	editor.putString("requestSecret", token.getSecret());
		editor.commit();
	}
	private Token getRequestToken() {
		Token at = new Token(settings.getString("requestToken", null), settings.getString("requestSecret", null));
		return at;
	}
	private Token getAccessToken() {
		Token at = new Token(settings.getString("accessToken", null), settings.getString("accessSecret", null));
		return at;
	}

	
	
    private class GetTwitterRequestTokenTask extends AsyncTask<Void, Void, Token> {
        protected Token doInBackground(Void...params) {
        	return twitterService.getRequestToken();
        }      
        protected void onPostExecute (Token requestToken) {
        	saveRequestToken(requestToken);
        	new GetTwitterAuthUriTask().execute();
        }
    }
    private class GetTwitterAuthUriTask extends AsyncTask<Void, Void, Uri> {
        protected Uri doInBackground(Void...params) {
        	return Uri.parse(twitterService.getAuthorizationUrl(getRequestToken()));
        }      
        protected void onPostExecute (Uri authUri) {
        	Intent intent = new Intent(Intent.ACTION_VIEW, authUri);
            startActivity(intent);
        }
    }
    private class GetTwitterAccessTokenTask extends AsyncTask<String, Void, Token> {
        protected Token doInBackground(String...verifier) {
        	Token result = twitterService.getAccessToken(new Token(settings.getString("requestToken", null), settings.getString("requestSecret", null)), new Verifier(verifier[0]));
        	return result;
        }      
        protected void onPostExecute (Token result) {
        	saveAccessToken(result);
        }
    }
}
