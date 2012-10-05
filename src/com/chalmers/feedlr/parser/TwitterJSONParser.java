/**
 * Class description
 * 
 * @author Olle Werme
 */

package com.chalmers.feedlr.parser;

import java.io.IOException;
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

public class TwitterJSONParser {

	private static ObjectMapper mapper;
	private static ObjectReader reader;

//	private static JsonFactory jfactory;
//	private static JsonParser jParser;

	public static List<TwitterItem> parseTweets(String json) {
		return dataBindingParse(json);
		//streamingParse(json);
	}

	private static List<TwitterItem> dataBindingParse(String json) {
		long time = System.currentTimeMillis();

		if (mapper == null) {
			mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			reader = mapper.reader(new TypeReference<List<TwitterItem>>() {
			});
		}

		List<TwitterItem> list = null;

		try {
			list = reader.readValue(json);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Log.i(TwitterJSONParser.class.getName(), "Data binding parse");
		Log.i(TwitterJSONParser.class.getName(), "Items: " + list.size());
		Log.i(TwitterJSONParser.class.getName(),
				"Time in millis: " + (System.currentTimeMillis() - time));
		
		return list;
	}

//	private static void streamingParse(String json) {
//		long time = System.currentTimeMillis();
//
//		List<TwitterItem> list = new ArrayList<TwitterItem>();
//
//		try {
//
//			if (jfactory == null)
//				jfactory = new JsonFactory();
//
//			jParser = jfactory.createJsonParser(json);
//
//			TwitterItem o = null;
//			while (jParser.nextToken() != null) {
//				if ("created_at".equals(jParser.getCurrentName())) {
//					jParser.nextToken();
//					o = new TwitterItem();
//					o.setTimestamp(jParser.getText());
//				}
//				if ("text".equals(jParser.getCurrentName())) {
//					jParser.nextToken();
//					o.setText(jParser.getText());
//				}
//				if ("name".equals(jParser.getCurrentName())) {
//					jParser.nextToken();
//					o.getUser().setUserName(jParser.getText());
//				}
//				if ("profile_image_url".equals(jParser.getCurrentName())) {
//					jParser.nextToken();
//					o.getUser().setProfileImageURL(jParser.getText());
//					list.add(o);
//				}
//			}
//			jParser.close();
//
//		} catch (JsonGenerationException e) {
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		Log.i(TwitterJSONParser.class.getName(), "Streaming parse");
//		Log.i(TwitterJSONParser.class.getName(), "Items: " + list.size());
//		Log.i(TwitterJSONParser.class.getName(),
//				"Time in millis: " + (System.currentTimeMillis() - time));
//	}

	public static String[] parseUserIDs(String json) {
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

	public static List<User> parseUserNames(String json) {
		long time = System.currentTimeMillis();

		if (mapper == null) {
			mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			reader = mapper.reader(new TypeReference<List<User>>() {
			});
		}

		List<User> list = null;

		try {
			list = reader.readValue(json);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Log.i(TwitterJSONParser.class.getName(), "Parsed " + list.size()
				+ " users.");
		Log.i(TwitterJSONParser.class.getName(), "Name: "
				+ list.get(30).getUserName() + ". ID: " + list.get(30).getId()
				+ ". URL: " + list.get(30).getProfileImageURL());
		;
		Log.i(TwitterJSONParser.class.getName(),
				"Time in millis: " + (System.currentTimeMillis() - time));

		return list;
	}
}