package com.xyn.activity;

import com.xyn.source.R;
import com.xyn.source.R.drawable;
import com.xyn.source.R.id;
import com.xyn.source.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.LinearLayout;
/**
 * 软件启动界面
 * */
public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if(FrameActivity.isRunning){
			Intent intent = new Intent(StartActivity.this,FrameActivity.class);
			startActivity(intent);
			finish();
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_my01);
		LinearLayout mLinear = (LinearLayout) findViewById(R.id.Fragment01Linear);
		mLinear.setBackgroundResource(R.drawable.ic_splash_screen);

		new Thread() {
			public void run() {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message msg = hand.obtainMessage();
				hand.sendMessage(msg);
			}

		}.start();
	};

	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (isFristRun()) {
				Intent intent = new Intent(StartActivity.this,MainActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(StartActivity.this,FrameActivity.class);
				startActivity(intent);
			}
			finish();
		};
	};

	private boolean isFristRun() {
		SharedPreferences sharedPreferences = this.getSharedPreferences(
				"share", MODE_PRIVATE);
		boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
		if (!isFirstRun) {
			return false;
		} else {
			Editor editor = sharedPreferences.edit();
			editor.putBoolean("isFirstRun", false);
			editor.commit();
			return true;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {

		}
		return true;
	}
}
