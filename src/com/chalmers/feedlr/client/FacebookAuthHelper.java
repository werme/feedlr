/**
 * Class description
 * 
 * @author Daniel Larsson
 */

package com.chalmers.feedlr.client;

import com.chalmers.feedlr.listener.AuthListener;
import com.chalmers.feedlr.util.ClientStore;
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
		facebook = Clients.getFacebook();
	}

	// Get access token if there is one
	public void setAccessToken() {
		ClientStore.getFacebookAccessToken(context);

		String accessToken = ClientStore.getFacebookAccessToken(context);
		System.out.println("accessToken: " + accessToken);
		Long accessTokenExpires = ClientStore
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
					new String[] { "read_stream" }, Clients.FACEBOOK,

					new DialogListener() {
						@Override
						public void onComplete(Bundle values) {
							ClientStore.saveFacebookAccessToken(facebook,
									context);
							ClientStore.saveFacebookAccessTokenExpires(
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
		ClientStore.saveFacebookAccessToken(facebook, context);
		ClientStore.saveFacebookAccessTokenExpires(facebook, context);
	}

	public void onAuthCallback(int requestCode, int resultCode, Intent data) {
		authorizeCallback(requestCode, resultCode, data);
	}
}
