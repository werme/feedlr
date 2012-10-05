/*
 * Copyright 2012 Feedlr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chalmers.feedlr.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class description
 * 
 * @author Olle Werme
 */

public class TwitterItem implements Item {

	private String text;
	private User user;
	private String timestamp;

	public TwitterItem() {
		user = new User();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chalmers.feedlr.model.Item#setText(java.lang.String)
	 */
	@Override
	public void setText(String text) {
		this.text = text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.chalmers.feedlr.model.Item#setUser(com.chalmers.feedlr.model.User)
	 */
	@Override
	public void setUser(User user) {
		this.user = user;
	}

	/*
	 * @param timestamp the time the Twitter item was created
	 */
	@JsonProperty("created_at")
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chalmers.feedlr.model.Item#getUser()
	 */
	@Override
	public User getUser() {
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chalmers.feedlr.model.Item#getText()
	 */
	@Override
	public String getText() {
		return text;
	}

	/*
	 * @return timestamp the time the Twitter item was created
	 */
	public String getTimestamp() {
		return timestamp;
	}
}
