package com.chalmers.feedlr.model;

import java.util.ArrayList;

public class Feed extends ArrayList<Item>{

	private static final long serialVersionUID = 8558900149051333875L;

	private String title;
	
	public Feed() {
		// Set a sweet default title
		setTitle("Yeah buddy");
	}
	
	public Feed(String title) {
		this.setTitle(title);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
