package com.example.game;


import android.app.Activity;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Im;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SlidingDrawer;
import android.widget.Toast;

public class GameActivity extends Activity {
	public static int pos;
	int i = 0;
	int firstClick,secondClick;
	ImageAdapter im = new ImageAdapter(this);
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(im);
 
        gridview.setOnItemClickListener(new OnItemClickListener() {

        	public void onItemClick(AdapterView<?> parent, final View v, int position, long id) {
                pos = position;
        		runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						while(true){
			                Log.d("msg","Running Thread!");
			                i = updateGrid();
			                if(i==0){
						    	break;
						    }
			               try {
								Thread.sleep(1000);
								v.invalidate();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		        		}
						
					}
				});
                
            }
		});
	}
	
	public int updateGrid(){
		i = im.switchImages();
	    im.notifyDataSetChanged();
	    return i;
	}
	
}
