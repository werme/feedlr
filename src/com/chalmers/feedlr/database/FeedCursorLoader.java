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

package com.chalmers.feedlr.database;

import com.chalmers.feedlr.external.SimpleCursorLoader;
import com.chalmers.feedlr.model.Feed;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class FeedCursorLoader extends SimpleCursorLoader {
	
	private String feedTitle;
	private DatabaseHelper db;

	public FeedCursorLoader(Context context, String feedTitle) {
		super(context);
		this.feedTitle = feedTitle;
		
		db = new DatabaseHelper(context);
	}

	@Override
	public Cursor loadInBackground() {
		Cursor cursor = db.getItems(new Feed(feedTitle), 60);
		Log.i("loader", "items: " + cursor.getCount());
		return cursor;
	}
}