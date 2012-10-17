package com.example.game;


import android.app.Activity;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Im;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SlidingDrawer;
import android.widget.Toast;

public class GameActivity extends Activity {
	public static int pos;
	int i = 0;
	int firstClick,secondClick;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.game);
        
        
	}
	
	
	
}
