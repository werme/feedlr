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
import java.util.Date;

import com.chalmers.feedlr.R;
import com.chalmers.feedlr.database.DatabaseHelper;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FeedAdapter extends SimpleCursorAdapter {

	Context context;
	DatabaseHelper db;
	private int numberOfViews; // Used for tagging ImageViews

	public FeedAdapter(Context context, int layout, Cursor c, String[] from,
			int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		this.context = context;
		this.db = new DatabaseHelper(context);
	}

	static class ViewHolder {
		private TextView text;
		private TextView author;
		private TextView timestamp;
		private ImageView profilePicture;
		private ImageView source;
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

		// Holds the views, so that a recycled view does not have to find its
		// XML view
		ViewHolder viewHolder = (ViewHolder) v.getTag();

		// Remove the recycled profile picture
		viewHolder.profilePicture.setVisibility(View.INVISIBLE);

		// Get user id
		int colNumUserId = c.getColumnIndex(DatabaseHelper.ITEM_COLUMN_USER_ID);
		Cursor cursor = db.getUser(c.getInt(colNumUserId) + "");
		cursor.moveToFirst();

		// Set source image
		int colNumSource = cursor
				.getColumnIndex(DatabaseHelper.USER_COLUMN_SOURCE);
		if (cursor.getString(colNumSource).equals("facebook")) {
			viewHolder.source.setBackgroundResource(R.drawable.source_facebook);
		} else {
			viewHolder.source.setBackgroundResource(R.drawable.source_twitter);
		}

		// Display profile picture
		int colNumURL = cursor
				.getColumnIndex(DatabaseHelper.USER_COLUMN_IMGURL);
		String imgURL = cursor.getString(colNumURL);
		viewHolder.profilePicture.setTag(numberOfViews++);
		new DownloadImageTask(viewHolder.profilePicture).execute(imgURL);

		// Display username
		int colNumUsername = cursor
				.getColumnIndex(DatabaseHelper.USER_COLUMN_USERNAME);
		if (cursor.getString(colNumUsername).length() > 18) {
			viewHolder.author.setTextSize(16);
		}

		viewHolder.author.setText(cursor.getString(colNumUsername));

		// Display timestamp
		int colNumTimestamp = c
				.getColumnIndex(DatabaseHelper.ITEM_COLUMN_TIMESTAMP);
		Date timestampDate = new Date(c.getLong(colNumTimestamp));
		String parsedTimestamp = DateUtils.getRelativeDateTimeString(context,
				timestampDate.getTime(), DateUtils.SECOND_IN_MILLIS,
				DateUtils.WEEK_IN_MILLIS, 0).toString();
		viewHolder.timestamp.setText(stripTimestamp(parsedTimestamp));
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

		// Find views
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.text = (TextView) tempView.findViewById(R.id.feed_item_text);
		viewHolder.author = (TextView) tempView
				.findViewById(R.id.feed_item_author);
		viewHolder.timestamp = (TextView) tempView
				.findViewById(R.id.feed_item_timestamp);
		viewHolder.profilePicture = (ImageView) tempView
				.findViewById(R.id.feed_item_author_image);
		viewHolder.source = (ImageView) tempView
				.findViewById(R.id.feed_item_source_image);

		// Set fonts
		Typeface robotoThinItalic = Typeface.createFromAsset(
				context.getAssets(), "fonts/Roboto-ThinItalic.ttf");
		Typeface robotoMedium = Typeface.createFromAsset(context.getAssets(),
				"fonts/Roboto-Medium.ttf");
		Typeface robotoLight = Typeface.createFromAsset(context.getAssets(),
				"fonts/Roboto-Light.ttf");
		viewHolder.text.setTypeface(robotoLight);
		viewHolder.timestamp.setTypeface(robotoThinItalic);
		viewHolder.author.setTypeface(robotoMedium);

		tempView.setTag(viewHolder);
		return tempView;
	}

	/*
	 * Strips timestamp string from unwanted information.
	 */
	public String stripTimestamp(String timestamp) {
		if (timestamp.contains(",")) {
			return (timestamp.substring(0, timestamp.indexOf(',')));
		} else {
			return timestamp;
		}
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap squareBitmap) {
		Bitmap roundedBitmap = Bitmap.createBitmap(squareBitmap.getWidth(),
				squareBitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(roundedBitmap);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, squareBitmap.getWidth(),
				squareBitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = 8;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(squareBitmap, rect, rect, paint);

		return roundedBitmap;
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
				Bitmap bitmapScaled = Bitmap.createScaledBitmap(thumbNail, 70,
						70, true);
				return bitmapScaled;
			} catch (MalformedURLException e) {
				Log.e(getClass().getName(), e.getMessage());
			} catch (IOException e) {
				Log.e(getClass().getName(), e.getMessage());
			}
			return null;
		}

		protected void onPostExecute(Bitmap result) {
			if (profilePicture.getTag().toString().equals(tag)) {
				profilePicture.setImageBitmap(getRoundedCornerBitmap(result));
				profilePicture.setVisibility(View.VISIBLE);
			}
		}
	}
}