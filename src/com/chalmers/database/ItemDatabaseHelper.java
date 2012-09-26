package com.chalmers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ItemDatabaseHelper extends SQLiteOpenHelper {

	// Database static variables
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "itemDatabase";
	private static final String TABLE_ITEM = "item";

	// Database columns
	private static final String COLUMN_ID = "_id";
	private static final String COLUMN_AUTHOR = "author";
	private static final String COLUMN_BODY = "body";
	private static final String COLUMN_TIMESTAMP = "timestamp";
	private static final String COLUMN_SOURCE = "source";

	public ItemDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		Log.d("DO we get here?", "yes we do!");
		database.execSQL("CREATE TABLE " + TABLE_ITEM + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_AUTHOR
				+ " TEXT," + COLUMN_BODY + " TEXT," + COLUMN_TIMESTAMP
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
	public void addItem(String author, String body, String timestamp, String source){
		SQLiteDatabase database = this.getWritableDatabase();
		
		ContentValues temp = new ContentValues();
		temp.put(COLUMN_AUTHOR, author);
		temp.put(COLUMN_BODY, body);
		temp.put(COLUMN_TIMESTAMP, timestamp);
		temp.put(COLUMN_SOURCE, source);
		
		database.insert(TABLE_ITEM, null, temp);
		database.close();
	}
	public String getRow(int id) {
	    SQLiteDatabase db = this.getReadableDatabase();
	 
	    Cursor cursor = db.query(TABLE_ITEM, new String[] { COLUMN_AUTHOR,
	            COLUMN_BODY, COLUMN_TIMESTAMP }, COLUMN_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    if (cursor != null)
	        cursor.moveToFirst();
	 

	    // return contact
	    return cursor.getString(0) + cursor.getString(1) + cursor.getString(2);
	}

	public void deleteTable(){
		SQLiteDatabase database = this.getWritableDatabase();
		database.delete(TABLE_ITEM, null, null);
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
	
	public long getNumberOfItems(){
		SQLiteDatabase database = this.getReadableDatabase();
		return DatabaseUtils.queryNumEntries(database ,TABLE_ITEM);		
	}
}
