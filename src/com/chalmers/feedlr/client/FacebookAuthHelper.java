/**
 * Class description
 * 
 * @author Daniel Larsson
 */

package com.chalmers.feedlr.client;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class FacebookAuthHelper {

	private static final String APP_NAME = "feedlr";

	private Context context;
	private SharedPreferences settings;
	private Facebook facebook;

	public FacebookAuthHelper(Context context, Facebook facebook) {
		this.facebook = facebook;
		this.context = context;
	}

	// Get access token if there is one
	public void setAccessToken() {
		this.settings = context.getSharedPreferences(APP_NAME, 0);
		String access_token = settings.getString("access_token", null);
		long expires = settings.getLong("access_expires", 0);
		if (access_token != null) {
			facebook.setAccessToken(access_token);
		}
		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}
	}

	public void authorize() {
		// Authorize if access token is expired
		if (!facebook.isSessionValid()) {
			facebook.authorize((Activity) context,
					new String[] { "read_stream" },
					Clients.FACEBOOK,

					new DialogListener() {
						@Override
						public void onComplete(Bundle values) {
							SharedPreferences.Editor editor = settings.edit();
							editor.putString("access_token",
									facebook.getAccessToken());
							editor.putLong("access_expires",
									facebook.getAccessExpires());
							editor.commit();
						}

						@Override
						public void onFacebookError(FacebookError error) {
						}

						@Override
						public void onError(DialogError e) {
						}

						@Override
						public void onCancel() {
						}
					});
		}
	}

	public void authorizeCallback(int requestCode, int resultCode, Intent data) {
		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	public void extendTokenIfNeeded() {
		facebook.extendAccessTokenIfNeeded(context, null);
	}
}
