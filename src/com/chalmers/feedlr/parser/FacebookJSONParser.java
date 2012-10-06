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
	private static ObjectReader reader;

	private static JsonFactory jFactory;
	private static JsonParser jParser;

	static JSONObject jsonObject;
	static JSONArray jArray;

	public static List<User> parseItems(String json) {
		return dataBindingParse(json);
		// streamingParse(json);
	}

	public static void parse(String json) {
		// System.out.println(json);
		System.out.println("Begins with: " + json.substring(0, 100));
		System.out.println("Ends with " + json.substring(json.length() - 100));
		// streamingParse(json);
		dataBindingParse(json);
		// androidJsonParse(json);
		// anotherStreamingParse(json);
	}

	private static List<User> dataBindingParse(String json) {
		long time = System.currentTimeMillis();

		String data = json.substring(8);

		if (mapper == null) {
			mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			reader = mapper.reader(new TypeReference<List<FacebookItem>>() {
			});
		}

		List<User> list = null;

		try {
			list = reader.readValue(data);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Log.i(FacebookJSONParser.class.getName(), "Data binding parse");
		Log.i(FacebookJSONParser.class.getName(), "Items: " + list.size());
		Log.i(FacebookJSONParser.class.getName(),
				"Time in millis: " + (System.currentTimeMillis() - time));

		return list;
	}

	private static void streamingParse(String json) {
		long time = System.currentTimeMillis();

		List<FacebookItem> list = new ArrayList<FacebookItem>();

		try {
			if (jFactory == null)
				jFactory = new JsonFactory();

			jParser = jFactory.createJsonParser(json);

			FacebookItem o = null;
			while (jParser.nextToken() != null) {
				if ("type".equals(jParser.getCurrentName())) {
					jParser.nextToken();
					o = new FacebookItem();
					o.setType(jParser.getText());
					System.out.println("Found type:   " + jParser.getText());
				}
				if ("from".equals(jParser.getCurrentName())) {
					System.out.println("Found from");
					while (!"name".equals(jParser.getCurrentName())) {
						jParser.nextToken();
					}
					jParser.nextToken();

					o.getUser().setUserName(jParser.getText());
					System.out.println("Found name:   " + jParser.getText());
				}
				if ("message".equals(jParser.getCurrentName())) {
					jParser.nextToken();
					o.setText(jParser.getText());
					System.out.println("Found message:   " + jParser.getText());
				}
				if ("created_time".equals(jParser.getCurrentName())) {
					jParser.nextToken();
					o.setTimestamp(jParser.getText());
					System.out.println("Found created_time:   "
							+ jParser.getText());
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

		Log.i(FacebookJSONParser.class.getName(), "Streaming parse");
		Log.i(FacebookJSONParser.class.getName(), "Items: " + list.size());
		Log.i(FacebookJSONParser.class.getName(),
				"Time in millis: " + (System.currentTimeMillis() - time));
	}

	private static void androidJsonParse(String json) {

		try {

			JSONArray data;
			data = new JSONArray(json);

			int itemCount = data.length();
			List<FacebookItem> list = new ArrayList<FacebookItem>(itemCount);

			for (int i = 0; i < itemCount; i++) {
				JSONObject item = data.getJSONObject(i);

				String type = item.getString("type");
				String message = item.optString("message");

				FacebookItem o = new FacebookItem();
				o.setText(message);
				o.setType(type);
				list.add(o);
			}
			System.out.println("Place 0" + list.get(0));
			System.out.println("Place 1" + list.get(1));
			System.out.println("Place 2" + list.get(2));
			System.out.println("Place 3" + list.get(3));
			System.out.println("Place 4" + list.get(4));

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private static void anotherStreamingParse(String json) {

		ObjectMapper mapper = new ObjectMapper();

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		// ItemContainer itemContainer = mapper.readValue(json,
		// ItemContainer.class);

		try {
			List<FacebookItem> list = mapper.readValue(json, List.class);

			System.out.println("Place 0" + list.get(0));
			System.out.println("Place 1" + list.get(1));
			System.out.println("Place 2" + list.get(2));
			System.out.println("Place 3" + list.get(3));
			System.out.println("Place 4" + list.get(4));
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * Parses string and prints it in the log.
	 */
	public static void parseAndPrint(String response) {

		try {
			jsonObject = new JSONObject(response);
			jArray = jsonObject.getJSONArray("data");

			/*
			 * Only parses traditional status updates for now, different types
			 * will be parsed later on.
			 */
			for (int i = 0; i < jArray.length(); i++) {

				String name = jArray.getJSONObject(i).getJSONObject("from")
						.getString("name");
				String message = null;
				message = jArray.getJSONObject(i).getString("message");

				System.out.println("Name: " + name + "        Message: "
						+ message);
			}
		} catch (JSONException e) {
			Log.w("JSONParser", "JSON Error in response " + e.getMessage());
		}
	}

	public static List<User> parseUsers(String json) {
		long time = System.currentTimeMillis();

		String data = json.substring(8);

		if (mapper == null) {
			mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			reader = mapper.reader(new TypeReference<List<User>>() {
			});
		}

		List<User> list = null;

		try {
			list = reader.readValue(data);
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
