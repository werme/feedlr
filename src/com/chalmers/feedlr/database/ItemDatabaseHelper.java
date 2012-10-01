/**
 * Class description
 * 
 * @author David G�ransson
 */

package com.chalmers.feedlr.database;

import java.util.List;

import com.chalmers.feedlr.model.Item;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.DatabaseUtils.InsertHelper;
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
	// 200 insertion method 4.7 seconds
	 	/*public void addListOfItems(List<Item> itemList){
	 		SQLiteDatabase database = this.getWritableDatabase();
			for (Item o: itemList) {
				
				ContentValues temp = new ContentValues();
				temp.put(COLUMN_AUTHOR, o.getUser().getUserName());
				temp.put(COLUMN_BODY, o.getText());
				temp.put(COLUMN_TIMESTAMP, "timestamp");
				temp.put(COLUMN_SOURCE, "Twitter");
				
				database.insert(TABLE_ITEM, null, temp);
				
			}
			database.close();
		}*/
	
	 	public void addListOfItems(List<Item> itemList){
	 		SQLiteDatabase database = this.getWritableDatabase();
	 		database.beginTransaction();
	 		for (Item o: itemList) {
				
				ContentValues temp = new ContentValues();
				temp.put(COLUMN_AUTHOR, o.getUser().getUserName());
				temp.put(COLUMN_BODY, o.getText());
				temp.put(COLUMN_TIMESTAMP, "timestamp");
				temp.put(COLUMN_SOURCE, "Twitter");
				
				database.insert(TABLE_ITEM, null, temp);
				
			}
			database.setTransactionSuccessful();
			database.endTransaction();
			database.close();
		}
	/*
	// 200 insertions 5.1 seconds
	public void addListOfItems(List<Item> itemList){
		
		SQLiteDatabase database = this.getWritableDatabase();
		InsertHelper ih = new InsertHelper(database, "item");
		
		final int authorIH = ih.getColumnIndex(COLUMN_AUTHOR);
        final int bodyIH = ih.getColumnIndex(COLUMN_BODY);
        final int timestampIH = ih.getColumnIndex(COLUMN_TIMESTAMP);
        final int sourceIH = ih.getColumnIndex(COLUMN_SOURCE);
        
		try {
			for (Item o: itemList) {
			ih.prepareForInsert();
			ih.bind(authorIH, o.getUser().getUserName());
			ih.bind(bodyIH, o.getText());
			ih.bind(timestampIH, "Timestamp");
			ih.bind(sourceIH, "Twitter");
			ih.execute();
			}
		}
		finally {
			ih.close();
		}
	}
	*/
	
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
	    SQLiteDatabase database = this.getReadableDatabase();
	 
	    Cursor cursor = database.query(TABLE_ITEM, new String[] { COLUMN_AUTHOR,
	            COLUMN_BODY, COLUMN_TIMESTAMP }, COLUMN_ID + "=?",
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
