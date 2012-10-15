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
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FeedAdapter extends SimpleCursorAdapter {

	Context context;

	public FeedAdapter(Context context, int layout, Cursor c, String[] from,
			int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = super.getView(position, convertView, parent);
		// if (convertView == null)
		// convertView = View.inflate(context, R.layout.feed_item, null);

		TextView text = (TextView) v.findViewById(R.id.feed_item_text);
		TextView author = (TextView) v.findViewById(R.id.feed_item_author);

		Typeface robotoThinItalic = Typeface.createFromAsset(
				context.getAssets(), "fonts/Roboto-ThinItalic.ttf");

		text.setTypeface(robotoThinItalic);

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
		super.bindView(v, context, c);
		int colNum = c.getColumnIndex(DatabaseHelper.ITEM_COLUMN_TIMESTAMP);
		Date timestampDate = new Date(c.getLong(colNum));

		String parsedTimestamp = DateUtils.getRelativeDateTimeString(context,
				timestampDate.getTime(), DateUtils.SECOND_IN_MILLIS,
				DateUtils.WEEK_IN_MILLIS, 0).toString();

		TextView textview = (TextView) v.findViewById(R.id.feed_item_timestamp);
		if (parsedTimestamp.contains(",")) {
			textview.setText(parsedTimestamp.substring(0,
					parsedTimestamp.indexOf(',')));
		} else {
			textview.setText(parsedTimestamp);
		}

		ImageView profilePicture;
		profilePicture = (ImageView) v
				.findViewById(R.id.feed_item_author_image);
		colNum = c.getColumnIndex(DatabaseHelper.ITEM_COLUMN_IMGURL);
		System.out.println("URL::::::::::: " + c.getString(colNum));
		// new DownloadImageTask(profilePicture).execute(c.getString(colNum));
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

		private ImageView profilePicture;

		public DownloadImageTask(ImageView profilePicture) {
			this.profilePicture = profilePicture;
		}

		protected Bitmap doInBackground(String... strings) {
			try {
				System.out.println("URL::::::::::: " + strings[0]);
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
			profilePicture.setImageBitmap(result);
		}
	}
}