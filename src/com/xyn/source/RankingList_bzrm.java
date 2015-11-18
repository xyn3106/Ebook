package com.xyn.source;

import java.util.ArrayList;
import java.util.List;

import com.anjoyo.adapter.FoodAdapter;
import com.anjoyo.net.MyGet;
import com.anjoyo.net.ThreadPoolUtils;
import com.anjoyo.thread.HttpGetThread;
import com.xyn.bean.FoodModel;
import com.xyn.utils.MyJson;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * ���а�-��������ģ��
 * */

public class RankingList_bzrm extends Activity {

	private ListView mListView;
	private MyGet myGet = new MyGet();
	private MyJson myJson = new MyJson();
	private List<FoodModel> list = new ArrayList<FoodModel>();
	private FoodAdapter mAdapter = null;
	private Button ListBottem = null;
	private int mStart = 1;
	private int mEnd = 5;
	private String url = null;
	private boolean flag = true;
	private boolean listBottemFlag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_paihangbang_bzrm);
		initView();
	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.ShopListView);
		mAdapter = new FoodAdapter(list, RankingList_bzrm.this);
		ListBottem = new Button(RankingList_bzrm.this);
		ListBottem.setText("������ظ���");
		ListBottem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (flag && listBottemFlag) {
					url = Model.SHOPURL + "start=" + mStart + "&end=" + mEnd;
					ThreadPoolUtils.execute(new HttpGetThread(hand, url));
					listBottemFlag = false;
				} else if (!listBottemFlag)
					Toast.makeText(RankingList_bzrm.this, "���������Ժ�", 1).show();
			}
		});
		mListView.addFooterView(ListBottem, null, false);
		ListBottem.setVisibility(View.GONE);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new MainListOnItemClickListener());
		// ƴ���ַ�������
		url = Model.SHOPURL + "start=" + mStart + "&end=" + mEnd;
		ThreadPoolUtils.execute(new HttpGetThread(hand, url));
	}

	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(RankingList_bzrm.this, "�Ҳ�����ַ", 1).show();
				listBottemFlag = true;
			} else if (msg.what == 100) {
				Toast.makeText(RankingList_bzrm.this, "����ʧ��", 1).show();
				listBottemFlag = true;
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				// ��activity���л�ȡ���罻��������
				if (result != null) {
					// 1���������󷵻ص�����
					List<FoodModel> newList = MyJson.getFoodList(result);
					if (newList != null) {
						if (newList.size() == 5) {
							ListBottem.setVisibility(View.VISIBLE);
							mStart += 5;
							mEnd += 5;
						} else {
							ListBottem.setVisibility(View.GONE);
						}
						for (FoodModel info : newList) {
							list.add(info);
						}
						mAdapter.notifyDataSetChanged();
						listBottemFlag = true;
						mAdapter.notifyDataSetChanged();
					}
				}
				mAdapter.notifyDataSetChanged();
			}
		};
	};

	private class MainListOnItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
//			Intent intent = new Intent(RankingList_bzrm.this,
//					ShopDetailsActivity.class);
//			Bundle bund = new Bundle();
//			bund.putSerializable("ShopInfo", list.get(arg2));
//			intent.putExtra("value", bund);
//			startActivity(intent);
		}
	}
}
