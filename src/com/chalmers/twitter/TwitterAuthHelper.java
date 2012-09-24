package com.chalmers.twitter;

import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;

public class TwitterAuthHelper {
	
	private static final String APP_NAME = "feedlr";
	
	private OAuthService twitterService;
	
	private Context context;
	private SharedPreferences settings;
	
	public TwitterAuthHelper(Context context, OAuthService twitterService) {
		this.context = context;
		this.settings = context.getSharedPreferences(APP_NAME, 0);
		this.twitterService = twitterService;
	}
	
	public void startProcess() {
		new GetTwitterRequestTokenTask().execute();
	}
	public void onCallback(String data) {
		new GetTwitterAccessTokenTask().execute(data);
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
	
	
	
	public Token getAccessToken() {
		String at = settings.getString("accessToken", null);
		String as = settings.getString("accessSecret", null);
		
		if(at != null && as != null)
			return new Token(at, as);
		
		return null;
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
            context.startActivity(intent);
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
