package com.example.testegame;


import android.os.Bundle;
import android.view.WindowManager;
import android.app.Activity;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.flume2d.Engine;

public class MainActivity extends AndroidApplication {
    @Override
        public void onCreate (Bundle savedInstanceState) 
        {
                super.onCreate(savedInstanceState);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                
                initialize(new Engine(new world()), false);               
        }
}

/*
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
*/