package com.example.game;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendAdapter extends BaseAdapter{
	private ArrayList<Friend> innerClassFriendArray;
	Context this_ctx;
	public FriendAdapter(Context ctx, ArrayList<Friend> paraFriendArray) {
		innerClassFriendArray = paraFriendArray;
		this_ctx = ctx;
	}

	// How many items are in the data set represented by this Adapter.
	@Override
	public int getCount() {
		return innerClassFriendArray.size();
	}

	// Get the data item associated with the specified position in the data set.
	@Override
	public Object getItem(int position) {
		return innerClassFriendArray.get(position);
	}

// Get the row id associated with the specified position in the list.
	@Override
	public long getItemId(int position) {
		return innerClassFriendArray.get(position).getId();
	}

// Get a View that displays the data at the specified position in the data set.
// You can either create a View manually or inflate it from an XML layout file.
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
	
		if(convertView == null){
			// LayoutInflater class is used to instantiate layout XML file into its corresponding View objects.
			LayoutInflater layoutInflater = (LayoutInflater) this_ctx.getSystemService(this_ctx.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.grid_item, null);
		}
	
		TextView rawTextView = (TextView) convertView.findViewById(R.id.name);	
		rawTextView.setText(innerClassFriendArray.get(position).getName());
		
		
		
		
		
		return convertView;
	}	
}