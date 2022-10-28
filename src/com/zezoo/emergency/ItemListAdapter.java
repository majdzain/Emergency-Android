package com.zezoo.emergency;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ItemListAdapter extends ArrayAdapter<Item> {

	Context mContext;
	int mResource;

	public ItemListAdapter(Context context, int resource, ArrayList<Item> objects) {
		super(context, resource, objects);
		this.mContext = context;
		this.mResource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String locationN = getItem(position).getLocationName();
		String locationX = getItem(position).getLongitude();
		String locationY = getItem(position).getLatitude();
		String locationT = getItem(position).getTime();

		Item i = new Item(locationN, locationX, locationY, locationT);

		LayoutInflater inflater = LayoutInflater.from(mContext);
		convertView = inflater.inflate(mResource, parent, false);

		TextView tv1 = (TextView) convertView.findViewById(R.id.locationName);
		TextView tv2 = (TextView) convertView.findViewById(R.id.longtitude);
		TextView tv3 = (TextView) convertView.findViewById(R.id.latitude);
		TextView tv4 = (TextView) convertView.findViewById(R.id.time);

		tv1.setText(locationN);
		tv2.setText(locationX);
		tv3.setText(locationY);
		tv4.setText(locationT);

		return convertView;
	}

}
