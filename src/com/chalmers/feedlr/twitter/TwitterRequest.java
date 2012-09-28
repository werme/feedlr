/**
 * Class description
 * 
 * @author Olle Werme
 */

package com.chalmers.feedlr.twitter;

import org.scribe.exceptions.OAuthException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.chalmers.feedlr.parser.TwitterJSONParser;

import android.os.AsyncTask;
import android.util.Log;

public class TwitterRequest {

	public static final String VERIFY_CREDENTIALS = "https://api.twitter.com/1.1/account/verify_credentials.json";
	public static final String TIMELINE = "https://api.twitter.com/1/statuses/home_timeline.json?include_entities=false&exclude_replies=true&count=200&include_rts=false";
	private OAuthService service;
	private Token accessToken;

	public TwitterRequest(OAuthService service, String requestURL, Token accessToken) {
		this.service = service;
		this.accessToken = accessToken;

		new HTTPRequestTask().execute(requestURL);
	}

	private class HTTPRequestTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String...requestURL) {
			
			try {
				OAuthRequest request = new OAuthRequest(Verb.GET, requestURL[0]);
				service.signRequest(accessToken, request);
				Response response = request.send();
				
				if(response.isSuccessful())
					return response.getBody();
				else
					Log.e(getClass().getName(), "Unsuccessful response returned");
			} catch (OAuthException e) {
				Log.e(getClass().getName(), "Problem establishing connection");
			}
			
			return null;
		}      
		protected void onPostExecute (String response) {
			if(response != null)
				TwitterJSONParser.parse(response);				
		}
	}
}
