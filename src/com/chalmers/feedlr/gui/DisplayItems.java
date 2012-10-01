/**
 * Class description
 * @author Robin Hammar�ng
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
	 * A function to statically add items from the database to the feed.
	 * Currently relying on that addItem returns the ID the post was added to.
	 */
	public void fillFeed() {
		db = new ItemDatabaseHelper(this.getApplicationContext());
		db.deleteTable();
		long tmp1 = 0;
		tmp1 = db.addItem("David G�ransson", "Hej, David h�r �r din body!",
				"12:47", "Facebook");
		long tmp2 = 0;
		tmp2 = db.addItem("Olle Werme", "Hej, Olle h�r �r din body!", "13:37",
				"Twitter");
		list.add(db.getRow((int) tmp1));
		list.add(db.getRow((int) tmp2));
	}

}
