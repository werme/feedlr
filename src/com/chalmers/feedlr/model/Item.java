package com.chalmers.feedlr.model;

public interface Item {

	public void setText(String text);

	public void setUser(User user);

	public User getUser();

	public String getText();

	public String getType();

	public String getTimestamp();

	public String getURL();

	public String getIMGURL();
}
