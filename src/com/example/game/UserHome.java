package com.example.game;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UserHome extends Activity {
	
	private ArrayList<Friend> friendArray;
	private Facebook facebook = new Facebook(Utils.APP_ID);
	ListView lv;
    private AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
    FriendAdapter mFriendAdapter;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home);
        SessionStore.restore(facebook, getApplicationContext());
        
        mAsyncRunner.request("me", new GetInfo());
        
        mAsyncRunner.request("me/friends", new FriendRequestListener());
        
        ((Button)findViewById(R.id.logout)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mAsyncRunner.logout(getApplicationContext(), new logoutListner());
			}
		});
        
        ((Button)findViewById(R.id.invite_friends)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle params = new Bundle();
		        params.putString("fields", "name, picture, location");
		        mAsyncRunner.request("me/friends", params, new FriendListListener());
				
				
				
			}
		});
	}
	private class FriendListListener implements RequestListener{

		@Override
		public void onComplete(String response, Object state) {
			Log.d("name", response);
			Intent myIntent = new Intent(getApplicationContext(), FriendList.class);
            myIntent.putExtra("API_RESPONSE", response);
            startActivity(myIntent);
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
	
	
	private class logoutListner implements RequestListener{

		@Override
		public void onComplete(String response, Object state) {
			SessionStore.clear(getApplicationContext());
			Intent i = new Intent(getApplicationContext(), HomeActivity.class);
			startActivity(i);
			
		}

		@Override
		public void onIOException(IOException e, Object state) {
			
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
	private class GetInfo implements RequestListener{

		@Override
		public void onComplete(String response, Object state) {
			JSONObject profile;
			try {
				profile = new JSONObject(response);
				final String name = profile.getString("name");
				final String id = profile.getString("id");
				
				final ImageView user_picture;
				user_picture=(ImageView)findViewById(R.id.image);
				URL img_value = null;
				img_value = new URL("http://graph.facebook.com/"+id+"/picture?type=large");
				final Bitmap mIcon1 = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
				 
				runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    	((TextView)findViewById(R.id.top_text)).setText("Hello "+name);
                    	user_picture.setImageBitmap(mIcon1);
                    	JSONObject json_data = new JSONObject();
                    	
                    	try {
							json_data.put("name", name);
							json_data.put("fb_profileid", id);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    	
                    	
                    	String response  = Utils.postData("user.php", json_data.toString());
                    	Log.d("response", response);
                    }
 
                });
				
				
				
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
		}

		@Override
		public void onIOException(IOException e, Object state) {
			
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
	
	private class FriendRequestListener implements RequestListener{

		@Override
		public void onComplete(String response, Object state) {
			JSONObject json;
			
			try {
				json = Util.parseJson(response);
				final JSONArray friends = json.getJSONArray("data");
				
				friendArray = new ArrayList<Friend>();
				for (int i = 0; i < 5; i++) {
					
					JSONObject friend = friends.getJSONObject(i);
					Friend frnd = new Friend();
					
					frnd.setName(friend.getString("name"));
					frnd.setId(i+1);
					frnd.setFbId(friend.getString("id"));
					friendArray.add(frnd);
					
				}
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						lv = (ListView)findViewById(R.id.game_invites);
						mFriendAdapter = new FriendAdapter(UserHome.this, friendArray);
						lv.setAdapter(mFriendAdapter);
						
						
						
						Utils.setListViewHeightBasedOnChildren(lv);
						
						ListView your_turn = (ListView)findViewById(R.id.your_turn);
						your_turn.setAdapter(mFriendAdapter);
						Utils.setListViewHeightBasedOnChildren(your_turn);
						
						ListView their_turn = (ListView)findViewById(R.id.their_turn);
						their_turn.setAdapter(mFriendAdapter);
						Utils.setListViewHeightBasedOnChildren(their_turn);
						
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
