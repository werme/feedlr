/**
 * Class description
 * 
 * @author Olle Werme
 */

package com.chalmers.feedlr.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.chalmers.feedlr.model.TwitterItem;
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
	
	private static JsonFactory jfactory;
	private static JsonParser jParser;
		
	public static void parse(String json) {
		dataBindingParse(json);
		streamingParse(json);		
	}
	
	private static void dataBindingParse(String json) {
		long time = System.currentTimeMillis();
		
		if(mapper == null) {
			mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			reader = mapper.reader(new TypeReference<List<TwitterItem>>() {});
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
		Log.i(TwitterJSONParser.class.getName(), "Time in millis: " + (System.currentTimeMillis() - time));
	}
	
	private static void streamingParse(String json) {
		long time = System.currentTimeMillis();
		
		List<TwitterItem> list = new ArrayList<TwitterItem>();
		
		try {

			if (jfactory == null)
				jfactory = new JsonFactory();

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
		Log.i(TwitterJSONParser.class.getName(), "Time in millis: " + (System.currentTimeMillis() - time));
	}
}