/**
 * Class description
 * 
 * @author Olle Werme
 */

package com.chalmers.feedlr.twitter;

import org.json.JSONArray;

import android.util.Log;

public class TwitterJSONParser {
	public static void parse(String JSONresponse) {
		try {
			JSONArray jsonArray = new JSONArray(JSONresponse);
			
			for (int i = 0; i < jsonArray.length(); i++) {
				Log.i(TwitterRequest.class.getName(), "User: " + jsonArray.getJSONObject(i).getJSONObject("user").getString("name"));
				Log.i(TwitterRequest.class.getName(), "Tweet: " + jsonArray.getJSONObject(i).getString("text"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
