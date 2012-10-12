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
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chalmers.feedlr.model.FacebookItem;
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
 * @author Daniel Larsson
 * 
 *         A parser for Facebook responses. The parsing is done with the Jackson
 *         JSON Processor.
 */

public class FacebookJSONParser {

	private static ObjectMapper mapper;
	private static ObjectReader itemReader;
	private static ObjectReader userReader;

	private static JsonFactory jFactory;
	private static JsonParser jParser;

	public FacebookJSONParser() {
		if (mapper == null) {
			mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
		}
	}

	public List<FacebookItem> parseFeed(String json) {
		long time = System.currentTimeMillis();

		String data = json.substring(8);

		if (itemReader == null)
			itemReader = mapper.reader(new TypeReference<List<FacebookItem>>() {
			});

		List<FacebookItem> list = null;

		try {
			list = itemReader.readValue(data);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Log.i(FacebookJSONParser.class.getName(), "Parsed " + list.size()
				+ " items.");
		Log.i(FacebookJSONParser.class.getName(), "Example:     From: "
				+ list.get(5).getUser().getUserName() + ". Message: "
				+ list.get(5).getText());
		Log.i(FacebookJSONParser.class.getName(),
				"Time in millis: " + (System.currentTimeMillis() - time));

		return list;
	}

	/*
	 * private static void streamingParse(String json) { long time =
	 * System.currentTimeMillis();
	 * 
	 * List<FacebookItem> list = new ArrayList<FacebookItem>();
	 * 
	 * try { if (jFactory == null) jFactory = new JsonFactory();
	 * 
	 * jParser = jFactory.createJsonParser(json);
	 * 
	 * FacebookItem o = null; while (jParser.nextToken() != null) { if
	 * ("type".equals(jParser.getCurrentName())) { jParser.nextToken(); o = new
	 * FacebookItem(); o.setType(jParser.getText());
	 * System.out.println("Found type:   " + jParser.getText()); } if
	 * ("from".equals(jParser.getCurrentName())) {
	 * System.out.println("Found from"); while
	 * (!"name".equals(jParser.getCurrentName())) { jParser.nextToken(); }
	 * jParser.nextToken();
	 * 
	 * o.getUser().setUserName(jParser.getText());
	 * System.out.println("Found name:   " + jParser.getText()); } if
	 * ("message".equals(jParser.getCurrentName())) { jParser.nextToken();
	 * o.setText(jParser.getText()); System.out.println("Found message:   " +
	 * jParser.getText()); } if
	 * ("created_time".equals(jParser.getCurrentName())) { jParser.nextToken();
	 * o.setTimestamp(jParser.getText());
	 * System.out.println("Found created_time:   " + jParser.getText());
	 * list.add(o); } } jParser.close();
	 * 
	 * } catch (JsonGenerationException e) { e.printStackTrace(); } catch
	 * (JsonMappingException e) { e.printStackTrace(); } catch (IOException e) {
	 * e.printStackTrace(); }
	 * 
	 * Log.i(FacebookJSONParser.class.getName(), "Streaming parse");
	 * Log.i(FacebookJSONParser.class.getName(), "Items: " + list.size());
	 * Log.i(FacebookJSONParser.class.getName(), "Time in millis: " +
	 * (System.currentTimeMillis() - time)); }
	 */

	public List<User> parseUsers(String json) {
		long time = System.currentTimeMillis();

		String data = json.substring(8);

		if (userReader == null)
			userReader = mapper.reader(new TypeReference<List<User>>() {
			});

		List<User> list = null;

		try {
			list = userReader.readValue(data);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Log.i(FacebookJSONParser.class.getName(), "Parsed " + list.size()
				+ " users.");
		Log.i(FacebookJSONParser.class.getName(), "Name: "
				+ list.get(30).getUserName() + ". ID: " + list.get(30).getId()
				+ ". URL: " + list.get(30).getProfileImageURL());
		Log.i(FacebookJSONParser.class.getName(),
				"Time in millis: " + (System.currentTimeMillis() - time));

		return list;
	}
}
