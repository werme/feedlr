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

	public FacebookItem() {
		user = new User();
	}

	@Override
	public void setText(String message) {
		this.message = message;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@JsonProperty("created_at")
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
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
