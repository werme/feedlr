package com.chalmers.facebook;

import com.facebook.android.Facebook;
import com.facebook.android.Facebook.ServiceListener;

import android.content.Context;
import android.content.Intent;

public class FacebookHelper {

	private static final String APP_ID = "477102822323129";

	private FacebookAuthHelper authHelper;

	private Facebook facebook;

	public FacebookHelper(Context context) {
		facebook = new Facebook(APP_ID);
		authHelper = new FacebookAuthHelper(context, facebook);
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

}
