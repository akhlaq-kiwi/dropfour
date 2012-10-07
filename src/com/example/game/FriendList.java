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
        
        Bundle extras = getIntent().getExtras();
        String apiResponse = extras.getString("API_RESPONSE");
        try {
			jsonArray = new JSONObject(apiResponse).getJSONArray("data");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        lv = (ListView) findViewById(R.id.friend_list);
        //friendsList.setOnItemClickListener(this);
        lv.setAdapter(new FriendListAdapter(this));
    }
    private class FriendRequestListener implements RequestListener{

		@Override
		public void onComplete(String response, Object state) {
			JSONObject json;
			//Log.d("name", "In Litsner On complete"+ response);
			try {
				json = Util.parseJson(response);
				jsonArray = json.getJSONArray("data");
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
                hView = mInflater.inflate(R.layout.friend_item, null);
                ViewHolder holder = new ViewHolder();
                holder.profile_pic = (ImageView) hView.findViewById(R.id.profile_pic);
                holder.name = (TextView) hView.findViewById(R.id.name);
                holder.info = (TextView) hView.findViewById(R.id.info);
                hView.setTag(holder);
            }

            ViewHolder holder = (ViewHolder) hView.getTag();
            try {
                    holder.profile_pic.setImageBitmap(Utility.model.getImage(
                            jsonObject.getString("id"), jsonObject.getString("picture")));
                
            } catch (JSONException e) {
                holder.name.setText("");
            }
            try {
                holder.name.setText(jsonObject.getString("name"));
            } catch (JSONException e) {
                holder.name.setText("");
            }
            try {
                    holder.info.setText(jsonObject.getJSONObject("location").getString("name"));
                
            } catch (JSONException e) {
                holder.info.setText("");
            }
            return hView;
        }

    }

    class ViewHolder {
        ImageView profile_pic;
        TextView name;
        TextView info;
    }
    
}
