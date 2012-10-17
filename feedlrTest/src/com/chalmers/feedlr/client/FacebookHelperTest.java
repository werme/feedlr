package com.chalmers.feedlr.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.chalmers.feedlr.listener.AuthListener;
import com.chalmers.feedlr.model.TwitterItem;
import com.chalmers.feedlr.model.User;
import com.facebook.android.FacebookError;

import android.test.AndroidTestCase;

public class FacebookHelperTest extends AndroidTestCase {
	private FacebookHelper facebook;
	private FacebookAuthHelper facebookAuthHelper;

	protected void setUp() throws Exception {
		super.setUp();
		facebook = new FacebookHelper();
		facebookAuthHelper = new FacebookAuthHelper(getContext());
	}

	public void testGetFriends() {
		facebook.getFriends(new com.facebook.android.AsyncFacebookRunner.RequestListener() {

			@Override
			public void onComplete(String response, Object state) {
				System.out.println("---------- GetFriends ----------");
				assertNotNull(response);
			}

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
				fail("MalformedURLException in getFriends");
			}

			@Override
			public void onIOException(IOException e, Object state) {
				fail("IOException in getFriends");
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
				fail("FileNotFoundException in getFriends");
			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
				fail("FacebookError in getFriends");
			}
		});
	}

	public void testGetUserFeed() {
		long userID = 648855648;
		facebook.getUserFeed(userID,
				new com.facebook.android.AsyncFacebookRunner.RequestListener() {

					@Override
					public void onComplete(String response, Object state) {
						System.out.println("---------- GetUserFeed ----------");
						assertNotNull(response);
					}

					@Override
					public void onMalformedURLException(
							MalformedURLException e, Object state) {
						fail("MalformedURLException in getFriends");
					}

					@Override
					public void onIOException(IOException e, Object state) {
						fail("IOException in getFriends");
					}

					@Override
					public void onFileNotFoundException(
							FileNotFoundException e, Object state) {
						fail("FileNotFoundException in getFriends");
					}

					@Override
					public void onFacebookError(FacebookError e, Object state) {
						fail("FacebookError in getFriends");
					}

				});
	}
}
