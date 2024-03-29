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

package com.chalmers.feedlr.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class description
 * 
 * @author Olle Werme
 */

public class Feed extends ArrayList<Item> {

	private static final long serialVersionUID = 8558900149051333875L;

	private String title;

	private List<User> twitterUsers;
	private List<User> facebookUsers;

	public Feed() {
		// Set a default title
		setTitle("Feed");
	}

	public Feed(String title) {
		if (title != null && !title.equals("")) {
			this.setTitle(title);
		} else {
			setTitle("Feed");
		}
	}

	/**
	 * @param title
	 *            the feed title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Sets the Twitter users of a feed.
	 * 
	 * @param twitterUsers
	 *            list of all twitter users to be displayed in the feed
	 */
	public void setTwitterUsers(List<User> twitterUsers) {
		this.twitterUsers = twitterUsers;
	}

	/**
	 * Sets the Facebook users of a feed.
	 * 
	 * @param facebookUsers
	 *            list of all facebook users to be displayed in the feed
	 */
	public void setFacebookUsers(List<User> facebookUsers) {
		this.facebookUsers = facebookUsers;
	}

	/**
	 * Returns the feed's title.
	 * 
	 * @return the feed title as a String.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Returns all the Twitter users in a feed.
	 * 
	 * @return list of all twitter users registered to this feed
	 */
	public List<User> getTwitterUsers() {
		return twitterUsers;
	}

	/**
	 * Returns all the Facebook users in a feed.
	 * 
	 * @return list of all facebook users registered to this feed
	 */
	public List<User> getFacebookUsers() {
		return facebookUsers;
	}
}
