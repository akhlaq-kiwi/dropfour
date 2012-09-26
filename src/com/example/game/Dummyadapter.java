package com.example.game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Dummyadapter extends BaseAdapter{

	 String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
	          "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
	          "Linux", "OS/2" };
	private Context context;
	 public Dummyadapter(Context ctx) {
		 this.context = ctx ;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return values.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup arg2) {
		TextView txt = new TextView(context);
		txt.setText(""+values[pos]);
		return txt;
	}

}