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
import com.facebook.android.Util;
import com.facebook.android.AsyncFacebookRunner.RequestListener;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.ListView;

public class FriendList extends Activity {
	private ArrayList<Friend> friendArray;
	private Facebook facebook = new Facebook(Utils.APP_ID);
	ListView lv;
    private AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
    FriendAdapter mFriendAdapter;
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
			Log.d("name", "In Litsner On complete"+ response);
			try {
				json = Util.parseJson(response);
				final JSONArray friends = json.getJSONArray("data");
				
				friendArray = new ArrayList<Friend>();
				for (int i = 0; i < friends.length(); i++) {
					
					JSONObject friend = friends.getJSONObject(i);
					Friend frnd = new Friend();
					
					frnd.setName(friend.getString("name"));
					frnd.setId(i+1);
					frnd.setFbId(friend.getString("id"));
					friendArray.add(frnd);
					Log.d("name", friend.getString("name"));
				}
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						lv = (ListView)findViewById(R.id.friend_list);
						mFriendAdapter = new FriendAdapter(FriendList.this, friendArray);
						lv.setAdapter(mFriendAdapter);
						
						
					}
				});
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
    
}
