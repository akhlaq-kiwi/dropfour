package com.example.game;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.R;
import com.facebook.android.Util;
import com.facebook.android.AsyncFacebookRunner.RequestListener;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class FriendList extends Activity implements OnItemClickListener  {
	ListView lv;
	FriendAdapter mFriendAdapter;
	ArrayList<Friend> friendArray = new ArrayList<Friend>();
	private Facebook facebook = new Facebook(Utils.APP_ID);
	private AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_list);
        SessionStore.restore(facebook, getApplicationContext());
        
        mAsyncRunner.request("me/friends", new FriendListListener());
        
    }
    
    private class FriendListListener implements RequestListener{

		@Override
		public void onComplete(String response, Object state) {
			
			JSONObject json;
			
			try {
				json = Util.parseJson(response);
				final JSONArray friends = json.getJSONArray("data");
				
				friendArray = new ArrayList<Friend>();
				for (int i = 0; i < friends.length(); i++) {
					
					JSONObject friend = friends.getJSONObject(i);
					Friend frnd = new Friend();
					
					frnd.setName(friend.getString("name"));
					frnd.setId(i+1);
					frnd.setGameId(i+1);
					frnd.setFbId(friend.getString("id"));
					friendArray.add(frnd);
					
				}
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						lv = (ListView)findViewById(R.id.friend_list);
						mFriendAdapter = new FriendAdapter(FriendList.this, friendArray);
						lv.setAdapter(mFriendAdapter);
						lv.setOnItemClickListener(FriendList.this);
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
			
		}

		@Override
		public void onFileNotFoundException(FileNotFoundException e, Object state) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onMalformedURLException(MalformedURLException e, Object state) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFacebookError(FacebookError e, Object state) {
			// TODO Auto-generated method stub
			
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		final TextView fb_id = (TextView)v.findViewById(R.id.fb_id);
		final TextView name = (TextView)v.findViewById(R.id.name);
		new AlertDialog.Builder(this).setTitle("Invite Friend To Jon Drop Four!")
			.setMessage("Massage would be posted on "+name.getText()+"'s wall")
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent i = new Intent(FriendList.this, PostOnWallActivity.class);
					i.putExtra("name", name.getText());
					i.putExtra("fb_id", fb_id.getText());
					startActivity(i);
				}
			}).setNegativeButton("No", null).show();
	}
    
}
