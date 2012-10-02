/**
 * Class description
 * 
 * @author Daniel Larsson
 */

package com.chalmers.feedlr.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chalmers.feedlr.model.FacebookItem;
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

public class FacebookJSONParser {

	private static ObjectMapper mapper;
	private static ObjectReader reader;

	private static JsonFactory jFactory;
	private static JsonParser jParser;

	static JSONObject jsonObject;
	static JSONArray jArray;

	public static void parse(String json) {
		System.out.println(json);
		streamingParse(json);
	}

	/*
	 * 
	 * long time = System.currentTimeMillis();
	 * 
	 * if (mapper == null) { mapper = new ObjectMapper();
	 * mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
	 * false); reader = mapper.reader(new TypeReference<List<FacebookItem>>() {
	 * }); }
	 * 
	 * List<FacebookItem> list = null;
	 * 
	 * try { list = reader.readValue(json); } catch (JsonParseException e) {
	 * e.printStackTrace(); } catch (JsonMappingException e) {
	 * e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); }
	 * 
	 * Log.i(FacebookJSONParser.class.getName(), "Data binding parse");
	 * Log.i(FacebookJSONParser.class.getName(), "Items: " + list.size());
	 * Log.i(FacebookJSONParser.class.getName(), "Time in millis: " +
	 * (System.currentTimeMillis() - time)); }
	 */

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
				}
				if ("from".equals(jParser.getCurrentName())) {
					while (!"name".equals(jParser.getCurrentName())) {
						jParser.nextToken();
					}
					jParser.nextToken();

					o.getUser().setUserName(jParser.getText());
				}
				if ("message".equals(jParser.getCurrentName())) {
					jParser.nextToken();
					o.setText(jParser.getText());
				}
				if ("created_time".equals(jParser.getCurrentName())) {
					jParser.nextToken();
					o.setTimestamp(jParser.getText());
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

}
