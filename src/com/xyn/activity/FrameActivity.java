package com.xyn.activity;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.xyn.fragment.BubbleFragment;
import com.xyn.fragment.MoreFragment;
import com.xyn.fragment.MyFragment;
import com.xyn.fragment.FoodFragment;
import com.xyn.source.R;
import com.xyn.source.R.color;
import com.xyn.source.R.drawable;
import com.xyn.source.R.id;
import com.xyn.source.R.layout;

import android.app.ActivityGroup;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 软件框架界面
 * */
public class FrameActivity extends FragmentActivity {
	private static String TAG = "FrameActivity";
	public static boolean isRunning = false; //用来跳转程序载入界面
	private LinearLayout  mMyBottemTuanBtn, mMyBottemCheckinBtn, mMyBottemMyBtn, mMyBottemMoreBtn;
	private ImageView mMyBottemTuanImg,mMyBottemCheckinImg, mMyBottemMyImg, mMyBottemMoreImg;
	private TextView mMyBottemTuanTxt, mMyBottemCheckinTxt, mMyBottemMyTxt, mMyBottemMoreTxt;
	private List<Fragment> list = new ArrayList<Fragment>();
	private android.support.v4.view.ViewPager mViewPager;
	private FragmentPagerAdapter pagerAdapter;
	public static RequestQueue mRequestQueue;
	public static ImageLoader mImageLoader;
//	private long exitTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_frame);
		mRequestQueue = Volley.newRequestQueue(this);
		initView();
		isRunning=true;
		mImageLoader = new ImageLoader(mRequestQueue,
	    		new ImageCache() {//图片内存缓存技术
		private int maxSize = 20 * 1024 * 1024; //缓存大小20m
		private LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(maxSize) {
	            @Override
	            protected int sizeOf(String key, Bitmap bitmap) {
	                return bitmap.getRowBytes() * bitmap.getHeight();    
	            }
	        };
	    @Override
	    public Bitmap getBitmap(String url) {
	        return mCache.get(url);
	    }
	    @Override    
	    public void putBitmap(String url, Bitmap bitmap) {
	        mCache.put(url, bitmap);
	    }
	    });
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		//输出Activity信息
		ActivityManager mActivityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE) ;
		@SuppressWarnings("deprecation")
		List<ActivityManager.RunningTaskInfo> appList3 = mActivityManager.getRunningTasks(1000);  
      for (ActivityManager.RunningTaskInfo runningTaskInfo : appList3) {
      	Log.e(TAG,"root:"+runningTaskInfo.baseActivity.getClassName());
      	Log.e(TAG,"totalnum:"+runningTaskInfo.numActivities);
      	Log.e(TAG,"runningnum:"+runningTaskInfo.numRunning);
      	}
	}
	
	@Override
	protected void onDestroy() {
		isRunning=false;
		super.onDestroy();
	}

	// 初始化控件
	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.FramePager);
		// 查找以linearlayout为按钮作用的控件
		mMyBottemTuanBtn = (LinearLayout) findViewById(R.id.MyBottemTuanBtn);
		mMyBottemCheckinBtn = (LinearLayout) findViewById(R.id.MyBottemCheckinBtn);
		mMyBottemMyBtn = (LinearLayout) findViewById(R.id.MyBottemMyBtn);
		mMyBottemMoreBtn = (LinearLayout) findViewById(R.id.MyBottemMoreBtn);
		// 查找linearlayout中的imageview
		mMyBottemTuanImg = (ImageView) findViewById(R.id.MyBottemTuanImg);
		mMyBottemCheckinImg = (ImageView) findViewById(R.id.MyBottemCheckinImg);
		mMyBottemMyImg = (ImageView) findViewById(R.id.MyBottemMyImg);
		mMyBottemMoreImg = (ImageView) findViewById(R.id.MyBottemMoreImg);
		// 查找linearlayout中的textview
		mMyBottemTuanTxt = (TextView) findViewById(R.id.MyBottemTuanTxt);
		mMyBottemCheckinTxt = (TextView) findViewById(R.id.MyBottemCheckinTxt);
		mMyBottemMyTxt = (TextView) findViewById(R.id.MyBottemMyTxt);
		mMyBottemMoreTxt = (TextView) findViewById(R.id.MyBottemMoreTxt);
		MyBtnOnclick mytouchlistener = new MyBtnOnclick();
		mMyBottemTuanBtn.setOnClickListener(mytouchlistener);
		mMyBottemCheckinBtn.setOnClickListener(mytouchlistener);
		mMyBottemMyBtn.setOnClickListener(mytouchlistener);
		mMyBottemMoreBtn.setOnClickListener(mytouchlistener);
		mMyBottemTuanImg.setImageResource(R.drawable.main_index_tuan_pressed);
		mMyBottemTuanTxt.setTextColor(Color.parseColor("#FF8C00"));
		createView();
		// 写一个内部类pageradapter
		pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return list.size();
			}
			@Override
			public Fragment getItem(int arg0) {
				return list.get(arg0);
			}
		};
		mViewPager.setAdapter(pagerAdapter);
		// 设置viewpager界面切换监听,监听viewpager切换第几个界面以及滑动的
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// 先清除按钮样式
				initBottemBtn();
