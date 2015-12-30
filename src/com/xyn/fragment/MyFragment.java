package com.xyn.fragment;

import com.xyn.ebook.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyFragment extends Fragment {

	private TextView mMy_login, mMy_address, mMy_checkin,
			mMy_comment, mMy_photo;
	// 信封
	private LinearLayout mMy_messagebtn;
	// listview类型的linearlayout按钮
	private LinearLayout mMy_list_tuangou, mMy_list_huiyuanka, mMy_list_yuding,
			mMy_list_menpiao, mMy_list_caogao,
			mMy_list_shanghushoucang, mMy_list_tuangoushoucang,
			mMy_list_guanzhu, mMy_list_fensi;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_my, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView(view);
	}

	private void initView(View view) {
		mMy_login = (TextView) view.findViewById(R.id.My_login);
		mMy_address = (TextView) view.findViewById(R.id.My_address);
		mMy_checkin = (TextView) view.findViewById(R.id.My_checkin);
		mMy_comment = (TextView) view.findViewById(R.id.My_comment);
		mMy_photo = (TextView) view.findViewById(R.id.My_photo);
		mMy_messagebtn = (LinearLayout) view.findViewById(R.id.My_messagebtn);
		mMy_list_tuangou = (LinearLayout) view.findViewById(R.id.My_list_tuangou);
		mMy_list_huiyuanka = (LinearLayout) view.findViewById(R.id.My_list_huiyuanka);
		mMy_list_yuding = (LinearLayout) view.findViewById(R.id.My_list_yuding);
		mMy_list_menpiao = (LinearLayout) view.findViewById(R.id.My_list_menpiao);
		mMy_list_caogao = (LinearLayout) view.findViewById(R.id.My_list_caogao);
		mMy_list_shanghushoucang = (LinearLayout) view.findViewById(R.id.My_list_shanghuhushoucang);
		mMy_list_tuangoushoucang = (LinearLayout) view.findViewById(R.id.My_list_tuangoushoucang);
		mMy_list_guanzhu = (LinearLayout) view.findViewById(R.id.My_list_guanzhu);
		mMy_list_fensi = (LinearLayout) view.findViewById(R.id.My_list_fensi);
		MyOnclickListener mOnclickListener = new MyOnclickListener();
		mMy_login.setOnClickListener(mOnclickListener);
		mMy_address.setOnClickListener(mOnclickListener);
		mMy_checkin.setOnClickListener(mOnclickListener);
		mMy_comment.setOnClickListener(mOnclickListener);
		mMy_photo.setOnClickListener(mOnclickListener);
		mMy_messagebtn.setOnClickListener(mOnclickListener);
		mMy_list_tuangou.setOnClickListener(mOnclickListener);
		mMy_list_huiyuanka.setOnClickListener(mOnclickListener);
		mMy_list_yuding.setOnClickListener(mOnclickListener);
		mMy_list_menpiao.setOnClickListener(mOnclickListener);
		mMy_list_caogao.setOnClickListener(mOnclickListener);
		mMy_list_shanghushoucang.setOnClickListener(mOnclickListener);
		mMy_list_tuangoushoucang.setOnClickListener(mOnclickListener);
		mMy_list_guanzhu.setOnClickListener(mOnclickListener);
		mMy_list_fensi.setOnClickListener(mOnclickListener);
	}

	private class MyOnclickListener implements View.OnClickListener {
		public void onClick(View v) {
			int mID = v.getId();
			switch (mID) {
			case R.id.My_login:
//				Intent intent2 = new Intent(getActivity(),LoginActivity.class);
//				startActivity(intent2);
				break;
			}
		}

	}
}
