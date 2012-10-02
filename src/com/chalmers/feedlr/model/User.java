/**
 * Class description
 * 
 * @author Olle Werme
 */

package com.chalmers.feedlr.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
	private String userName, profileImageURL;

	public User() {
	}

	public User(String userName) {
		this.userName = userName;
	}

	public User(String userName, String profileImageURL) {
		this.userName = userName;
		this.profileImageURL = profileImageURL;
	}

	@JsonProperty("name")
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@JsonProperty("profile_image_url")
	public void setProfileImageURL(String profileImageURL) {
		this.profileImageURL = profileImageURL;
	}

	public String getUserName() {
		return userName;
	}

	public String getProfileImageURL() {
		return profileImageURL;
	}
}
