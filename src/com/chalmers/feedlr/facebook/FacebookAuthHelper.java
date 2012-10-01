/**
 * Class description
 * 
 * @author Daniel Larsson
 */

package com.chalmers.feedlr.facebook;

import com.chalmers.feedlr.listeners.AuthListener;
import com.chalmers.feedlr.util.ServiceStore;
import com.chalmers.feedlr.util.Services;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class FacebookAuthHelper {

	private Context context;
	private Facebook facebook;
	private AuthListener authListener;

	public FacebookAuthHelper(Context context) {
		this.context = context;
		facebook = FacebookService.getInstance();
	}

	// Get access token if there is one
	public void setAccessToken() {
		ServiceStore.getFacebookAccessToken(context);

		String accessToken = ServiceStore.getFacebookAccessToken(context);
		Long accessTokenExpires = ServiceStore
				.getFacebookAccessTokenExpires(context);
		if (accessToken != null) {
			facebook.setAccessToken(accessToken);
		}
		if (accessTokenExpires != 0) {
			facebook.setAccessExpires(accessTokenExpires);
		}
	}

	public void authorize(AuthListener listener) {
		authListener = listener;
		setAccessToken();

		if (!isAuthorized()) {
			facebook.authorize((Activity) context,
					new String[] { "read_stream" }, Services.FACEBOOK,

					new DialogListener() {
						@Override
						public void onComplete(Bundle values) {
							ServiceStore.saveFacebookAccessToken(facebook,
									context);
							ServiceStore.saveFacebookAccessTokenExpires(
									facebook, context);
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
		authListener.onAuthorizationComplete();
	}

	public boolean isAuthorized() {
		return (facebook.isSessionValid());
	}

	public void authorizeCallback(int requestCode, int resultCode, Intent data) {
		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	public void extendTokenIfNeeded() {
		facebook.extendAccessTokenIfNeeded(context, null);
		ServiceStore.saveFacebookAccessToken(facebook, context);
		ServiceStore.saveFacebookAccessTokenExpires(facebook, context);
	}

	public void onAuthCallback(int requestCode, int resultCode, Intent data) {
		authorizeCallback(requestCode, resultCode, data);
	}
}
