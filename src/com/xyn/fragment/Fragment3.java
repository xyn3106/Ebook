package com.xyn.fragment;

import com.xyn.activity.AboutActivity;
import com.xyn.activity.ListViewActivity;
import com.xyn.ebook.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Fragment3 extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_3, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView(view);
	}

	private void initView(View view) {
//		LinearLayout mMore_list_bangzhu = (LinearLayout) view.findViewById(R.id.More_list_bangzhu);
		LinearLayout mMore_list_women = (LinearLayout) view.findViewById(R.id.More_list_women);
		LinearLayout mMore_list_exit = (LinearLayout) view.findViewById(R.id.More_list_exit);
		MyOnclickListener mOnclickListener = new MyOnclickListener();
//		mMore_list_bangzhu.setOnClickListener(mOnclickListener);
		mMore_list_women.setOnClickListener(mOnclickListener);
		mMore_list_exit.setOnClickListener(mOnclickListener);
	}

	private class MyOnclickListener implements View.OnClickListener {
		public void onClick(View v) {
			int ID = v.getId();
			if(ID == R.id.More_list_exit)
				getActivity().finish();
			else if(ID == R.id.More_list_women){
				Intent intent3 = new Intent(getActivity(), AboutActivity.class);
				startActivity(intent3);
			}
		}
	}
}
