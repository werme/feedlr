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

import java.util.List;

import com.chalmers.feedlr.model.Item;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Class description
 * 
 * @user David Gšransson
 */



public class FeedDatabaseHelper extends SQLiteOpenHelper {

	// Database static variables
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "itemDatabase";
	private static final String TABLE_ITEM = "item";

	// Database columns
	private static final String COLUMN_ID = "_id";
	private static final String COLUMN_USER = "user";
	private static final String COLUMN_SOURCE = "source";

	public FeedDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL("CREATE TABLE " + TABLE_ITEM + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER
				+ " TEXT," + COLUMN_SOURCE + " TEXT" + ")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		// No need to save items if upgraded since its only feed items.
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
		onCreate(database);
	}
	
	//Add a single item to the database.
	
	public void addListOfItems(List<Item> itemList){
		for (Item o: itemList) {
			addItem(o.getUser().getUserName(), o.getText(), "Timestamp", "Twitter");
		}
	}
	
	public void addItem(String user, String text, String timestamp, String source){
		SQLiteDatabase database = this.getWritableDatabase();
		
		ContentValues temp = new ContentValues();
		temp.put(COLUMN_USER, user);
		temp.put(COLUMN_SOURCE, source);
		
		database.insert(TABLE_ITEM, null, temp);
		database.close();
	}
	public String getRow(int id) {
	    SQLiteDatabase database = this.getReadableDatabase();
	 
	    Cursor cursor = database.query(TABLE_ITEM, new String[] { COLUMN_USER }, COLUMN_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    if (cursor != null)
	        cursor.moveToFirst();
	 

	    // return contact /test
	    String i = cursor.getString(0) + cursor.getString(1) + cursor.getString(2);
	    cursor.close();
	    database.close();
	    return i;
	}

	public void deleteTable(){
		SQLiteDatabase database = this.getWritableDatabase();
		database.delete(TABLE_ITEM, null, null);
		database.close();
	}
	/*
	
	public getItem(int id){
		SQLiteDatabase database = this.getReadableDatabase();
		 
        Cursor cursor = database.query(TABLE_ITEM, new String[] { KEY_ID,
                KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return contact;
	}
	*/
	public void getAllItems(){
		//How should we return all items?
		return ;
	}
	
	public long getSize(){
		SQLiteDatabase database = this.getReadableDatabase();
		long l = DatabaseUtils.queryNumEntries(database ,TABLE_ITEM);
		database.close();
		return l;
		
	}
}
