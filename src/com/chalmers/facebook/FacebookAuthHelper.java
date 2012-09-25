package com.chalmers.facebook;

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

	private Context context;
	private SharedPreferences settings;
	private Facebook facebook;

	public FacebookAuthHelper(Context context, Facebook facebook) {
		this.facebook = facebook;
		this.context = context;
	}

	// Get access token if there is one
	public void setAccessToken() {
		// Full�sning.
		settings = ((Activity) context).getPreferences(Context.MODE_PRIVATE);
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
			// M�ste l�sa detta p� ett snyggare s�tt.
			facebook.authorize((Activity) context,
					new String[] { "friends_status" },

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
