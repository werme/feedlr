/**
 * Class description
 * 
 * @author Olle Werme
 */

package com.chalmers.feedlr.model;

public class TwitterItem implements Item {
	
	private String text;	
	private User user;
	
	public TwitterItem() {
		user = new User();
	}

	public void setText(String text) { 
		this.text = text; 
	}
	public void setUser(User user) {
		this.user = user;
	}
	public User getUser() {
		return user;
	}
	public String getText() { 
    	return text;
    }
}
