package com.xyn.fragment;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.anjoyo.adapter.FoodAdapter;
import com.xyn.activity.FrameActivity;
import com.xyn.activity.FoodDetailsActivity;
import com.xyn.bean.FoodModel;
import com.xyn.source.Model;
import com.xyn.source.MyApplication;
import com.xyn.source.R;
import com.xyn.utils.MyJson;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class FoodFragment extends Fragment {
	private static String TAG = "TuanFragment";
	private LinearLayout mTuan_search_Layout;
	private TextView mTuan_mytuan_txt;
	private ProgressBar mLoadingProgress;
	private TextView mTuan_fenlei_btn1, mTuan_xuexiao_btn2, mTuan_paixu_btn3;
	private ListView mListView;
	private List<FoodModel> FoodList = new ArrayList<FoodModel>();
	private MyOnClickListener myclicklistener = new MyOnClickListener();
	private MainListOnItemClickListener myListClickListener = new MainListOnItemClickListener();
	private FoodAdapter mAdapter = null;
	private Button mTuan_Refresh_btn;
	private Button ListBottem;
	private boolean Loading = false;
	private boolean firstLoad = true;
	private int LoadSize = 6;
	private int LoadPosition = 0;
	private String params = "startnid="+LoadPosition+"&count="+LoadSize+"&s_id="+1+"&sort="+0;
	private String url = Model.HTTPURL+"getSpecifyCategoryNews";
//	private String url = "http://172.18.35.238:8080/web/getSpecifyCategoryNews";
//	private String url = "http://bdream.xyz:8080/web/getSpecifyCategoryNews";

	@Override
	public void onCreate( Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new FoodAdapter(FoodList,getActivity());
		ListBottem = new Button(getActivity());
		ListBottem.setText("点击加载更多");
		ListBottem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(Loading) return;
				//必须动态生成请求参数，因为默认参数是String对象，String是静态的
				String params = "startnid="+LoadPosition+"&count="+LoadSize+"&s_id="+1+"&sort="+0;
				HttpPost(url,params);
				}});
		Log.e(TAG, "onCreate");
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e(TAG, "onCreateView");
		return inflater.inflate(R.layout.fragment_food, container, false);
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView(view);
		Log.e(TAG, "onViewCreated");
	}
	
	private void initView(View view) {
		mTuan_Refresh_btn = (Button) view.findViewById(R.id.titlebar_refresh_btn);
		mTuan_fenlei_btn1 = (TextView) view.findViewById(R.id.Tuan_title_textbtn1);
		mTuan_xuexiao_btn2 = (TextView) view.findViewById(R.id.Tuan_title_textbtn2);
		mTuan_paixu_btn3 = (TextView) view.findViewById(R.id.Tuan_title_textbtn3);
		mListView = (ListView) view.findViewById(R.id.FoodListView);
		mTuan_search_Layout = (LinearLayout) view.findViewById(R.id.Tuan_search);
		mLoadingProgress = (ProgressBar) view.findViewById(R.id.loading_progress);
		mTuan_search_Layout.setOnClickListener(myclicklistener);
		mTuan_Refresh_btn.setOnClickListener(myclicklistener);
		mTuan_fenlei_btn1.setOnClickListener(myclicklistener);
		mTuan_xuexiao_btn2.setOnClickListener(myclicklistener);
		mTuan_paixu_btn3.setOnClickListener(myclicklistener);
		mListView.addFooterView(ListBottem, null, false);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(myListClickListener);
		if(firstLoad){
			firstLoad=false;
			HttpPost(url,params);
		}
		if(Loading)
			mLoadingProgress.setVisibility(View.VISIBLE);
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

	private class MyOnClickListener implements View.OnClickListener {
		public void onClick(View v) {
			if (v.getId() == R.id.titlebar_refresh_btn) {
				if(Loading) return;
				FoodList.clear();
				mAdapter.notifyDataSetChanged();
				LoadPosition=0;
				HttpPost(url,params);
			}
		}
	}
	
	public void HttpPost(String url,String params){
		if(Loading) return;
		Loading = true;
		ListBottem.setVisibility(View.INVISIBLE);
		mLoadingProgress.setVisibility(View.VISIBLE);
		if (null!=params && !params.equals(""))
			url += "?" + params;
//		Log.e(TAG, "FinalURL: "+url);
		StringRequest stringRequest = new StringRequest(Request.Method.POST , url,
				new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
//				Log.e(TAG, "onResponse: "+response);
				Loading = false;
				mLoadingProgress.setVisibility(View.GONE);
				List<FoodModel> newList = MyJson.getFoodList(response);
				if (newList != null) {
					if (newList.size() == LoadSize) {
						ListBottem.setVisibility(View.VISIBLE);
						LoadPosition += LoadSize;
					} else 
						ListBottem.setVisibility(View.GONE); //加载完毕
					for (FoodModel info : newList) {
						FoodList.add(info);
					}
					mAdapter.notifyDataSetChanged();
					}
				}
			},new Response.ErrorListener() {
				@Override  
				public void onErrorResponse(VolleyError error) {
					Loading = false;
					mLoadingProgress.setVisibility(View.GONE);
					Log.e(TAG, "onErrorResponse: "+error.toString());
					error.printStackTrace();
					Toast.makeText(getActivity(),"连接失败，请稍后重试",Toast.LENGTH_SHORT).show();
					ListBottem.setVisibility(View.VISIBLE);
					}
				});
		stringRequest.setRetryPolicy(Model.RetryPolicy);
		FrameActivity.mRequestQueue.add(stringRequest);
	}
	
	private class MainListOnItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			Intent intent = new Intent(getActivity(), FoodDetailsActivity.class);
			Bundle bund = new Bundle();
			bund.putSerializable("FoodModel",FoodList.get(arg2));
			intent.putExtra("value",bund);
			startActivity(intent);
		}
	}

}
