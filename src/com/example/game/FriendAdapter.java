package com.example.game;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendAdapter extends BaseAdapter{
	private ArrayList<Friend> innerClassFriendArray;
	private Context this_ctx;
	public ArrayList<ImageView> mIconList;
	public ImageLoader imageLoader = new ImageLoader(this_ctx);
	
	public FriendAdapter(Context ctx, ArrayList<Friend> paraFriendArray) {
		innerClassFriendArray = paraFriendArray;
		this_ctx = ctx;
		mIconList = new ArrayList<ImageView>();
		if (Utility.model == null) {
            Utility.model = new FriendsGetProfilePics();
        }
        Utility.model.setListener(this);
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
	
	
		if(convertView == null) {
			// LayoutInflater class is used to instantiate layout XML file into its corresponding View objects.
			LayoutInflater layoutInflater = (LayoutInflater) this_ctx.getSystemService(this_ctx.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.grid_item, null);
			mIconList.add((ImageView)convertView.findViewById(R.id.icon)); 
		} 
		Typeface typeFace=Typeface.createFromAsset(this_ctx.getAssets(),"Orange_LET_Plain.TTF");
		
		TextView rawTextView = (TextView) convertView.findViewById(R.id.name);	
		rawTextView.setText(innerClassFriendArray.get(position).getName());
		
		
		rawTextView.setTypeface(typeFace);
		
		TextView game_id = (TextView) convertView.findViewById(R.id.game_id);	
		game_id.setText(""+innerClassFriendArray.get(position).getGameId());
		
		TextView fb_id = (TextView) convertView.findViewById(R.id.fb_id);	
		fb_id.setText(innerClassFriendArray.get(position).getFbId());
		
		ImageView img = (ImageView)convertView.findViewById(R.id.icon);
		String id = innerClassFriendArray.get(position).getFbId();
		
		TextView time_txt = (TextView) convertView.findViewById(R.id.time);	
		time_txt.setText(innerClassFriendArray.get(position).getTime());
		time_txt.setTypeface(typeFace);
		
		String url = "http://graph.facebook.com/"+id+"/picture?type=small";
		
		img.setImageBitmap(Utility.model.getImage(id, url));
		
		return convertView;
	}

		
	
	
	
}