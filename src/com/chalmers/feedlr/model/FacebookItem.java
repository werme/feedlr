/**
 * Class description
 * 
 * @author Daniel Larsson
 */

package com.chalmers.feedlr.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FacebookItem implements Item {

	private String message;
	private User user;
	private String timestamp;
	private String type;
	private String name;
	private String userId;

	public FacebookItem() {
		user = new User();
	}

	@Override
	@JsonProperty("message")
	public void setText(String message) {
		this.message = message;
		System.out.println("Set message: " + message);
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setName(String name) {
		this.name = name;
		System.out.println("Set name: " + name);
	}

	@JsonProperty("created_time")
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public void setFrom(String[] from) {
		setName(from[0]);
		setUserId(from[1]);
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public String getText() {
		return message;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
