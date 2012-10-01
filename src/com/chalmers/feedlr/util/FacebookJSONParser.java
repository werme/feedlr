/**
 * Class description
 * 
 * @author Daniel Larsson
 */

package com.chalmers.feedlr.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class FacebookJSONParser {

	static JSONObject jsonObject;
	static JSONArray jArray;

	/*
	 * Parses string and prints prints it in the log.
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
