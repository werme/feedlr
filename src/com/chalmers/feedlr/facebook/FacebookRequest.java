package com.chalmers.feedlr.facebook;

import com.facebook.android.AsyncFacebookRunner.RequestListener;

import android.os.Bundle;

public class FacebookRequest {

	public static final String FEED = "me/home";

	private RequestListener listener;

	public FacebookRequest(String requestURL, RequestListener listener) {
		this.listener = listener;

		if (requestURL.equals(FEED)) {
			getFeed();
		}
	}

	public void getFeed() {
		Bundle params = new Bundle();
		params.putString("fields",
				"type, link, from, message, picture, name, description, created_time");
		FacebookService.getAsyncFacebookRunner()
				.request(FEED, params, listener);
	}
}