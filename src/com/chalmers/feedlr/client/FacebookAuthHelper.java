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

/**
 * Class description
 * 
 * @author Daniel Larsson
 * 
 *         This class provides help methods for Facebook authorization.
 */

public class FacebookAuthHelper {

	private Context context;
	private Facebook facebook;
	private AuthListener authListener;

	public FacebookAuthHelper(Context context) {
		this.context = context;
		facebook = Clients.getFacebook();
		facebook.extendAccessTokenIfNeeded(context, null);

		if (ClientStore.getFacebookAccessToken(context) != null) {
			authorize(new AuthListener() {

				@Override
				public void onAuthorizationComplete() {
				}

				@Override
				public void onAuthorizationFail() {
				}
			});
		}
		setAccessToken();
	}

	/**
	 * Looks for an access token in <code>ClientStore</code> and, if there is
	 * one, applies it to this session. Also sets expiration time of the token.
	 */
	public void setAccessToken() {
		String accessToken = ClientStore.getFacebookAccessToken(context);
		Long accessTokenExpires = ClientStore
				.getFacebookAccessTokenExpires(context);

		if (accessToken != null) {
			facebook.setAccessToken(accessToken);
		}

		if (accessTokenExpires != 0) {
			facebook.setAccessExpires(accessTokenExpires);
		}
	}

	/**
	 * Authorizes the session, if is not authorized.
	 * 
	 * @param listener
	 *            the listener to be used in the authorization request
	 */
	public void authorize(AuthListener listener) {
		authListener = listener;
		setAccessToken();

		if (!Clients.isAuthorized(Clients.FACEBOOK, context)) {
			facebook.authorize((Activity) context, new String[] {
					"read_stream", "read_friendlists" },
					Facebook.FORCE_DIALOG_AUTH,

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

	public void authorizeCallback(int requestCode, int resultCode, Intent data) {
		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	/**
	 * Uses variable shouldExtendAccessToken in
	 * <code>com.facebook.android.Facebook</code> to validate if the access
	 * token used is out dated. If so, the method will call
	 * <code>extendAccessToken</code>
	 */
	public void extendTokenIfNeeded() {
		if (!facebook.extendAccessTokenIfNeeded(context, null)) {
			ClientStore.saveFacebookAccessToken(facebook, context);
			ClientStore.saveFacebookAccessTokenExpires(facebook, context);
		}
	}

	public void onAuthCallback(int requestCode, int resultCode, Intent data) {
		authorizeCallback(requestCode, resultCode, data);
	}
}
