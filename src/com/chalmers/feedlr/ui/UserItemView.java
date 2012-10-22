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

package com.chalmers.feedlr.ui;

import com.chalmers.feedlr.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author Olle Werme
 * 
 * 
 */

public class UserItemView extends LinearLayout implements Checkable {

	private boolean checked;
	private TextView textView;

	public UserItemView(Context context) {
		super(context);
	}

	public UserItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public UserItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		textView = (TextView) findViewById(R.id.user_item_text_view);
	}

	@Override
	public boolean isChecked() {
		return checked;
	}

	@Override
	public void setChecked(boolean checked) {
		if (checked) {

			setBackgroundColor(Color.rgb(39, 51, 56));
			textView.setTextColor(Color.rgb(136, 171, 183));
		} else {
			setBackgroundColor(Color.TRANSPARENT);
			textView.setTextColor(Color.WHITE);
		}
		this.checked = checked;
	}

	@Override
	public void toggle() {
		setChecked(!checked);
	}

}
