package com.chalmers.feedlr.parser;

import java.util.List;

import com.chalmers.feedlr.model.FacebookItem;

import android.test.AndroidTestCase;

public class FacebookJSONParserTest extends AndroidTestCase {

	private FacebookJSONParser parser;

	protected void setUp() throws Exception {
		super.setUp();
		parser = new FacebookJSONParser();
	}

	public void testParseFeed() {
		StringBuilder json = new StringBuilder();

		json.append("[{").append("\"id\": \"503993062\"").append(",")
				.append("\"statuses\": {").append("\"data\": [").append("{")
				.append("\"id\": \"10151115918458063\",").append("\"from\": {")
				.append("\"name\": \"Joel Ackerstierna\",")
				.append("\"id\": \"503993062\"").append("},")
				.append("\"message\": \"I am alive!\",")
				.append("\"update_time\": \"2012-10-14T15:05:15+0000\"")
				.append("}}]");

		System.out.println(json.toString());

		String jsonString = json.toString();

		List<FacebookItem> feed = parser.parseFeed(jsonString);

		assertNotNull(feed);
	}
}
