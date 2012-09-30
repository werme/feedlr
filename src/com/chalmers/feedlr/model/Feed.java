/**
 * Class description
 * 
 * @author Olle Werme
 */

package com.chalmers.feedlr.model;

import java.util.ArrayList;
import java.util.List;

public class Feed extends ArrayList<Item>{

	private static final long serialVersionUID = 8558900149051333875L;

	private String title;
	
	private List<User> twitterUsers;
	private List<User> facebookUsers;
	
	public Feed() {
		// Set a sweet default title
		setTitle("Yeah buddy");
	}
	
	public Feed(String title) {
		this.setTitle(title);
	}

	/**
	 * @param title the feed title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * @param twitterUsers list of all twitter users to be displayed in the feed
	 */
	public void setTwitterUsers(List<User> twitterUsers) {
		this.twitterUsers = twitterUsers;
	}
	
	/**
	 * @param facebookUsers list of all facebook users to be displayed in the feed
	 */
	public void setFacebookUsers(List<User> facebookUsers) {
		this.facebookUsers = facebookUsers;
	}
	
	/**
	 * @return the feed title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @return list of all twitter users registered to this feed
	 */
	public List<User> getTwitterUsers() {
		return twitterUsers;
	}
	
	/**
	 * @return list of all facebook users registered to this feed
	 */
	public List<User> getFacebookUsers() {
		return facebookUsers;
	}
}
