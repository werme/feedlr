package com.chalmers.feedlr.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import android.os.Bundle;
import android.util.Log;

import com.chalmers.feedlr.model.FacebookItem;
import com.chalmers.feedlr.parser.FacebookJSONParser;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;

public class FacebookHelper {

	public static final String TIMELINE = "me/home";

	private String accessToken;

	FacebookRequestListener listener = new FacebookRequestListener();
	AsyncFacebookRunner asyncFacebookRunner = new AsyncFacebookRunner(
			Clients.getFacebook());

	public FacebookHelper(String accessToken) {
		this.accessToken = accessToken;

	}

	/* public List<FacebookItem> getTimeline() {
		long time = System.currentTimeMillis();

		Bundle params = new Bundle();
		params.putString("fields",
				"type, link, from, message, picture, name, description, created_time");
		request(TIMELINE, params, listener);

		Log.i(FacebookJSONParser.class.getName(),
				"Timeline request time in millis: "
						+ (System.currentTimeMillis() - time));
	} */

	private void request(String requestURL, Bundle params,
			RequestListener listener) {
		asyncFacebookRunner.request(requestURL, params, listener);
	}

	private class FacebookRequestListener implements
			com.facebook.android.AsyncFacebookRunner.RequestListener {

		@Override
		public void onComplete(String response, Object state) {
			FacebookJSONParser.parse(response);
		}

		@Override
		public void onFacebookError(FacebookError e, final Object state) {
			Log.e("stream", "Facebook Error:" + e.getMessage());
		}

		@Override
		public void onFileNotFoundException(FileNotFoundException e,
				final Object state) {
			Log.e("stream", "Resource not found:" + e.getMessage());
		}

		@Override
		public void onIOException(IOException e, final Object state) {
			Log.e("stream", "Network Error:" + e.getMessage());
		}

		@Override
		public void onMalformedURLException(MalformedURLException e,
				final Object state) {
			Log.e("stream", "Invalid URL:" + e.getMessage());
		}
	}
}
