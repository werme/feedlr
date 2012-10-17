package com.chalmers.feedlr.parser;

import java.util.List;

import com.chalmers.feedlr.model.TwitterItem;
import com.chalmers.feedlr.model.User;

import android.test.AndroidTestCase;

public class TwitterJSONParserTest extends AndroidTestCase {

	private TwitterJSONParser parser;

	private static final String START_OBJECT = "{";
	private static final String END_OBJECT = "}";
	private static final String START_ARRAY = "[";
	private static final String END_ARRAY = "]";
	private static final String SEPARATOR = ",";

	protected void setUp() throws Exception {
		super.setUp();
		parser = new TwitterJSONParser();
	}

	public void testParseTweets() {
		StringBuilder json = new StringBuilder();

		json.append(START_ARRAY)
				.append(START_OBJECT)
				.append("\"created_at\": \"Wed Aug 29 17:12:58 +0000 2012\"")
				.append(SEPARATOR)
				.append("\"id_str\": \"240859602684612608\"")
				.append(SEPARATOR)
				.append("\"text\":\"Awesome test message!\"")
				.append(SEPARATOR)
				.append("\"id\": 240859602684612608")
				.append(SEPARATOR)
				.append("\"user\":")
				.append(START_OBJECT)
				.append("\"name\": \"Awesome test user\"")
				.append(SEPARATOR)
				.append("\"profile_image_url\":\"http://www.google.com/img.png\"")
				.append(SEPARATOR).append("\"id_str\": \"6253282\"")
				.append(SEPARATOR).append("\"id\": 6253282").append(END_OBJECT)
				.append(END_OBJECT).append(END_ARRAY);

		String jsonString = json.toString();

		List<TwitterItem> tweets = parser.parseTweets(jsonString);

		assertNotNull(tweets);
	}

	public void testParseUserNames() {
		StringBuilder json = new StringBuilder();

		json.append(START_ARRAY)
				.append(START_OBJECT)
				.append("\"name\": \"Awesome test user\"")
				.append(SEPARATOR)
				.append("\"profile_image_url\":\"http://www.google.com/img.png\"")
				.append(SEPARATOR).append("\"id\": 24085960")
				.append(END_OBJECT).append(END_ARRAY);

		String jsonString = json.toString();

		List<User> users = parser.parseUserNames(jsonString);

		assertNotNull(users);
	}

	public void testParseUserISs() {
		StringBuilder json = new StringBuilder();

		json.append(START_OBJECT).append("\"ids\":").append(START_ARRAY)
				.append("657693,183709371,7588892,38895958,22891211")
				.append(END_ARRAY).append(END_OBJECT);

		String jsonString = json.toString();

		String[] users = parser.parseUserIDs(jsonString);

		assertNotNull(users);
	}

	public void parseCredentials() {
		StringBuilder json = new StringBuilder();

		json.append(START_OBJECT).append("\"id\": 657693").append(END_OBJECT);

		String jsonString = json.toString();

		long userID = 0;
		userID = parser.parseCredentials(jsonString);

		assert (userID != 0);
	}
}
