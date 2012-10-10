package com.example.game;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;

import android.os.Bundle;
import android.util.Log;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

public class PostOnWallActivity extends Activity {
	private Facebook facebook = new Facebook(Utils.APP_ID);
	private AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_on_wall);
        SessionStore.restore(facebook, getApplicationContext());
       
        
        Intent i = getIntent();
        String name = i.getStringExtra("name");
        String fb_id = i.getStringExtra("fb_id");
        
        JSONObject json = new JSONObject();
        try {
        	 SharedPreferences myPrefs = this.getSharedPreferences("my_pref", MODE_WORLD_READABLE);
             String my_fb_id = myPrefs.getString("my_fb_id", "nothing");
        	
        	json.put("fb_id", fb_id);
			json.put("name", name);
			json.put("invited_by", my_fb_id);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //Log.d("msg", json.toString());
        Bundle param = new Bundle();
        param.putString("message", "Hello "+name+ ", I am playing dropfour game using android application, I invite you to join this game...");
        mAsyncRunner.request(fb_id+"/feed", param, "POST", new PostOnWallLitsner(json.toString()), null);
        
    }
    
    public class PostOnWallLitsner implements RequestListener{
    	String json_data = null;
    	public PostOnWallLitsner(String json){
    		this.json_data = json;
    	}
		@Override
		public void onComplete(String response, Object state) {
			
			//String web_response = Utils.postData("invite_friend.php", json_data);
			Log.d("jsondata",""+json_data);
			
			//Log.d("msg", web_response);
			Intent i = new Intent(PostOnWallActivity.this, FriendList.class);
			startActivity(i);
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
