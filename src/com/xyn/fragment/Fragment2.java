package com.xyn.fragment;

import com.xyn.activity.GameActivity;
import com.xyn.activity.ListViewActivity;
import com.xyn.ebook.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Fragment2 extends Fragment {
	private static String TAG = "TuanFragment";
	private MyOnClickListener ClickListener = new MyOnClickListener();
	

	@Override
	public void onCreate( Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(TAG, "onCreate");
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e(TAG, "onCreateView");
		return inflater.inflate(R.layout.fragment_2, container, false);
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView(view);
		Log.e(TAG, "onViewCreated");
	}
	
	private void initView(View view) {
		Button bt = (Button) view.findViewById(R.id.game);
		bt.setOnClickListener(ClickListener);
	}
	
	@Override
	public void onStart(){
		super.onStart();
//		Log.e(TAG, "onStart");
	}
	@Override
	public void onResume(){
		super.onResume();
		Log.e(TAG, "onResume");
	}
	@Override
	public void onPause(){
		Log.e(TAG, "onPause");
		super.onPause();
	}
	@Override
	public void onStop(){
//		Log.e(TAG, "onStop");
		super.onStop();
	}
	@Override
	public void onDestroyView(){
		Log.e(TAG, "onDestroyView");
		super.onDestroyView();
	}
	@Override
	public void onDestroy(){
		Log.e(TAG, "onDestroy");
		super.onDestroy();
	}

	private class MyOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			int ID = v.getId();
			switch (ID){
			case R.id.game:
				Intent intent = new Intent(getActivity(), GameActivity.class);
				startActivity(intent);
				break;
			}
		}
	}

}
