/**
 * Class description
 * 
 * @author Daniel Larsson
 */

package com.chalmers.facebook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import com.chalmers.feedlr.parser.FacebookJSONParser;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.ServiceListener;
import com.facebook.android.FacebookError;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class FacebookHelper {

	private static final String APP_ID = "477102822323129";

	private FacebookAuthHelper authHelper;

	private AsyncFacebookRunner asyncFacebookRunner;
	private Facebook facebook;
	private FacebookJSONParser facebookJSONParser;

	public FacebookHelper(Context context) {
		facebook = new Facebook(APP_ID);
		authHelper = new FacebookAuthHelper(context, facebook);
		facebookJSONParser = new FacebookJSONParser();
	}

	public void init() {
		authHelper.setAccessToken();
		authHelper.authorize();
	}

	public void onAuthCallback(int requestCode, int resultCode, Intent data) {
		authHelper.authorizeCallback(requestCode, resultCode, data);
	}

	public void extendTokenIfNeeded(Context context,
			ServiceListener serviceListener) {
		authHelper.extendTokenIfNeeded();
	}

	public boolean isAuthorized() {
		return (facebook.isSessionValid());
	}

	public class FBRequestListener implements RequestListener {
		public void onComplete(final String response, final Object state) {
			facebookJSONParser.parseAndPrint(response);
		}

		@Override
		public void onFacebookError(FacebookError arg0, Object arg1) {
		}

		@Override
		public void onFileNotFoundException(FileNotFoundException arg0,
				Object arg1) {
		}

		@Override
		public void onIOException(IOException arg0, Object arg1) {
		}

		@Override
		public void onMalformedURLException(MalformedURLException arg0,
				Object arg1) {
		}
	}

	public void startFeed() {
		asyncFacebookRunner = new AsyncFacebookRunner(facebook);
		Bundle params = new Bundle();
		params.putString("fields",
				"type, link, from, message, picture, name, description, created_time");
		asyncFacebookRunner.request("me/home", params, new FBRequestListener());
	}
}
