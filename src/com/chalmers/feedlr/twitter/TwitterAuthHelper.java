/**
 * Class description
 * 
 * @author Olle Werme
 */

package com.chalmers.feedlr.twitter;

import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.chalmers.feedlr.listeners.AuthListener;
import com.chalmers.feedlr.util.ServiceStore;
import com.chalmers.feedlr.util.Services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class TwitterAuthHelper {
	
	private Context context;
	private OAuthService twitter;
	private AuthListener authListener;
		
	public TwitterAuthHelper(Context context) {
		this.context = context;
		twitter = Twitter.getInstance();
	}
	
	public void authorize(AuthListener listener) {
		authListener = listener;
		new GetTwitterRequestTokenTask().execute();
	}
	
	public boolean isAuthorized() {
		Token accessToken = ServiceStore.getTwitterAccessToken(context);
		return (accessToken.getToken() != null && accessToken.getSecret() != null);
	}
	 
	public void onAuthCallback(Intent data) {
		String verifier = (String) data.getExtras().get("oauth_verifier");
		new GetTwitterAccessTokenTask().execute(verifier);
	}
	
	private class GetTwitterRequestTokenTask extends AsyncTask<Void, Void, Token> {
	        protected Token doInBackground(Void...params) {
        	return twitter.getRequestToken();
        }      
        protected void onPostExecute (Token requestToken) {
        	ServiceStore.saveTwitterRequestToken(requestToken, context);
        	new GetTwitterAuthUriTask().execute();
        }
    }
    private class GetTwitterAuthUriTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void...params) {
        	Token requestToken = ServiceStore.getTwitterRequestToken(context);
        	return twitter.getAuthorizationUrl(requestToken);
        }      
        protected void onPostExecute (String authURL) {    	
        	// Send to twitter authorization page for user input
            Intent intent = new Intent(context, TwitterWebActivity.class);
            intent.putExtra("URL", authURL);
            ((Activity) context).startActivityForResult(intent, Services.TWITTER);
        }
    }
    private class GetTwitterAccessTokenTask extends AsyncTask<String, Void, Token> {
        protected Token doInBackground(String...verifier) {
        	Token requestToken = ServiceStore.getTwitterRequestToken(context);
        	Token result = twitter.getAccessToken(requestToken, new Verifier(verifier[0]));
        	return result;
        }      
        protected void onPostExecute (Token accessToken) {
        	ServiceStore.saveTwitterAccessToken(accessToken, context);
        	authListener.onAuthorizationComplete();
        }
    }
}