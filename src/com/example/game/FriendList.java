package com.example.game;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.R;
import com.facebook.android.Util;
import com.facebook.android.AsyncFacebookRunner.RequestListener;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FriendList extends Activity {
	//private ArrayList<Friend> friendArray;
	public static JSONArray jsonArray;
	private Facebook facebook = new Facebook(Utils.APP_ID);
	ListView lv;
    private AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
    FriendListAdapter mFriendAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_list);
        SessionStore.restore(facebook, getApplicationContext());
        
        Log.d("name", "In Activity");
        mAsyncRunner.request("me/friends", new FriendRequestListener());
        
        
    }
    private class FriendRequestListener implements RequestListener{

		@Override
		public void onComplete(String response, Object state) {
			JSONObject json;
			//Log.d("name", "In Litsner On complete"+ response);
			try {
				json = Util.parseJson(response);
				jsonArray = json.getJSONArray("data");
				Log.d("name", "In Litsner On complete"+ jsonArray.toString());
				lv = (ListView)findViewById(R.id.friend_list);
				mFriendAdapter = new FriendListAdapter(FriendList.this);
			} catch (FacebookError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		@Override
		public void onIOException(IOException e, Object state) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFileNotFoundException(FileNotFoundException e,
				Object state) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onMalformedURLException(MalformedURLException e,
				Object state) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFacebookError(FacebookError e, Object state) {
			// TODO Auto-generated method stub
			
		}

		
		
	}
    
    public class FriendListAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        FriendList friendsList;
        JSONArray json_array;

        public FriendListAdapter(FriendList friendsList) {
            this.friendsList = friendsList;
            if (Utility.model == null) {
                Utility.model = new FriendsGetProfilePics();
            }
            Utility.model.setListener(this);
            mInflater = LayoutInflater.from(friendsList.getBaseContext());
        }

        @Override
        public int getCount() {
            Log.d("name", ""+jsonArray.length());
        	return jsonArray.length();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            JSONObject jsonObject = null;
            try {
                jsonObject = jsonArray.getJSONObject(position);
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            View hView = convertView;
            if (convertView == null) {
                hView = mInflater.inflate(R.layout.grid_item, null);
                ViewHolder holder = new ViewHolder();
                holder.name = (TextView) hView.findViewById(R.id.name);
                holder.icon = (ImageView) hView.findViewById(R.id.icon);
                hView.setTag(holder);
            }

            ViewHolder holder = (ViewHolder) hView.getTag();
            try {
                    holder.icon.setImageBitmap(Utility.model.getImage(jsonObject.getString("id"), jsonObject.getString("picture")));
                
            } catch (JSONException e) {
                holder.name.setText("");
            }
            try {
                holder.name.setText(jsonObject.getString("name"));
            } catch (JSONException e) {
                holder.name.setText("");
            }
           
            return hView;
        }

    }

    class ViewHolder {
        ImageView icon;
        TextView name;
        
    }
    
}
