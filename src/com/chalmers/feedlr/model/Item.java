package com.chalmers.feedlr.model;

public interface Item {
	
	public void setText(String text);
	public void setUser(User user);
	public void setTimestamp(String timestamp);
	public User getUser();
	public String getText();
	public String getTimestamp();
}
