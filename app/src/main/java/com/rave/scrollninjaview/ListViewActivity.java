package com.rave.scrollninjaview;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.rave.library.NinjaViewHelper;

import java.util.ArrayList;


public class ListViewActivity extends Activity {

	ArrayList<String> dataList = new ArrayList<>();
	private NinjaViewHelper mViewHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview);
		for (int i = 0; i < 15; i++) {
			dataList.add("list item" + i);
		}
		mViewHelper = new NinjaViewHelper(this);
		View ninjaView = mViewHelper.createPoppyViewOnListView(R.id.listView1, R.layout.poppyview, new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				Log.d("ListViewActivity", "onScrollStateChanged");
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				Log.d("ListViewActivity", "onScroll");
			}
		});

		ninjaView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(ListViewActivity.this, "Click me!", Toast.LENGTH_SHORT).show();
			}
		});

		final ListView listView1 = (ListView)findViewById(R.id.listView1);
		listView1.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if(convertView == null) {
					convertView = getLayoutInflater().inflate(R.layout.listitem_song, null);
				}
				((TextView) convertView).setText(getItem(position));
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				return dataList.get(position).hashCode();
			}

			@Override
			public String getItem(int position) {
				return dataList.get(position);
			}

			@Override
			public int getCount() {
				return dataList.size();
			}
		});
	}

}
