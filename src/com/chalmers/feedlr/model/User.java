/**
 * Class description
 * 
 * @author Olle Werme
 */

package com.chalmers.feedlr.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
	private int id;
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
	
	/**
	 * @param id the user id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param userName the name to set
	 */
	@JsonProperty("name")
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @param id the url to set
	 */
	@JsonProperty("profile_image_url")
	public void setProfileImageURL(String profileImageURL) {
		this.profileImageURL = profileImageURL;
	}

	/**
	 * @param id the name of the user
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param id the url to the users profile image
	 */
	public String getProfileImageURL() {
		return profileImageURL;
	}

	/**
	 * @return the user id
	 */
	public int getId() {
		return id;
	}	
}
