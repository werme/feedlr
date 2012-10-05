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

import java.util.ArrayList;

import com.chalmers.feedlr.R;
import com.chalmers.feedlr.model.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

public class UsersAdapter extends ArrayAdapter<User> {

	private ArrayList<User> users;
	private Context context;

	public UsersAdapter(Context context, int textViewResourceId,
			ArrayList<User> users) {
		super(context, textViewResourceId, users);
		this.users = users;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.user_list_item, null);
		}

		User user = users.get(position);
		if (user != null) {
			CheckedTextView ctv = (CheckedTextView) v.findViewById(R.id.checked_text_view);
			if (ctv != null)
				ctv.setText(user.getUserName());	
		}
		return v;
	}
}