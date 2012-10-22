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

import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Facebook item in a feed. Contains all the attributes that are
 * relevant for the feed.
 * 
 * @author Daniel Larsson
 */

public class FacebookItem implements Item {

	private String message;
	private String timestamp;
	private String id;
	private From from;

	public FacebookItem() {
		from = new From();
		from.setSource("facebook");
	}

	@Override
	@JsonProperty("message")
	public void setText(String message) {
		this.message = message;
	}

	/*
	 * @param timestamp the time the Facebook item was created
	 */
	@JsonProperty("updated_time")
	public void setTimestamp(String timestamp) {

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss+SSSS");
		dateFormat.setLenient(false);
		Date created = null;
		try {
			created = dateFormat.parse(timestamp);
		} catch (Exception e) {
			Log.e(getClass().getName(), e.getMessage());
		}

		SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.timestamp = d.format(created);
	}

	/**
	 * Returns the link to the Author of a post.
	 * 
	 * @param from
	 *            Link to the the author.
	 */
	public void setFrom(From from) {
		this.from = from;
	}

	@Override
	public String getText() {
		return message;
	}

	/*
	 * @return timestamp the time the Facebook item was created
	 */
	public Long getTimestamp() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date d = null;
		try {
			d = format.parse(this.timestamp);
		} catch (Exception e) {
			Log.e(getClass().getName(), e.getMessage());
		}

		return d.getTime();
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	/**
	 * Returns the author of the item as a From.
	 * 
	 * @return the user as a From
	 */
	public From getFrom() {
		return from;
	}

	/**
	 * From links the Author of the post.
	 * 
	 * @author Daniel Larsson
	 * 
	 */
	class From extends User {
		public From() {
		}

		/*
		 * @param userId the user id of a user
		 */
		public void setId(String id) {
			this.id = id;
		}

		@JsonProperty("name")
		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getUserName() {
			return userName;
		}

		public String getId() {
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
