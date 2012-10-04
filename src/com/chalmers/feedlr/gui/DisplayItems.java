/**
 * Class description
 * @author Robin Hammaräng
 */

package com.chalmers.feedlr.gui;

import java.util.ArrayList;
import java.util.List;

import com.chalmers.feedlr.R;
import com.chalmers.feedlr.database.ItemDatabaseHelper;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class DisplayItems extends ListActivity {

	private ArrayAdapter<String> adapter;
	private ItemDatabaseHelper db;
	private List<String> list = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		fillFeed();
		// testArray = getResources().getStringArray(R.array.test);
		adapter = new ArrayAdapter<String>(this, R.layout.feed_list_layout,
				R.id.display_item, list);
		setListAdapter(adapter);

	}

	/**
	 * A method to statically add items from the database to the feed.
	 */
	public void fillFeed() {
		db = new ItemDatabaseHelper(this.getApplicationContext());

		db.addItem("David Göransson", "Hej, David här är din body!",
				"12:47", "Facebook");
		db.addItem("Olle Werme", "Hej, Olle här är din body!", "13:37",
				"Twitter");
		list.add("HEJ!");
//		list.add(db.getRow(1));
//		list.add(db.getRow(2));
	}

}
