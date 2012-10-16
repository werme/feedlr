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

package com.chalmers.feedlr.adapter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.chalmers.feedlr.R;
import com.chalmers.feedlr.database.DatabaseHelper;

import android.app.Activity;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FeedAdapter extends SimpleCursorAdapter {

	Context context;
	DatabaseHelper db;
	private int numberOfItems; // Used for tagging ImageViews

	public FeedAdapter(Context context, int layout, Cursor c, String[] from,
			int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		this.context = context;
		this.db = new DatabaseHelper(context);
	}

	static class ViewHolder {
		public TextView text;
		public TextView author;
		public TextView timeStamp;
		public ImageView profilePicture;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Avarage time:
		// 135 ms for first view.
		// 40 ms for new views.
		// 13 ms for recycled views.
		View v = super.getView(position, convertView, parent);

		return v;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.widget.SimpleCursorAdapter#bindView(android.view.View,
	 * android.content.Context, android.database.Cursor)
	 */
	@Override
	public void bindView(View v, Context context, Cursor c) {
		// Avarage time: 100 ms for first view, 10ms for the rest.
		super.bindView(v, context, c);

		ImageView profilePicture;
		ViewHolder viewHolder = (ViewHolder) v.getTag();

		int colNum = c.getColumnIndex(DatabaseHelper.ITEM_COLUMN_TIMESTAMP);
		Date timestampDate = new Date(c.getLong(colNum));

		String parsedTimestamp = DateUtils.getRelativeDateTimeString(context,
				timestampDate.getTime(), DateUtils.SECOND_IN_MILLIS,
				DateUtils.WEEK_IN_MILLIS, 0).toString();

		if (parsedTimestamp.contains(",")) {
			viewHolder.timeStamp.setText(parsedTimestamp.substring(0,
					parsedTimestamp.indexOf(',')));
		} else {
			viewHolder.timeStamp.setText(parsedTimestamp);
		}

		profilePicture = (ImageView) v
				.findViewById(R.id.feed_item_author_image);
		colNum = c.getColumnIndex(DatabaseHelper.ITEM_COLUMN_USER_ID);
		Cursor cursor = db.getUser(c.getInt(colNum) + "");
		cursor.moveToFirst();

		int colNum2 = cursor.getColumnIndex(DatabaseHelper.USER_COLUMN_IMGURL);
		String imgURL = cursor.getString(colNum2);

		profilePicture.setTag(numberOfItems++);
		new DownloadImageTask(profilePicture).execute(imgURL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.widget.ResourceCursorAdapter#newView(android.content
	 * .Context, android.database.Cursor, android.view.ViewGroup)
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// Avarage time: 30 ms
		super.newView(context, cursor, parent);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View tempView = inflater.inflate(R.layout.feed_item, null);
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.text = (TextView) tempView.findViewById(R.id.feed_item_text);
		viewHolder.author = (TextView) tempView
				.findViewById(R.id.feed_item_author);

		viewHolder.timeStamp = (TextView) tempView
				.findViewById(R.id.feed_item_timestamp);
		Typeface robotoThinItalic = Typeface.createFromAsset(
				context.getAssets(), "fonts/Roboto-ThinItalic.ttf");
		viewHolder.text.setTypeface(robotoThinItalic);
		tempView.setTag(viewHolder);
		return tempView;
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

		// Avarage time: 270 ms

		private ImageView profilePicture;
		private String tag;

		public DownloadImageTask(ImageView profilePicture) {
			this.profilePicture = profilePicture;
			this.tag = profilePicture.getTag().toString();

		}

		protected Bitmap doInBackground(String... strings) {
			try {
				URL imgValue = new URL(strings[0]);
				Bitmap thumbNail = BitmapFactory.decodeStream(imgValue
						.openConnection().getInputStream());
				return thumbNail;
			} catch (MalformedURLException e) {

				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Bitmap result) {
			if (profilePicture.getTag().toString().equals(tag)) {
				profilePicture.setImageBitmap(result);
			}
		}
	}
}