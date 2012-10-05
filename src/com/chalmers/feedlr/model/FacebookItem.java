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
 */

public class FacebookItem implements Item {

	private String message;
	private User user;
	private String timestamp;
	private String type;
	private String name;
	private String userId;

	public FacebookItem() {
		user = new User();
	}

	@Override
	@JsonProperty("message")
	public void setText(String message) {
		this.message = message;
		System.out.println("Set message: " + message);
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setName(String name) {
		this.name = name;
		System.out.println("Set name: " + name);
	}

	@JsonProperty("created_time")
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public void setFrom(String[] from) {
		setName(from[0]);
		setUserId(from[1]);
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public String getText() {
		return message;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
