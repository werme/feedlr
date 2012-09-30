package com.chalmers.feedlr.gui;

import com.chalmers.feedlr.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class DisplayItems extends ListActivity {
	
	private String[] testArray;
	private ArrayAdapter<String> adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		testArray = getResources().getStringArray(R.array.test);    
		
	    adapter = new ArrayAdapter<String>(this, R.layout.feed_list_layout, R.id.display_item, testArray);
	    setListAdapter(adapter);

	}

}
