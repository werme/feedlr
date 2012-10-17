package com.chalmers.feedlr.parser;

import java.util.List;

import com.chalmers.feedlr.model.TwitterItem;
import com.chalmers.feedlr.model.User;

import android.test.AndroidTestCase;

public class TwitterJSONParserTest extends AndroidTestCase {

	private TwitterJSONParser parser;

	protected void setUp() throws Exception {
		super.setUp();
		parser = new TwitterJSONParser();
	}

	public void testParseTweets() {
		StringBuilder json = new StringBuilder();

		json.append("[{")
				.append("\"created_at\": \"Wed Aug 29 17:12:58 +0000 2012\"")
				.append(",")
				.append("\"id_str\": \"240859602684612608\"")
				.append(",")
				.append("\"text\":\"Awesome test message!\"")
				.append(",")
				.append("\"id\": 240859602684612608")
				.append(",")
				.append("\"user\":")
				.append("{")
				.append("\"name\": \"Awesome test user\"")
				.append(",")
				.append("\"profile_image_url\":\"http://www.google.com/img.png\"")
				.append(",").append("\"id_str\": \"6253282\"").append(",")
				.append("\"id\": 6253282").append("}").append("}]");

		String jsonString = json.toString();

		List<TwitterItem> tweets = parser.parseTweets(jsonString);

		assertNotNull(tweets);
	}

	public void testParseUserNames() {
		StringBuilder json = new StringBuilder();

		json.append("[{")
				.append("\"name\": \"Awesome test user\"")
				.append(",")
				.append("\"profile_image_url\":\"http://www.google.com/img.png\"")
				.append(",").append("\"id\": 24085960").append("}]");

		String jsonString = json.toString();

		List<User> users = parser.parseUserNames(jsonString);

		assertNotNull(users);
	}

	public void testParseUserISs() {
		StringBuilder json = new StringBuilder();

		json.append("{").append("\"ids\":").append("[")
				.append("657693,183709371,7588892,38895958,22891211")
				.append("]}");

		String jsonString = json.toString();

		String[] users = parser.parseUserIDs(jsonString);

		assertNotNull(users);
	}
	
	public void parseCredentials() {
		StringBuilder json = new StringBuilder();

		json.append("{").append("\"id\": 657693").append("}");

		String jsonString = json.toString();

		long userID = 0;
		userID = parser.parseCredentials(jsonString);

		assert(userID != 0);
	}
}
