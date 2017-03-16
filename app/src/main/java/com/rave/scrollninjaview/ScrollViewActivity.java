package com.rave.scrollninjaview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.rave.library.NinjaViewHelper;


public class ScrollViewActivity extends Activity {

	private NinjaViewHelper mNinjaViewHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scrollview);

		mNinjaViewHelper = new NinjaViewHelper(this, NinjaViewHelper.ViewPosition.TOP);
		View ninjaView = mNinjaViewHelper.createNinjaViewOnScrollView(R.id.scrollView, R.layout.poppyview);

		ninjaView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(ScrollViewActivity.this, "Click me!", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
