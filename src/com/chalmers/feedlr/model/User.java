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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A class representing a user or an author of a post in a feed.
 * 
 * @author Olle Werme
 */

public class User {
	protected String id;
	protected String userName, profileImageURL, source;

	public User() {
	}

	public User(String id, String userName) {
		this.id = id;
		this.userName = userName;
	}

	public User(String id, String userName, String profileImageURL) {
		this.id = id;
		this.userName = userName;
		this.profileImageURL = profileImageURL;
	}

	/**
	 * @param id
	 *            the user id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param userName
	 *            the name to set
	 */
	@JsonProperty("name")
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @param id
	 *            the url to set
	 */
	@JsonProperty("profile_image_url")
	public void setProfileImageURL(String url) {
		this.profileImageURL = url;
	}

	/**
	 * @param id
	 *            the name of the user
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param id
	 *            the url to the users profile image
	 */
	public String getProfileImageURL() {
		return profileImageURL;
	}

	/**
	 * @return the user id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
}
