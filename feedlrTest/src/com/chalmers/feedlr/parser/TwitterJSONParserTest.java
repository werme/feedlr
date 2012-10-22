package com.chalmers.feedlr.parser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
		String timestampIn = "Wed Aug 29 17:12:58 +0000 2012";
		
		// Parse timestamp
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);
		dateFormat.setLenient(false);
		
		Date date = null;
		try {
			date = dateFormat.parse(timestampIn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		long timestampOut = date.getTime();
		String text = "Awesome text message!";
		String tweetId = "2408596";
		String username = "Awesome username";
		String url = "http://www.google.com/img.png";
		String userId = "6253282";

		StringBuilder json = new StringBuilder();
		json.append(START_ARRAY).append(START_OBJECT)
				.append("\"created_at\":\"" + timestampIn + "\"")
				.append(SEPARATOR)
				.append("\"text\":\"" + text + "\"")
				.append(SEPARATOR)
				.append("\"id\":" + tweetId)
				.append(SEPARATOR)
				.append("\"user\":")
				.append(START_OBJECT)
				.append("\"name\":\"" + username + "\"")
				.append(SEPARATOR)
				.append("\"profile_image_url\":\"" + url + "\"")
				.append(SEPARATOR)
				.append("\"id\":" + userId)
				.append(END_OBJECT)
				.append(END_OBJECT).append(END_ARRAY);

		String jsonString = json.toString();

		List<TwitterItem> tweets = parser.parseTweets(jsonString);

		assertNotNull(tweets);

		TwitterItem item = tweets.get(0);
		assertNotNull(item);

		assertEquals("twitter", item.getUser().getSource());
		assertTrue(timestampOut == item.getTimestamp());
		assertEquals(text, item.getText());
		assertEquals(tweetId, item.getId());
		assertEquals(username, item.getUser().getUserName());
		assertEquals(url, item.getUser().getProfileImageURL());
		assertEquals(userId, item.getUser().getId());
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
