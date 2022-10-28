package com.zezoo.emergency;

import static com.zezoo.emergency.Constants.FIRST_COLUMN;
import static com.zezoo.emergency.Constants.FOURTH_COLUMN;
import static com.zezoo.emergency.Constants.SECOND_COLUMN;
import static com.zezoo.emergency.Constants.THIRD_COLUMN;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class TruckerActivity extends Activity {
	SharedPreferences archives;
	public static final String ARCHIVES_NAME = "an";
	static final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	ListView mListView;
	ArrayList<Item> arrayList;
	ItemListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trucker);
		archives = getSharedPreferences(ARCHIVES_NAME, MODE_PRIVATE);
		SharedPreferences.Editor editor = archives.edit();
		mListView = (ListView) findViewById(R.id.listLocation);
		arrayList = new ArrayList<Item>();
		for (int i = 1; i <= archives.getInt("itemsNumber", 0); i++) {
			Item item = new Item("ÇáãßÇä : " + archives.getString("A_" + String.valueOf(i), "A"),
					"X:" + archives.getString("X_" + String.valueOf(i), "X"),
					"Y" + archives.getString("Y_" + String.valueOf(i), "Y"),
					archives.getString("T_" + String.valueOf(i), "T"));
			arrayList.add(item);
		}

		adapter = new ItemListAdapter(getApplicationContext(), R.layout.list_item, arrayList);
		mListView.setAdapter(adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_1, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.delete:
			arrayList.clear();
			adapter = new ItemListAdapter(getApplicationContext(), R.layout.list_item, arrayList);
			mListView.setAdapter(adapter);
			archives = getSharedPreferences(ARCHIVES_NAME, MODE_PRIVATE);
			SharedPreferences.Editor editor = archives.edit();
			editor.clear();
			editor.putInt("itemsNumber", 0);
			editor.commit();
			break;
		}
		return true;
	}
}
