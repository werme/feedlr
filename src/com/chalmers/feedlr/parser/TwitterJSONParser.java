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

package com.chalmers.feedlr.parser;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chalmers.feedlr.model.TwitterItem;
import com.chalmers.feedlr.model.User;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import android.util.Log;

/**
 * Class description
 * 
 * @author Olle Werme
 */

public class TwitterJSONParser {

	private static ObjectMapper mapper;
	private static ObjectReader tweetReader;
	private static ObjectReader usernameReader;

	private static JsonFactory jfactory;
	private static JsonParser jParser;

	public TwitterJSONParser() {
		if (mapper == null) {
			mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
		}
	}

	public List<TwitterItem> parseTweets(String json) {
		long time = System.currentTimeMillis();

		if (tweetReader == null) {
			tweetReader = mapper.reader(new TypeReference<List<TwitterItem>>() {
			});
		}

		List<TwitterItem> list = null;

		try {
			list = tweetReader.readValue(json);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Log.i(TwitterJSONParser.class.getName(), "Data binding parse");
		// Log.i(TwitterJSONParser.class.getName(), "Items: " + list.size());
		// Log.i(TwitterJSONParser.class.getName(),
		// "Time in millis: " + (System.currentTimeMillis() - time));

		return list;
	}

	/**
	 * Parse a JSON response for a list of "tweets" from Twitters REST API with
	 * Jacksons streaming parse method. Although this is a sligthly faster
	 * alternative the current build uses the databinding parse, favoring a
	 * simplier implementation. This method is thus currently not in use.
	 * 
	 * @param json
	 *            the JSON response from Twitters RESP API
	 */
	private List<TwitterItem> streamingTwitterParse(String json) {
		long time = System.currentTimeMillis();

		List<TwitterItem> list = new ArrayList<TwitterItem>();

		try {

			if (jfactory == null) {
				jfactory = new JsonFactory();
			}

			jParser = jfactory.createJsonParser(json);

			TwitterItem o = null;
			while (jParser.nextToken() != null) {
				if ("created_at".equals(jParser.getCurrentName())) {
					jParser.nextToken();
					o = new TwitterItem();
					o.setTimestamp(jParser.getText());
				}
				if ("text".equals(jParser.getCurrentName())) {
					jParser.nextToken();
					o.setText(jParser.getText());
				}
				if ("name".equals(jParser.getCurrentName())) {
					jParser.nextToken();
					o.getUser().setUserName(jParser.getText());
				}
				if ("profile_image_url".equals(jParser.getCurrentName())) {
					jParser.nextToken();
					o.getUser().setProfileImageURL(jParser.getText());
					list.add(o);
				}
			}
			jParser.close();

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Log.i(TwitterJSONParser.class.getName(), "Streaming parse");
		Log.i(TwitterJSONParser.class.getName(), "Items: " + list.size());
		Log.i(TwitterJSONParser.class.getName(),
				"Time in millis: " + (System.currentTimeMillis() - time));

		return list;
	}

	public String[] parseUserIDs(String json) {
		long time = System.currentTimeMillis();

		JSONObject wrapperObject;
		String[] ids = null;
		try {
			wrapperObject = new JSONObject(json);
			JSONArray jsonIDs = wrapperObject.getJSONArray("ids");
			ids = new String[jsonIDs.length()];
			for (int i = 0; i < jsonIDs.length(); i++) {
				ids[i] = jsonIDs.getString(i);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.i(TwitterJSONParser.class.getName(), "Parsed " + ids.length
				+ " user ids.");
		Log.i(TwitterJSONParser.class.getName(),
				"Time in millis: " + (System.currentTimeMillis() - time));

		return ids;
	}

	public List<User> parseUserNames(String json) {
		long time = System.currentTimeMillis();

		if (usernameReader == null) {
			usernameReader = mapper.reader(new TypeReference<List<User>>() {
			});
		}

		List<User> list = null;

		try {
			list = usernameReader.readValue(json);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Log.i(TwitterJSONParser.class.getName(), "Parsed " + list.size()
				+ " users.");
		Log.i(TwitterJSONParser.class.getName(),
				"Time in millis: " + (System.currentTimeMillis() - time));

		return list;
	}

	public long parseCredentials(String json) {
		long time = System.currentTimeMillis();

		JSONObject wrapperObject;
		long userID = 0;
		try {
			wrapperObject = new JSONObject(json);
			userID = wrapperObject.getLong("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.i(TwitterJSONParser.class.getName(), "Parsed user credentials");
		Log.i(TwitterJSONParser.class.getName(), "User ID: " + userID);
		Log.i(TwitterJSONParser.class.getName(),
				"Time in millis: " + (System.currentTimeMillis() - time));

		return userID;
	}
}