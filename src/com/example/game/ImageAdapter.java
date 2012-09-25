package com.example.game;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

	private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
	}
	public int switchImages(){
		
		int length = mThumbIds.length;
		if(GameActivity.pos < length-5){
			Integer help = mThumbIds[GameActivity.pos];
	        mThumbIds[GameActivity.pos]=mThumbIds[GameActivity.pos+5];
	        mThumbIds[GameActivity.pos+5]=help;
	        GameActivity.pos = GameActivity.pos+5;
	        return GameActivity.pos;
		}else{
			return 0;
		}
	}
	
	// references to our images
    public Integer[] mThumbIds = {
            R.drawable.black, R.drawable.black, R.drawable.black, R.drawable.black, R.drawable.black,
            R.drawable.white, R.drawable.white, R.drawable.white, R.drawable.white, R.drawable.white,
            R.drawable.white, R.drawable.white, R.drawable.white, R.drawable.white, R.drawable.white,
            R.drawable.white, R.drawable.white, R.drawable.white, R.drawable.white, R.drawable.white,
            R.drawable.white, R.drawable.white, R.drawable.white, R.drawable.white, R.drawable.white,
    };

}
