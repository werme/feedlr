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
import java.util.Locale;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TwitterItem defines a item or post from twitter as a object. The TwitterItem
 * are created by the parser and then inserted and stored in the database.
 * 
 * @author Olle Werme
 */

public class TwitterItem implements Item {

	private String text;
	private User user;
	private String timestamp;
	private String id;

	public TwitterItem() {
		user = new User();
		user.setSource("twitter");
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void setUser(User user) {
		this.user = user;
		this.user.setSource("twitter");
	}

	/**
	 * Sets the timestamp of a Ttwitter Item.
	 * 
	 * @param timestamp
	 *            the time the Twitter item was created
	 */
	@JsonProperty("created_at")
	public void setTimestamp(String timestamp) {
		// Converts from the format to dateformat.
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);
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

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public String getText() {
		return text;
	}

	/**
	 * Returns the timestamp as a long.
	 * 
	 * @return timestamp the time the Twitter item was created
	 */
	public Long getTimestamp() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date d = null;
		try {
			d = format.parse(timestamp);
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
}
