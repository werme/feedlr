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
 * @author Daniel Larsson
 * 
 *         Represents a Facebook item in a feed. Contains all the attributes
 *         that are relevant for the feed.
 * 
 */

public class FacebookItem implements Item {

	private String message;
	private String timestamp;
	private String type;
	private String source = "facebook";
	private String id;
	private From from;

	public FacebookItem() {
		from = new From();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chalmers.feedlr.model.Item#setText(java.lang.String)
	 */
	@Override
	@JsonProperty("message")
	public void setText(String message) {
		this.message = message;
	}

	/*
	 * @param timestamp the time the Facebook item was created
	 */
	@JsonProperty("created_time")
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public void setFrom(From from) {
		this.from = from;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chalmers.feedlr.model.Item#getText()
	 */
	@Override
	public String getText() {
		return message;
	}

	/*
	 * @return timestamp the time the Facebook item was created
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/*
	 * @param type the type of Facebook Item
	 */
	public void setType(String type) {
		this.type = type;
	}

	/*
	 * @return type the type of Facebook Item
	 */
	public String getType() {
		return type;
	}

	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIMGURL() {
		return null;
	}

	@Override
	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public String getSource() {
		return source;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	public From getFrom() {
		return from;
	}

	class From extends User {
		private String userName;
		private long id;

		public From() {
		}

		/*
		 * @param userId the user id of a user
		 */
		public void setId(long id) {
			this.id = id;
		}

		@JsonProperty("name")
		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getUserName() {
			return userName;
		}

		public long getId() {
			return id;
		}
	}

	@Override
	public void setUser(User user) {
		this.from = (From) user;
	}

	@Override
	public User getUser() {
		return from;
	}
}
