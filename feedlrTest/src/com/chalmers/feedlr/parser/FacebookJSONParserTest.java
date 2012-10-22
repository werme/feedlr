package com.chalmers.feedlr.parser;

import java.util.List;

import com.chalmers.feedlr.model.FacebookItem;
import com.chalmers.feedlr.model.User;

import android.test.AndroidTestCase;

public class FacebookJSONParserTest extends AndroidTestCase {

	private FacebookJSONParser parser;

	protected void setUp() throws Exception {
		super.setUp();
		parser = new FacebookJSONParser();
	}

	public void testParseFeed() {
		StringBuilder json = new StringBuilder();

		json.append("[{").append("\"id\":\"503993062\"").append(",")
				.append("\"statuses\":{").append("\"data\":[").append("{")
				.append("\"id\":\"10151115918458063\",").append("\"from\":{")
				.append("\"name\":\"Joel Ackerstierna\",")
				.append("\"id\":\"503993062\"").append("},")
				.append("\"message\":\"I am alive!\",")
				.append("\"update_time\":\"2012-10-14T15:05:15+0000\"")
				.append("}]");

		String jsonString = json.toString();

		List<FacebookItem> feed = parser.parseFeed(jsonString);
		assertNotNull(feed);

		FacebookItem item = feed.get(0);
		assertNotNull(item);
		assertEquals("I am alive!", item.getText());

		User user = item.getUser();
		assertNotNull(user);
		assertEquals("Joel Ackerstierna", user.getUserName());
		assertEquals("503993062", user.getId());
	}

	public void testParseUsers() {
		StringBuilder json = new StringBuilder();

		json.append("{").append("\"data\":[").append("{")
				.append("\"name\":\"Joel Ackerstierna\",")
				.append("\"id\":\"503993062\"").append("},").append("{")
				.append("\"name\":\"Niklas Logren\",")
				.append("\"id\":\"509320134\"").append("}]");

		String jsonString = json.toString();

		List<User> users = parser.parseUsers(jsonString);
		assertNotNull(users);

		User user = users.get(0);
		assertEquals("Joel Ackerstierna", user.getUserName());
		assertEquals("503993062", user.getId());
	}
}