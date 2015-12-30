package com.xyn.activity;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.Volley;
import com.xyn.ebook.R;
import com.xyn.fragment.Fragment2;
import com.xyn.fragment.Fragment1;
import com.xyn.fragment.Fragment3;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * �����ܽ���
 * */
public class FrameActivity extends FragmentActivity {
	private static String TAG = "FrameActivity";
	public static boolean isRunning = false; //������ת�����������
	private LinearLayout  mMyBottemTuanBtn, mMyBottemCheckinBtn, mMyBottemMoreBtn;
	private ImageView mMyBottemTuanImg,mMyBottemCheckinImg, mMyBottemMoreImg;
	private TextView mMyBottemTuanTxt, mMyBottemCheckinTxt, mMyBottemMoreTxt;
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
	    		new ImageCache() {//ͼƬ�ڴ滺�漼��
		private int maxSize = 20 * 1024 * 1024; //�����С20m
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
		//���Activity��Ϣ
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

	// ��ʼ���ؼ�
	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.FramePager);
		// ������linearlayoutΪ��ť���õĿؼ�
		mMyBottemTuanBtn = (LinearLayout) findViewById(R.id.MyBottemTuanBtn);
		mMyBottemCheckinBtn = (LinearLayout) findViewById(R.id.MyBottemCheckinBtn);
//		mMyBottemMyBtn = (LinearLayout) findViewById(R.id.MyBottemMyBtn);
		mMyBottemMoreBtn = (LinearLayout) findViewById(R.id.MyBottemMoreBtn);
		// ����linearlayout�е�imageview
		mMyBottemTuanImg = (ImageView) findViewById(R.id.MyBottemTuanImg);
		mMyBottemCheckinImg = (ImageView) findViewById(R.id.MyBottemCheckinImg);
//		mMyBottemMyImg = (ImageView) findViewById(R.id.MyBottemMyImg);
		mMyBottemMoreImg = (ImageView) findViewById(R.id.MyBottemMoreImg);
		// ����linearlayout�е�textview
		mMyBottemTuanTxt = (TextView) findViewById(R.id.MyBottemTuanTxt);
		mMyBottemCheckinTxt = (TextView) findViewById(R.id.MyBottemCheckinTxt);
//		mMyBottemMyTxt = (TextView) findViewById(R.id.MyBottemMyTxt);
		mMyBottemMoreTxt = (TextView) findViewById(R.id.MyBottemMoreTxt);
		MyBtnOnclick mytouchlistener = new MyBtnOnclick();
		mMyBottemTuanBtn.setOnClickListener(mytouchlistener);
		mMyBottemCheckinBtn.setOnClickListener(mytouchlistener);
//		mMyBottemMyBtn.setOnClickListener(mytouchlistener);
		mMyBottemMoreBtn.setOnClickListener(mytouchlistener);
		mMyBottemTuanImg.setImageResource(R.drawable.main_index_tuan_pressed);
		mMyBottemTuanTxt.setTextColor(Color.parseColor("#FF8C00"));
		createView();
		// дһ���ڲ���pageradapter
		pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public void destroyItem(ViewGroup container, int position,Object object) {}
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
		// ����viewpager�����л�����,����viewpager�л��ڼ��������Լ�������
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// �������ť��ʽ
				initBottemBtn();
//				// ���ն�Ӧ��view��tag���жϵ����л����ĸ����档
//				int flag = (Integer) list.get((position)).getTag();
					if (position == 0) {
					mMyBottemTuanImg.setImageResource(R.drawable.main_index_tuan_pressed);
					mMyBottemTuanTxt.setTextColor(Color.parseColor("#FF8C00"));
				} else if (position == 1) {
					mMyBottemCheckinImg.setImageResource(R.drawable.main_index_checkin_pressed);
					mMyBottemCheckinTxt.setTextColor(Color.parseColor("#FF8C00"));
				}
//else if (position == 2) {
//					mMyBottemMyImg.setImageResource(R.drawable.main_index_my_pressed);
//					mMyBottemMyTxt.setTextColor(Color.parseColor("#FF8C00"));
//				}
			else if (position == 2) {
					mMyBottemMoreImg.setImageResource(R.drawable.main_index_more_pressed);
					mMyBottemMoreTxt.setTextColor(Color.parseColor("#FF8C00"));
				}
			}
			@Override //����ҳ�滬���� arg0 ��ʾ��ǰ������view arg1 ��ʾ�����İٷֱ� arg2 ��ʾ�����ľ���
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			
			@Override //��������״̬ arg0 ��ʾ���ǵĻ���״̬ 0:Ĭ��״̬ 1:����״̬ 2:����ֹͣ
			public void onPageScrollStateChanged(int arg0) {}
		});
	}

	private void createView() {
		Fragment1 fragment1 = new Fragment1();
		Fragment2 fragment2 = new Fragment2();
//		MyFragment fragment3 = new MyFragment();
		Fragment3 fragment4 = new Fragment3();
		 list.add(fragment1);
		 list.add(fragment2);
//		 list.add(fragment3);
		 list.add(fragment4);
	}
	
	/**
	 * ��linearlayout��Ϊ��ť�ļ����¼�
	 * */
	private class MyBtnOnclick implements View.OnClickListener {
		@Override
		public void onClick(View arg0) {
			int mBtnid = arg0.getId();
			switch (mBtnid) {
			// //�������ǵ�viewpager��ת�Ǹ�����0������������ǵ�list���,�൱��list������±�
			case R.id.MyBottemTuanBtn:
				mViewPager.setCurrentItem(0,false);//not smoothScroll
				initBottemBtn();
				mMyBottemTuanImg.setImageResource(R.drawable.main_index_tuan_pressed);
				mMyBottemTuanTxt.setTextColor(Color.parseColor("#FF8C00"));
				break;
			case R.id.MyBottemCheckinBtn:
				mViewPager.setCurrentItem(1,false);
				initBottemBtn();
				mMyBottemCheckinImg.setImageResource(R.drawable.main_index_checkin_pressed);
				mMyBottemCheckinTxt.setTextColor(Color.parseColor("#FF8C00"));
				break;
//			case R.id.MyBottemMyBtn:
//				mViewPager.setCurrentItem(2,false);
//				initBottemBtn();
//				mMyBottemMyImg.setImageResource(R.drawable.main_index_my_pressed);
//				mMyBottemMyTxt.setTextColor(Color.parseColor("#FF8C00"));
//				break;
			case R.id.MyBottemMoreBtn:
				mViewPager.setCurrentItem(2,false);
				initBottemBtn();
				mMyBottemMoreImg.setImageResource(R.drawable.main_index_more_pressed);
				mMyBottemMoreTxt.setTextColor(Color.parseColor("#FF8C00"));
				break;
			}
		}
	}

	/**
	 * ��ʼ���ؼ�����ɫ
	 * */
	private void initBottemBtn() {
//		mMyBottemSearchImg.setImageResource(R.drawable.search_bottem_search);
		mMyBottemTuanImg.setImageResource(R.drawable.search_bottem_tuan);
		mMyBottemCheckinImg.setImageResource(R.drawable.search_bottem_checkin);
//		mMyBottemMyImg.setImageResource(R.drawable.search_bottem_my);
		mMyBottemMoreImg.setImageResource(R.drawable.search_bottem_more);
//		mMyBottemSearchTxt.setTextColor(getResources().getColor(
//				R.color.search_bottem_textcolor));
		mMyBottemTuanTxt.setTextColor(getResources().getColor(
				R.color.search_bottem_textcolor));
		mMyBottemCheckinTxt.setTextColor(getResources().getColor(
				R.color.search_bottem_textcolor));
//		mMyBottemMyTxt.setTextColor(getResources().getColor(
//				R.color.search_bottem_textcolor));
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
//	            Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�!", Toast.LENGTH_SHORT).show();                                
//	            exitTime = System.currentTimeMillis();   
//	        } else {
//	            finish(); //����Activity��onDestroy
////	            System.exit(0); //ֱ��ɱ������
//	        }
//	        return true;   
//	    }
//		return super.onKeyDown(keyCode, event);
//	}
}
