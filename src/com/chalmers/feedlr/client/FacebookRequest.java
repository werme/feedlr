/*
 * Copyright 2012 Feedlr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chalmers.feedlr.client;

import com.facebook.android.AsyncFacebookRunner.RequestListener;

import android.os.Bundle;

/**
 * Class description
 * 
 * @author Daniel Larsson
 */



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
		Clients.getAsyncFacebookRunner().request(FEED, params, listener);
	}
}