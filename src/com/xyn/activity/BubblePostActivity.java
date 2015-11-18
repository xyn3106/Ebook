package com.xyn.activity;

import java.util.ArrayList;
import java.util.List;

import com.anjoyo.adapter.FaceGridAdapter;
import com.xyn.source.Model;
import com.xyn.source.R;
import com.xyn.utils.SmileyParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
public class BubblePostActivity extends Activity {

//	private FoodModel info = null;
	private TextView mShop_details_more_title;
	private ImageView mShoplist_back;
	private EditText mshop_qiandao_edittext1;
	private ImageView mshop_qiandao_biaoqing, mshop_qiandao_addimage;
	private TextView mshop_qiandao_text, mshop_qiandao_OK;

	private List<GridView> list = new ArrayList<GridView>();
	private String[] mFaceValue;
	final String arrText1[] = new String[20];
	final String arrText2[] = new String[20];
	final String arrText3[] = new String[20];
	final String arrText4[] = new String[16];
	private ViewPager mViewPager;
	private boolean openFaceFlag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bubble_post);
//		Intent intent = getIntent();
//		Bundle bund = intent.getBundleExtra("value");
//		info = (FoodModel) bund.getSerializable("ShopInfo");
		mFaceValue = this.getResources().getStringArray(
				R.array.default_smiley_texts);
		createView();
		initView();
		initModel();
	}

	private void initView() {
		mShop_details_more_title = (TextView) findViewById(R.id.Shop_details_more_title);
		mShoplist_back = (ImageView) findViewById(R.id.Shoplist_back);
		mViewPager = (ViewPager) findViewById(R.id.FaceViewGroup);
		mViewPager.setAdapter(new MyPageAdapter());
		mshop_qiandao_edittext1 = (EditText) findViewById(R.id.shop_qiandao_edittext1);
		mshop_qiandao_biaoqing = (ImageView) findViewById(R.id.shop_qiandao_biaoqing);
		mshop_qiandao_addimage = (ImageView) findViewById(R.id.shop_qiandao_addimage);
		mshop_qiandao_text = (TextView) findViewById(R.id.shop_qiandao_text);
		mshop_qiandao_OK = (TextView) findViewById(R.id.shop_qiandao_OK);
		MyOnClickListener myonclick = new MyOnClickListener();
		mShoplist_back.setOnClickListener(myonclick);
		mshop_qiandao_biaoqing.setOnClickListener(myonclick);
		mshop_qiandao_addimage.setOnClickListener(myonclick);
		mshop_qiandao_OK.setOnClickListener(myonclick);
	}
	
	@Override
	public void onBackPressed(){
		if (openFaceFlag) {
			mViewPager.setVisibility(View.GONE);
			openFaceFlag = !openFaceFlag;
		} else 
			showExitDialog();
	}
	
	private void showExitDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("确定要返回吗？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				BubblePostActivity.super.onBackPressed(); //调用父类方法
			}
		});
		builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {}
			});
		builder.create().show();
	}

	private class MyOnClickListener implements View.OnClickListener {
		public void onClick(View v) {
			int mID = v.getId();
			if (mID == R.id.shop_qiandao_OK) {
				addSign();
			}
			else if (mID == R.id.shop_qiandao_biaoqing) {
				if (openFaceFlag) {
					mViewPager.setVisibility(View.GONE);
				} else {
					mViewPager.setVisibility(View.VISIBLE);
				}
				openFaceFlag = !openFaceFlag;
			}
			else if(mID == R.id.Shoplist_back){
				showExitDialog();
			}
			else if(mID == R.id.shop_qiandao_addimage){
			}
		}
	}

	private void initModel() {
//		mShop_details_more_title.setText(info.getf_name());
		for (int i = 0; i < 20; i++) {
			arrText1[i] = mFaceValue[i];
		}
		for (int i = 20; i < 40; i++) {
			arrText2[i - 20] = mFaceValue[i];
		}
		for (int i = 40; i < 60; i++) {
			arrText3[i - 40] = mFaceValue[i];
		}
		for (int i = 60; i < 76; i++) {
			arrText4[i - 60] = mFaceValue[i];
		}
	}

	// 我的签到方法
	private void addSign() {
//		int sid = info.getfid();
//		String pid = "1";
//		String signcontent = mshop_qiandao_edittext1.getText().toString()
//				.trim();
//		String signlevel = "" + mshop_qiandao_ratingbar.getNumStars();
//		String signimage = "";
//		String signtime = ""
//				+ (new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date()));
//		String json = "{\"sid\":\"" + sid + "\"," + "\"pid\":\"" + pid + "\","
//				+ "\"signcontent\":\"" + signcontent + "\","
//				+ "\"signlevel\":\"" + signlevel + "\"," + "\"signimage\":\""
//				+ signimage + "\"," + "\"signtime\":\"" + signtime + "\"}";
//		ThreadPoolUtils.execute(new HttpPostThread(hand, Model.SIGNURL, "1",
//				json));
	}

	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 200) {
				Toast.makeText(BubblePostActivity.this, "成功",Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	};

	// 创建viewpager当中的view
	private void createView() {
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		GridView mGrid1 = new GridView(BubblePostActivity.this);
		mGrid1.setLayoutParams(lp);
		mGrid1.setNumColumns(7);
		mGrid1.setAdapter(new FaceGridAdapter(BubblePostActivity.this,Model.image1));
		list.add(mGrid1);
		GridView mGrid2 = new GridView(BubblePostActivity.this);
		mGrid2.setLayoutParams(lp);
		mGrid2.setNumColumns(7);
		mGrid2.setAdapter(new FaceGridAdapter(BubblePostActivity.this,Model.image2));
		list.add(mGrid2);
		GridView mGrid3 = new GridView(BubblePostActivity.this);
		mGrid3.setLayoutParams(lp);
		mGrid3.setNumColumns(7);
		mGrid3.setAdapter(new FaceGridAdapter(BubblePostActivity.this,Model.image3));
		list.add(mGrid3);
		GridView mGrid4 = new GridView(BubblePostActivity.this);
		mGrid4.setLayoutParams(lp);
		mGrid4.setNumColumns(7);
		mGrid4.setAdapter(new FaceGridAdapter(BubblePostActivity.this,Model.image4));
		list.add(mGrid4);

		SmileyParser.init(BubblePostActivity.this);
		final SmileyParser parser = SmileyParser.getInstance();
		mGrid1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == 20) {
					if(mshop_qiandao_edittext1.getText().toString().endsWith("]"))
					mshop_qiandao_edittext1.setText(mshop_qiandao_edittext1.getText()
							.subSequence(0,mshop_qiandao_edittext1.getText().length() - 4));
					else if(mshop_qiandao_edittext1.getText().length() != 0 )
						mshop_qiandao_edittext1.setText(mshop_qiandao_edittext1.getText()
								.subSequence( 0, mshop_qiandao_edittext1.getText().length()-1));
				} else {
					mshop_qiandao_edittext1.setText(mshop_qiandao_edittext1.getText()
							.append(parser.addSmileySpans(arrText1[arg2])));
				}
				mshop_qiandao_edittext1.setSelection(mshop_qiandao_edittext1.getText().length());
			}
		});
		mGrid2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				if (arg2 == 20) {
					if(mshop_qiandao_edittext1.getText().toString().endsWith("]"))
					mshop_qiandao_edittext1.setText(mshop_qiandao_edittext1.getText()
							.subSequence(0,mshop_qiandao_edittext1.getText().length() - 4));
					else if(mshop_qiandao_edittext1.getText().length() != 0 )
						mshop_qiandao_edittext1.setText(mshop_qiandao_edittext1.getText()
								.subSequence( 0, mshop_qiandao_edittext1.getText().length()-1));
				} else {
					mshop_qiandao_edittext1.setText(mshop_qiandao_edittext1
							.getText().append(parser.addSmileySpans(arrText2[arg2])));
				}
				mshop_qiandao_edittext1.setSelection(mshop_qiandao_edittext1.getText().length());
			}
		});
		mGrid3.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				if (arg2 == 20) {
					if(mshop_qiandao_edittext1.getText().toString().endsWith("]"))
					mshop_qiandao_edittext1.setText(mshop_qiandao_edittext1.getText()
							.subSequence(0,mshop_qiandao_edittext1.getText().length() - 4));
					else if(mshop_qiandao_edittext1.getText().length() != 0 )
						mshop_qiandao_edittext1.setText(mshop_qiandao_edittext1.getText()
								.subSequence( 0, mshop_qiandao_edittext1.getText().length()-1));
				} else {
					mshop_qiandao_edittext1.setText(mshop_qiandao_edittext1
							.getText().append(parser.addSmileySpans(arrText3[arg2])));
				}
				mshop_qiandao_edittext1.setSelection(mshop_qiandao_edittext1.getText().length());
			}
		});
		mGrid4.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				if (arg2 == 16) {
					if(mshop_qiandao_edittext1.getText().toString().endsWith("]"))
					mshop_qiandao_edittext1.setText(mshop_qiandao_edittext1.getText()
							.subSequence(0,mshop_qiandao_edittext1.getText().length() - 4));
					else if(mshop_qiandao_edittext1.getText().length() != 0 )
						mshop_qiandao_edittext1.setText(mshop_qiandao_edittext1.getText()
								.subSequence( 0, mshop_qiandao_edittext1.getText().length()-1));
				} else {
					mshop_qiandao_edittext1.setText(mshop_qiandao_edittext1
							.getText().append(parser.addSmileySpans(arrText4[arg2])));
				}
				mshop_qiandao_edittext1.setSelection(mshop_qiandao_edittext1.getText().length());
			}
		});
	}

	class MyPageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(list.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = list.get(position);
			container.addView(view);
			return view;
		}

	}

}
