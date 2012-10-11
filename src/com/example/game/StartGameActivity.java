package com.example.game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;

public class StartGameActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_game);
        
        Intent i = getIntent();
        String fb_id = i.getStringExtra("fb_id");
        String game_id = i.getStringExtra("game_id");
        SharedPreferences myPrefs = this.getSharedPreferences("my_pref", MODE_WORLD_READABLE);
        String my_fb_id = myPrefs.getString("my_fb_id", "nothing");
        
        JSONObject json = new JSONObject();
        try {
			json.put("my_fb_id", my_fb_id);
			json.put("opponent_fb_id", fb_id);
			json.put("game_id", game_id);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String response = Utils.postData("check_game_status.php", json.toString());
        try {
			JSONObject gameObj = new JSONObject(response).getJSONObject("game_data");
			JSONObject my_dataObj = new JSONObject(response).getJSONObject("my_data");
			JSONObject opponent_dataObj = new JSONObject(response).getJSONObject("opponent_data");
			Button start_game = (Button)findViewById(R.id.start_new);
	        if(gameObj.getInt("status")==1){
	        	start_game.setText("Play With "+opponent_dataObj.getString("name"));
	        }
	        //notifyAll();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        
    }
    
    
}
