package com.example.game;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
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
	
	
	public FriendAdapter(Context ctx, ArrayList<Friend> paraFriendArray) {
		innerClassFriendArray = paraFriendArray;
		this_ctx = ctx;
		mIconList = new ArrayList<ImageView>();
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
	
		TextView rawTextView = (TextView) convertView.findViewById(R.id.name);	
		rawTextView.setText(innerClassFriendArray.get(position).getName());
		
		ImageView img = (ImageView)convertView.findViewById(R.id.icon);
		String id = innerClassFriendArray.get(position).getFbId();
		
		String url = "http://graph.facebook.com/"+id+"/picture?type=large";
		//updateImage(img, id);
		new LoadImage(img).execute(url);

		
		return convertView;
	}	
	
	public class LoadImage extends AsyncTask<String, Void, Bitmap>{
		private ImageView imv;
       
        public LoadImage(ImageView imv) {
             this.imv = imv;
        }
        @Override
		protected Bitmap doInBackground(String... params) {
			URL img_value = null;
			Bitmap mIcon1 = null;
			try {
				img_value = new URL(params[0]);
				mIcon1 = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return mIcon1;
		}

        @Override
        protected void onPostExecute(Bitmap result) {
           imv.setImageBitmap(result);
           notifyDataSetChanged();
           Log.d("msg", "test");
        }

		
	}

	
	
	public void updateImage(final ImageView img, final String id){
		Thread thrd = new Thread(new Runnable() {
			
			
			
			@Override
			public void run() {
				URL img_value = null;
				Bitmap mIcon1 = null;
				try {
					img_value = new URL("http://graph.facebook.com/"+id+"/picture?type=large");
					mIcon1 = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				img.setImageBitmap(mIcon1);
				notifyDataSetChanged();
				
			}
		});
		thrd.start();
		
		
	}
	
}