//				// 按照对应的view的tag来判断到底切换到哪个界面。
//				int flag = (Integer) list.get((position)).getTag();
					if (position == 0) {
					mMyBottemTuanImg
							.setImageResource(R.drawable.main_index_tuan_pressed);
					mMyBottemTuanTxt.setTextColor(Color.parseColor("#FF8C00"));
				} else if (position == 1) {
					mMyBottemCheckinImg
							.setImageResource(R.drawable.main_index_checkin_pressed);
					mMyBottemCheckinTxt.setTextColor(Color
							.parseColor("#FF8C00"));
				} else if (position == 2) {
					mMyBottemMyImg
							.setImageResource(R.drawable.main_index_my_pressed);
					mMyBottemMyTxt.setTextColor(Color.parseColor("#FF8C00"));
				} else if (position == 3) {
					mMyBottemMoreImg
							.setImageResource(R.drawable.main_index_more_pressed);
					mMyBottemMoreTxt.setTextColor(Color.parseColor("#FF8C00"));
				}
			}
			@Override //监听页面滑动的 arg0 表示当前滑动的view arg1 表示滑动的百分比 arg2 表示滑动的距离
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			
			@Override //监听滑动状态 arg0 表示我们的滑动状态 0:默认状态 1:滑动状态 2:滑动停止
			public void onPageScrollStateChanged(int arg0) {}
		});
	}

	private void createView() {
		FoodFragment fragment1 = new FoodFragment();
		BubbleFragment fragment2 = new BubbleFragment();
		MyFragment fragment3 = new MyFragment();
		MoreFragment fragment4 = new MoreFragment();
		 list.add(fragment1);
		 list.add(fragment2);
		 list.add(fragment3);
		 list.add(fragment4);
	}
	
	/**
	 * 用linearlayout作为按钮的监听事件
	 * */
	private class MyBtnOnclick implements View.OnClickListener {
		@Override
		public void onClick(View arg0) {
			int mBtnid = arg0.getId();
			switch (mBtnid) {
			// //设置我们的viewpager跳转那个界面0这个参数和我们的list相关,相当于list里面的下标
			case R.id.MyBottemTuanBtn:
				mViewPager.setCurrentItem(0);
				initBottemBtn();
				mMyBottemTuanImg
						.setImageResource(R.drawable.main_index_tuan_pressed);
				mMyBottemTuanTxt.setTextColor(Color.parseColor("#FF8C00"));
				break;
			case R.id.MyBottemCheckinBtn:
				mViewPager.setCurrentItem(1);
				initBottemBtn();
				mMyBottemCheckinImg
						.setImageResource(R.drawable.main_index_checkin_pressed);
				mMyBottemCheckinTxt.setTextColor(Color.parseColor("#FF8C00"));
				break;
			case R.id.MyBottemMyBtn:
				mViewPager.setCurrentItem(2);
				initBottemBtn();
				mMyBottemMyImg
						.setImageResource(R.drawable.main_index_my_pressed);
				mMyBottemMyTxt.setTextColor(Color.parseColor("#FF8C00"));
				break;
			case R.id.MyBottemMoreBtn:
				mViewPager.setCurrentItem(3);
				initBottemBtn();
				mMyBottemMoreImg
						.setImageResource(R.drawable.main_index_more_pressed);
				mMyBottemMoreTxt.setTextColor(Color.parseColor("#FF8C00"));
				break;
			}
		}
	}

	/**
	 * 初始化控件的颜色
	 * */
	private void initBottemBtn() {
//		mMyBottemSearchImg.setImageResource(R.drawable.search_bottem_search);
		mMyBottemTuanImg.setImageResource(R.drawable.search_bottem_tuan);
		mMyBottemCheckinImg.setImageResource(R.drawable.search_bottem_checkin);
		mMyBottemMyImg.setImageResource(R.drawable.search_bottem_my);
		mMyBottemMoreImg.setImageResource(R.drawable.search_bottem_more);
//		mMyBottemSearchTxt.setTextColor(getResources().getColor(
//				R.color.search_bottem_textcolor));
		mMyBottemTuanTxt.setTextColor(getResources().getColor(
				R.color.search_bottem_textcolor));
		mMyBottemCheckinTxt.setTextColor(getResources().getColor(
				R.color.search_bottem_textcolor));
		mMyBottemMyTxt.setTextColor(getResources().getColor(
				R.color.search_bottem_textcolor));
		mMyBottemMoreTxt.setTextColor(getResources().getColor(
				R.color.search_bottem_textcolor));
	}

	@Override
    public void onBackPressed() {
    	Intent intent = new Intent(Intent.ACTION_MAIN);
    	intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
	}
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
//	        if((System.currentTimeMillis()-exitTime) > 2000){
//	            Toast.makeText(getApplicationContext(), "再按一次退出!", Toast.LENGTH_SHORT).show();                                
//	            exitTime = System.currentTimeMillis();   
//	        } else {
//	            finish(); //调用Activity的onDestroy
////	            System.exit(0); //直接杀死进程
//	        }
//	        return true;   
//	    }
//		return super.onKeyDown(keyCode, event);
//	}
}
