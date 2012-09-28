/**
 * Class description
 * 
 * @author Olle Werme
 */

package com.chalmers.feedlr.twitter;

import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.chalmers.feedlr.FeedActivity;
import com.chalmers.feedlr.listeners.AuthListener;
import com.chalmers.feedlr.listeners.RequestListener;
import com.chalmers.feedlr.services.ServiceDataStore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class TwitterHelper {
	
	private Context context;
	private OAuthService twitter;
	private AuthListener authListener;
		
	public TwitterHelper(Context context) {
		this.context = context;
		twitter = Twitter.getInstance();
	}
	
	public void setAuthListener(AuthListener listener) {
		authListener = listener;
	}
	
	public void request(String query, RequestListener listener) {
		Token accessToken = ServiceDataStore.getTwitterAccessToken(context);
		new TwitterRequest(twitter, query, accessToken, listener);
	}
	
	public void authorize() {
		new GetTwitterRequestTokenTask().execute();
	}
	 
	public void onAuthCallback(Intent data) {
		String verifier = (String) data.getExtras().get("oauth_verifier");
		new GetTwitterAccessTokenTask().execute(verifier);
	}
	
	public boolean isAuthorized() {
		// TODO
		return true;
	}
	
	private class GetTwitterRequestTokenTask extends AsyncTask<Void, Void, Token> {
        protected Token doInBackground(Void...params) {
        	return twitter.getRequestToken();
        }      
        protected void onPostExecute (Token requestToken) {
        	ServiceDataStore.saveTwitterRequestToken(requestToken, context);
        	new GetTwitterAuthUriTask().execute();
        }
    }
    private class GetTwitterAuthUriTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void...params) {
        	Token requestToken = ServiceDataStore.getTwitterRequestToken(context);
        	return twitter.getAuthorizationUrl(requestToken);
        }      
        protected void onPostExecute (String authURL) {    	
        	// Send to twitter authorization page for user input
            Intent intent = new Intent(context, TwitterWebActivity.class);
            intent.putExtra("URL", authURL);
            ((Activity) context).startActivityForResult(intent, FeedActivity.TWITTER_AUTHORIZATION);
        }
    }
    private class GetTwitterAccessTokenTask extends AsyncTask<String, Void, Token> {
        protected Token doInBackground(String...verifier) {
        	Token requestToken = ServiceDataStore.getTwitterRequestToken(context);
        	Token result = twitter.getAccessToken(requestToken, new Verifier(verifier[0]));
        	return result;
        }      
        protected void onPostExecute (Token accessToken) {
        	ServiceDataStore.saveTwitterAccessToken(accessToken, context);
        	authListener.onAuthorizationComplete();
        }
    }
}