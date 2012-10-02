/**
 * Class description
 * 
 * @author Daniel Larsson
 */

package com.chalmers.feedlr.facebook;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;

public class FacebookService {

	private static final String APP_ID = "477102822323129";

	private static Facebook instance;
	private static AsyncFacebookRunner asyncFacebookRunner;

	private FacebookService() {
	}

	public synchronized static Facebook getInstance() {
		if (instance != null)
			return instance;

		instance = new Facebook(APP_ID);
		return instance;
	}

	public synchronized static AsyncFacebookRunner getAsyncFacebookRunner() {
		if (asyncFacebookRunner != null)
			return asyncFacebookRunner;

		asyncFacebookRunner = new AsyncFacebookRunner(instance);
		return asyncFacebookRunner;
	}
}
