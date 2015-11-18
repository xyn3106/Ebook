package com.xyn.activity;

import com.xyn.bean.FoodModel;
import com.xyn.source.R;
import com.xyn.source.R.id;
import com.xyn.source.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 店铺详情-点评模块
 * */
public class FoodCommentActivity extends Activity {

	private FoodModel FoodModel;
	private TextView mShop_details_more_title;
	private ImageView mShoplist_back;
	private RatingBar mshop_dianping_ratingbar;
	private EditText mshop_dianping_edittext1, mshop_dianping_edittext2;
	private TextView mshop_dianping_text1, mshop_dianping_OK;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_food_comment);
		Intent intent = getIntent();
		Bundle bund = intent.getBundleExtra("value");
		FoodModel = (FoodModel) bund.getSerializable("FoodModel");
		initView();
		initModel();
	}

	private void initView() {
		mShop_details_more_title = (TextView) findViewById(R.id.Shop_details_more_title);
		mShoplist_back = (ImageView) findViewById(R.id.Shoplist_back);
		mShoplist_back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		mshop_dianping_ratingbar = (RatingBar) findViewById(R.id.shop_dianping_ratingbar);
		mshop_dianping_edittext1 = (EditText) findViewById(R.id.shop_dianping_edittext1);
		mshop_dianping_edittext2 = (EditText) findViewById(R.id.shop_dianping_edittext2);
		mshop_dianping_text1 = (TextView) findViewById(R.id.shop_dianping_text1);
		mshop_dianping_OK = (TextView) findViewById(R.id.shop_dianping_OK);
		mshop_dianping_OK.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if(mshop_dianping_ratingbar.getRating() == 0)
					Toast.makeText(FoodCommentActivity.this, "亲，请评分~",Toast.LENGTH_SHORT).show();
				else{
					Toast.makeText(FoodCommentActivity.this, "感谢亲的评价~",Toast.LENGTH_SHORT).show();
					finish();
					}
			}
		});
		mshop_dianping_ratingbar.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					event.setAction(MotionEvent.ACTION_MOVE);
				}
				return false;
			}
		});
	}

	private void initModel() {
		mShop_details_more_title.setText(FoodModel.getf_name());
	}
}
