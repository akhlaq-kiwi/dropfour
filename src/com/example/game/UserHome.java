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
import com.facebook.android.R;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.drm.ProcessedData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UserHome extends Activity implements OnItemClickListener {
	
	private ArrayList<Friend> invitesArray, my_turnsArray, their_turnArray;
	private Facebook facebook = new Facebook(Utils.APP_ID);
	ListView lv;
    private AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
    FriendAdapter mFriendAdapter;
    ProgressDialog progressbar;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home);
        SessionStore.restore(facebook, getApplicationContext());
        //progressbar = ProgressDialog.show(UserHome.this, "Wait", "Please Wait..");
        
        mAsyncRunner.request("me", new GetInfo());
        
       ImageButton img_logout = (ImageButton)findViewById(R.id.logout);
       img_logout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				mAsyncRunner.logout(getApplicationContext(), new logoutListner());
				return false;
			}
		});
        
        ImageView img = (ImageView)findViewById(R.id.invite_friends);
        img.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				Intent myIntent = new Intent(UserHome.this, FriendList.class);
	            startActivity(myIntent);
				return false;
			}
		});
        
        
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
				SharedPreferences.Editor editor = UserHome.this.getSharedPreferences("my_pref", MODE_WORLD_READABLE).edit();
				editor.putString("my_fb_id", id);
				editor.commit();
				
				JSONObject json_data = new JSONObject();
				json_data.put("name", name);
				json_data.put("fb_id", id);
				
            	Utils.postData("user.php", json_data.toString());
            	String all_data_json = Utils.postData("all_data.php", json_data.toString());
            	
            	JSONArray invite = new JSONObject(all_data_json).getJSONArray("game_invites");
            	invitesArray = new ArrayList<Friend>();
            	if(invite.length() > 0){
	            	for(int i=0; i<invite.length(); i++){
	            		JSONObject friend = invite.getJSONObject(i);
						
	            		Friend invite_friend = new Friend();
						
						invite_friend.setName(friend.getString("name"));
						invite_friend.setId(i+1);
						invite_friend.setGameId(friend.getLong("game_id"));
						invite_friend.setFbId(friend.getString("id"));
						invitesArray.add(invite_friend);
	            	}
            	}
            	JSONArray my_turns = new JSONObject(all_data_json).getJSONArray("my_turns");
            	my_turnsArray = new ArrayList<Friend>();
            	
            	if(my_turns.length() > 0){
	            	for(int i=0; i<my_turns.length(); i++){
	            		JSONObject friend = my_turns.getJSONObject(i);
						
	            		Friend my_turns_friend = new Friend();
						
	            		my_turns_friend.setName(friend.getString("name"));
	            		my_turns_friend.setId(i+1);
	            		my_turns_friend.setGameId(friend.getLong("game_id"));
	            		my_turns_friend.setFbId(friend.getString("id"));
	            		my_turnsArray.add(my_turns_friend);
	            	}
            	}
            	
            	JSONArray their_turns = new JSONObject(all_data_json).getJSONArray("their_turns");
            	their_turnArray = new ArrayList<Friend>();
            	Log.d("msg", ""+their_turns.length());
            	if(their_turns.length() > 0){
	            	for(int i=0; i<their_turns.length(); i++){
	            		JSONObject friend = their_turns.getJSONObject(i);
						
	            		Friend their_turns_friend = new Friend();
						
	            		their_turns_friend.setName(friend.getString("name"));
	            		their_turns_friend.setId(i+1);
	            		their_turns_friend.setGameId(friend.getLong("game_id"));
	            		their_turns_friend.setFbId(friend.getString("id"));
	            		their_turnArray.add(their_turns_friend);
	            	}
            	}
            	
            	runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						ListView game_invites = (ListView)findViewById(R.id.game_invites);
						FriendAdapter innviteFriendAdapter = new FriendAdapter(UserHome.this, invitesArray);
						game_invites.setAdapter(innviteFriendAdapter);
						Utils.setListViewHeightBasedOnChildren(game_invites);
						game_invites.setOnItemClickListener(UserHome.this);
						if(innviteFriendAdapter.getCount()==0){
							LinearLayout game_invite_container = (LinearLayout)findViewById(R.id.invite_container);
							game_invite_container.setVisibility(View.GONE);
						}
						
						ListView my_turns_list = (ListView)findViewById(R.id.my_turn);
						FriendAdapter my_turnsFriendAdapter = new FriendAdapter(UserHome.this, my_turnsArray);
						my_turns_list.setAdapter(my_turnsFriendAdapter);
						Utils.setListViewHeightBasedOnChildren(my_turns_list);
						if(my_turnsFriendAdapter.getCount()==0){
							LinearLayout my_turn_container = (LinearLayout)findViewById(R.id.my_turn_container);
							my_turn_container.setVisibility(View.GONE);
						}
						
						ListView their_turn_list = (ListView)findViewById(R.id.their_turn);
						FriendAdapter their_turnFriendAdapter = new FriendAdapter(UserHome.this, their_turnArray);
						their_turn_list.setAdapter(their_turnFriendAdapter);
						Utils.setListViewHeightBasedOnChildren(their_turn_list);
						if(their_turnFriendAdapter.getCount()==0){
							LinearLayout their_turn_container = (LinearLayout)findViewById(R.id.their_turn_container);
							their_turn_container.setVisibility(View.GONE);
						}
						
					}
				});
            	//progressbar.dismiss();
			} catch (JSONException e) {
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
	

	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		TextView game_id = (TextView)findViewById(R.id.game_id);
		TextView fb_id = (TextView)findViewById(R.id.fb_id);
		Intent i = new Intent(UserHome.this, StartGameActivity.class);
		i.putExtra("game_id", game_id.getText());
		i.putExtra("fb_id", fb_id.getText());
		startActivity(i);
		
	}
	
}
