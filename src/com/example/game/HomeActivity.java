package com.example.game;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class HomeActivity extends Activity {
	private Facebook facebook = new Facebook(Utils.APP_ID);
    private AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_home);
        
        SessionStore.restore(facebook, HomeActivity.this);
        
        if(facebook.isSessionValid()){
        	Intent i = new Intent(getApplicationContext(), UserHome.class);
			startActivity(i);
        }
        
        ImageButton FbLogin = (ImageButton)findViewById(R.id.facebook_login);
        FbLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!facebook.isSessionValid()){
					facebook.authorize(HomeActivity.this, new LoginDialog());
				}else{
					mAsyncRunner.logout(HomeActivity.this, new LogoutListner());
				}
			}
		});
        
    }

    private class LoginDialog implements DialogListener{

		@Override
		public void onComplete(Bundle values) {
			SessionStore.save(facebook, HomeActivity.this);
			Intent i = new Intent(getApplicationContext(), UserHome.class);
			startActivity(i);
		}

		@Override
		public void onFacebookError(FacebookError e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onError(DialogError e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
			
		}
    	
    }
    
    private class LogoutListner implements RequestListener{
		@Override
		public void onComplete(String response, Object state) {
			//Log.d("msg", response);
			SessionStore.clear(HomeActivity.this);
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
