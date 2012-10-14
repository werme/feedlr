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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.chalmers.feedlr.model.FacebookItem;
import com.chalmers.feedlr.model.User;
import com.fasterxml.jackson.core.JsonParseException;

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

	public FacebookJSONParser() {
		if (mapper == null) {
			mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
		}
	}

	public List<FacebookItem> parseFeed(String json) {
		long time = System.currentTimeMillis();

		System.out.println(json);
		String data = json.substring(json.indexOf("statuses") + 18);

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
				+ " Facebook items in " + (System.currentTimeMillis() - time)
				+ " millis.");
		return list;
	}

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
				+ " Facebook users in " + (System.currentTimeMillis() - time)
				+ " millis.");

		return list;
	}
}
