package com.xyn.activity;

import java.util.List;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.anjoyo.myview.MyScrollView;
import com.anjoyo.myview.MyScrollView.OnScrollListener;
import com.xyn.bean.FoodModel;
import com.xyn.source.Model;
import com.xyn.source.R;
import com.xyn.source.R.id;
import com.xyn.source.R.layout;
import com.xyn.utils.JsonUtil;
import com.xyn.utils.LoadImg;
import com.xyn.utils.LoadImg.ImageDownloadCallBack;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FoodDetailsActivity extends Activity implements OnScrollListener {

	private static String TAG = "FoodDetailsActivity";
	private FoodModel FoodModel;
	private ImageLoader mImageLoader = FrameActivity.mImageLoader;
	private ImageView mTuan_details_back, mTuan_details_share,
			mTuan_details_off, mTuan_details_img,food_score;
	private MyScrollView mTuan_details_scroll;
	private TextView mTuan_details_money1, mTuan_details_money2,
			mTuan_details_money3, mTuan_details_money4, mTuan_details_qianggou,
			mTuan_details_qianggou2,canteen_name,food_name,food_comment_count,
			food_desc;
	private RelativeLayout Tuan_details_tuangou;
	private boolean Loading = false;
	private String params;
	private String url = Model.HTTPURL+"getSpecifyCategoryNews";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_food_details);
		Bundle bund = getIntent().getBundleExtra("value");
		FoodModel = (FoodModel) bund.getSerializable("FoodModel");
		params ="c_id="+FoodModel.getc_id()+"&s_id"+FoodModel.gets_id()
				+"&u_id="+Model.u_id;
		initView();
	}

	private void initView() {
		mTuan_details_img = (ImageView) findViewById(R.id.Tuan_details_img);
		mTuan_details_back = (ImageView) findViewById(R.id.Tuan_details_back);
		mTuan_details_share = (ImageView) findViewById(R.id.Tuan_details_share);
		mTuan_details_off = (ImageView) findViewById(R.id.Tuan_details_off);
		Tuan_details_tuangou = (RelativeLayout) findViewById(R.id.Tuan_details_tuangou);
		food_score = (ImageView) findViewById(R.id.food_score);
		mTuan_details_money1 = (TextView) findViewById(R.id.Tuan_details_money1);
		mTuan_details_money2 = (TextView) findViewById(R.id.Tuan_details_money2);
		mTuan_details_money3 = (TextView) findViewById(R.id.Tuan_details_money3);
		mTuan_details_money4 = (TextView) findViewById(R.id.Tuan_details_money4);
		mTuan_details_qianggou = (TextView) findViewById(R.id.Tuan_details_qianggou);
		mTuan_details_qianggou2 = (TextView) findViewById(R.id.Tuan_details_qianggou2);
		mTuan_details_scroll = (MyScrollView) findViewById(R.id.Tuan_details_scroll);
		canteen_name = (TextView) findViewById(R.id.canteen_name);
		food_name = (TextView) findViewById(R.id.food_name);
		food_comment_count = (TextView) findViewById(R.id.food_comment_count);
		food_desc = (TextView) findViewById(R.id.food_desc);
		MyOnClickListener myonclick = new MyOnClickListener();
		mTuan_details_img.setOnClickListener(myonclick);
		mTuan_details_back.setOnClickListener(myonclick);
		mTuan_details_share.setOnClickListener(myonclick);
		mTuan_details_off.setOnClickListener(myonclick);
		mTuan_details_money1.setText("¥"+FoodModel.getf_price());
		mTuan_details_money2.setText("价值¥"+FoodModel.getf_price2());
		mTuan_details_money3.setText("¥"+FoodModel.getf_price());
		mTuan_details_money4.setText("价值¥"+FoodModel.getf_price2());
		mTuan_details_qianggou.setOnClickListener(myonclick);
		mTuan_details_qianggou2.setOnClickListener(myonclick);
		mTuan_details_scroll.setOnScrollListener(this);
		canteen_name.setText("饭堂："+FoodModel.getc_name());
		food_name.setText("美食名字："+FoodModel.getf_name());
		food_comment_count.setText("评价数："+FoodModel.getf_commentcount());
		food_desc.setText("  "+FoodModel.getf_desc());
		int slevel = Integer.valueOf(FoodModel.getf_score());
		switch (slevel) {
		case 0:
			food_score.setImageResource(R.drawable.star0);
			break;
		case 1:
			food_score.setImageResource(R.drawable.star1);
			break;
		case 2:
			food_score.setImageResource(R.drawable.star2);
			break;
		case 3:
			food_score.setImageResource(R.drawable.star3);
			break;
		case 4:
			food_score.setImageResource(R.drawable.star4);
			break;
		case 5:
			food_score.setImageResource(R.drawable.star5);
			break;
		}
		ImageListener listener = ImageLoader.getImageListener(mTuan_details_img,
				R.id.Tuan_details_img, R.id.Tuan_details_img);//加载中和失败的图片
		mImageLoader.get(Model.FOODLISTIMGURL+FoodModel.getImgSrc(), listener,320,240);//URL和图片限制
	}
	
	public void HttpPost(String url,String params){
		if(Loading) return;
		Loading = true;
		if (params!=null)
			url += "?" + params;
//		Log.e(TAG, "FinalURL: "+url);
		StringRequest stringRequest = new StringRequest(Request.Method.POST , url,
				new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
//				Log.e(TAG, "onResponse: "+response);
				Loading = false;
				List<FoodModel> newList = JsonUtil.getFoodList(response);
//					mAdapter.notifyDataSetChanged();
				}
			},new Response.ErrorListener() {
				@Override  
				public void onErrorResponse(VolleyError error) {
					Loading = false;
					Log.e(TAG, "onErrorResponse: "+error.toString());
					error.printStackTrace();
					Toast.makeText(FoodDetailsActivity.this,"连接失败，请稍后重试",Toast.LENGTH_SHORT).show();
					}
				});
		stringRequest.setRetryPolicy(Model.RetryPolicy);
		FrameActivity.mRequestQueue.add(stringRequest);
	}

	private class MyOnClickListener implements View.OnClickListener {
		public void onClick(View v) {
			int mID = v.getId();
			switch (mID) {
			case R.id.Tuan_details_back:
				finish();
				break;
			case R.id.Tuan_details_qianggou:
				showFoodCommentActivity();
				break;
			case R.id.Tuan_details_qianggou2:
				showFoodCommentActivity();
				break;
			}
		}
	}
	
	private void showFoodCommentActivity(){
		Intent intent = new Intent(this, FoodCommentActivity.class);
		Bundle bund = new Bundle();
		bund.putSerializable("FoodModel",FoodModel);
		intent.putExtra("value",bund);
		startActivity(intent);
	}

	@Override
	public void onScroll(int scrollY) {
		if (scrollY >= mTuan_details_img.getHeight()) {
			Tuan_details_tuangou.setVisibility(View.VISIBLE);
		} else {
			Tuan_details_tuangou.setVisibility(View.GONE);
		}
	}

}
