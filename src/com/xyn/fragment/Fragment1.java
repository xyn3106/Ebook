package com.xyn.fragment;

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
import android.widget.LinearLayout;

public class Fragment1 extends Fragment {
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
		return inflater.inflate(R.layout.fragment_1, container, false);
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView(view);
		Log.e(TAG, "onViewCreated");
	}
	
	private void initView(View view) {
		LinearLayout L1 = (LinearLayout) view.findViewById(R.id.L1);
		LinearLayout L2 = (LinearLayout) view.findViewById(R.id.L2);
		LinearLayout L3 = (LinearLayout) view.findViewById(R.id.L3);
		L1.setOnClickListener(ClickListener);
		L2.setOnClickListener(ClickListener);
		L3.setOnClickListener(ClickListener);
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
			case R.id.L1:
				Intent intent = new Intent(getActivity(), ListViewActivity.class);
				intent.putExtra("what", 1);
				startActivity(intent);
				break;
			case R.id.L2:
				Intent intent2 = new Intent(getActivity(), ListViewActivity.class);
				intent2.putExtra("what", 2);
				startActivity(intent2);
				break;
			case R.id.L3:
				Intent intent3 = new Intent(getActivity(), ListViewActivity.class);
				intent3.putExtra("what", 3);
				startActivity(intent3);
				break;
			}
		}
	}

}
