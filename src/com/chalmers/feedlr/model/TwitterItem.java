/**
 * Class description
 * 
 * @author Olle Werme
 */

package com.chalmers.feedlr.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.AnnotationIntrospector.ReferenceProperty.Type;

public class TwitterItem implements Item {
	
	private String text;	
	private User user;
	private String timestamp;
	
	public TwitterItem() {
		user = new User();
	}

	@Override
	public void setText(String text) { 
		this.text = text; 
	}
	@Override
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
    	return text;
    }
	public String getTimestamp() {
		return timestamp;
	}

	@Override
	public String getType() {
		return "STATUS";
	}

	@Override
	public String getURL() {
		return null;
	}

	@Override
	public String getIMGURL() {
		return null;
	}
}
