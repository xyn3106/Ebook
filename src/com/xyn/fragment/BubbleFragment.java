package com.xyn.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.anjoyo.adapter.SignAdapter;
import com.anjoyo.myview.MyScrollListView;
import com.anjoyo.net.ThreadPoolUtils;
import com.anjoyo.thread.HttpGetThread;
import com.xyn.activity.BubblePostActivity;
import com.xyn.activity.FoodDetailsActivity;
import com.xyn.bean.SignInfo;
import com.xyn.source.Model;
import com.xyn.source.R;
import com.xyn.source.R.id;
import com.xyn.source.R.layout;
import com.xyn.utils.MyJson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

public class BubbleFragment extends Fragment {
	private static String TAG = "BubbleFragment";
	private Button mPost_btn,ListBottem1,ListBottem2;
	private LinearLayout mLayout;
	private ListView left;
//	private ListView right;
	private MyScrollListView mScroll;
	private SignAdapter signAdapter1 = null;
//	private SignAdapter signAdapter2 = null;
	private List<SignInfo> leftlist = new ArrayList<SignInfo>();
//	private List<SignInfo> rightlist = new ArrayList<SignInfo>();
	private MyOnclickListener mOnclickListener = new MyOnclickListener();
	private String url = "http://112.74.205.86:8080/web/getSpecifyCategoryNews";

	@Override
	public void onCreate( Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		signAdapter1 = new SignAdapter(leftlist, getActivity());
//		signAdapter2 = new SignAdapter(rightlist, getActivity());
		ListBottem1 = new Button(getActivity());
		ListBottem1.setText("加载更多");
		ListBottem1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Message msg = hand.obtainMessage();
				msg.what = 201;
				hand.sendMessage(msg);
				}});
		
		Message msg = hand.obtainMessage();
		msg.what = 200;
		hand.sendMessage(msg);
		
		Log.e(TAG, "onCreate");
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_bubble, container, false);
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mPost_btn = (Button) view.findViewById(R.id.titlebar_post_btn);
		mPost_btn.setOnClickListener(mOnclickListener);
//		left = new ListView(getActivity());
//		right = new ListView(getActivity());
//		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,1);
//		left.setLayoutParams(lp);
//		right.setLayoutParams(lp);
		left = (ListView) view.findViewById(R.id.bubble_listview);
		left.setAdapter(signAdapter1);
		left.addFooterView(ListBottem1, null, false);
//		right.setAdapter(signAdapter2);
//		mLayout = (LinearLayout) view.findViewById(R.id.bubble_group);
//		mScroll = new MyScrollListView(getActivity(), left, right);
//		mLayout.addView(mScroll);
//		ThreadPoolUtils.execute(new HttpGetThread(hand, Model.SELECTSIGNURL));TODO
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
	
	private class MyOnclickListener implements View.OnClickListener {
		public void onClick(View v) {
			int mID = v.getId();
			if(mID==R.id.titlebar_post_btn){
			Intent intent = new Intent(getActivity(), BubblePostActivity.class);
			startActivity(intent);
			}
		}

	}

	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 200) {
				
				//测试，直接生成列表里的内容
				List<SignInfo> list = new ArrayList<SignInfo>();
				for (int i = 0; i < 8; i++) {
					SignInfo info = new SignInfo();
					info.setb_id("b"+i);
					info.setu_id("u"+i);
					info.setu_name("name"+i);
					info.setb_content("content"+i);
					info.setb_img("image.jpg"+i);
					info.setb_time("time"+i);
					info.setb_likecount(""+(32+i));
					list.add(info);
				}
//				String value = (String) msg.obj;
//				if(value != null){
//					List<SignInfo> list = new MyJson().getSignList(value);
					for (int i = 0; i < list.size(); i++) {
//						if (i % 2 == 0) {
							leftlist.add(list.get(i));
							continue;
//						}
//						else {
//							rightlist.add(list.get(i));
//							continue;
//						}
					}
//				}
				signAdapter1.notifyDataSetChanged();
//				signAdapter2.notifyDataSetChanged();
			}
			else if(msg.what==201){
				List<SignInfo> list = new ArrayList<SignInfo>();
				for (int i = 0; i < 6; i++) {
					SignInfo info = new SignInfo();
					info.setb_id("b"+i);
					info.setu_id("u"+i);
					info.setu_name("name"+i);
					info.setb_content("content"+i);
					info.setb_img("image.jpg"+i);
					info.setb_time("time"+i);
					info.setb_likecount(""+(32+i));
					list.add(info);
				}
					for (int i = 0; i < list.size(); i++) {
						leftlist.add(list.get(i));
					}
//					ListBottem1.setVisibility(View.GONE);
				signAdapter1.notifyDataSetChanged();
			}
//			else if(msg.what==202){
//				List<SignInfo> list = new ArrayList<SignInfo>();
//				for (int i = 0; i < 6; i++) {
//					SignInfo info = new SignInfo();
//					info.setName("name"+i);
//					info.setSigncontent(("content"+i));
//					info.setSignimage(("image.jpg"));
//					info.setSignlevel("3");
//					info.setSigntime(("time"+i));
//					list.add(info);
//				}
//					for (int i = 0; i < list.size(); i++) {
//						rightlist.add(list.get(i));
//					}
//				ListBottem2.setVisibility(View.GONE);
//				signAdapter2.notifyDataSetChanged();
//			}
		};
	};
}
