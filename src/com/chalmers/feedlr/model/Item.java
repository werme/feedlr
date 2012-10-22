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

/**
 * Interface to define a item or post from a social media.
 * 
 */
public interface Item {
	/**
	 * Sets the text of a item
	 * 
	 * @param text
	 *            the text of the item.
	 */
	public void setText(String text);

	/**
	 * Sets the author of the item.
	 * 
	 * @param user
	 *            the author of the item.
	 */
	public void setUser(User user);

	/**
	 * Sets the id of the item.
	 * 
	 * @param id
	 *            the id of the item.
	 */
	public void setId(String id);

	/**
	 * Returns the text of the item.
	 * 
	 * @return the text of the item as a string.
	 */
	public String getText();

	/**
	 * Returns the user of the item.
	 * 
	 * @return the author of the item as a User.
	 */
	public User getUser();

	/**
	 * Returns the time stamp of the item as a long.
	 * 
	 * @return the timestamp as a long.
	 */
	public Long getTimestamp();

	/**
	 * Returns the id of the item.
	 * 
	 * @return the id of the item as a String.
	 */
	public String getId();
}
