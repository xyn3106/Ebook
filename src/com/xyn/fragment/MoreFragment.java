package com.xyn.fragment;

import com.xyn.activity.BubblePostActivity;
import com.xyn.activity.FoodPostActivity;
import com.xyn.source.R;
import com.xyn.source.R.id;
import com.xyn.source.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MoreFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_more, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView(view);
	}

	private void initView(View view) {
		ImageView More_settings_btn = (ImageView) view.findViewById(R.id.More_settings_btn);
		LinearLayout mMore_title_btn1 = (LinearLayout) view.findViewById(R.id.More_title_btn1);
		LinearLayout more_post_food = (LinearLayout) view.findViewById(R.id.more_post_food);
		LinearLayout mMore_list_saoyisao = (LinearLayout) view.findViewById(R.id.More_list_saoyisao);
		LinearLayout mMore_list_lishi = (LinearLayout) view.findViewById(R.id.More_list_lishi);
		LinearLayout mMore_list_suishoupai = (LinearLayout) view.findViewById(R.id.More_list_suishoupai);
		LinearLayout mMore_list_yiguanbi = (LinearLayout) view.findViewById(R.id.More_list_yiguanbi);
		LinearLayout mMore_list_gengxin = (LinearLayout) view.findViewById(R.id.More_list_gengxin);
		LinearLayout mMore_list_gaosupengyou = (LinearLayout) view.findViewById(R.id.More_list_gaosupengyou);
		LinearLayout mMore_list_bangzhu = (LinearLayout) view.findViewById(R.id.More_list_bangzhu);
		LinearLayout mMore_list_women = (LinearLayout) view.findViewById(R.id.More_list_women);
		LinearLayout mMore_list_dianhua = (LinearLayout) view.findViewById(R.id.More_list_dianhua);
		LinearLayout mMore_list_exit = (LinearLayout) view.findViewById(R.id.More_list_exit);
		ImageView mMore_list_moreapp = (ImageView) view.findViewById(R.id.More_list_moreapp);
		MyOnclickListener mOnclickListener = new MyOnclickListener();
//		mMore_button1.setOnClickListener(mOnclickListener);
		mMore_title_btn1.setOnClickListener(mOnclickListener);
		more_post_food.setOnClickListener(mOnclickListener);
		mMore_list_saoyisao.setOnClickListener(mOnclickListener);
		mMore_list_lishi.setOnClickListener(mOnclickListener);
		mMore_list_suishoupai.setOnClickListener(mOnclickListener);
		mMore_list_yiguanbi.setOnClickListener(mOnclickListener);
		mMore_list_gengxin.setOnClickListener(mOnclickListener);
		mMore_list_gaosupengyou.setOnClickListener(mOnclickListener);
		mMore_list_bangzhu.setOnClickListener(mOnclickListener);
		mMore_list_women.setOnClickListener(mOnclickListener);
		mMore_list_dianhua.setOnClickListener(mOnclickListener);
		mMore_list_exit.setOnClickListener(mOnclickListener);
		mMore_list_moreapp.setOnClickListener(mOnclickListener);
	}

	private class MyOnclickListener implements View.OnClickListener {
		public void onClick(View v) {
			int ID = v.getId();
			if(ID == R.id.More_list_exit)
				getActivity().finish();
			else if(ID == R.id.more_post_food){
				Intent intent = new Intent(getActivity(), FoodPostActivity.class);
				startActivity(intent);
			}
		}
	}
}
