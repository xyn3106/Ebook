package com.anjoyo.myfragment;

import com.xyn.ebook.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("ValidFragment")
public class MyFragmentone extends Fragment {

	private Context ctx;// 从activity当中得到的上下文

	@SuppressLint("ValidFragment")
	public MyFragmentone(Context ctx) {
		super();
		this.ctx = ctx;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// fragment创建view时使用
		// 返回的是一个view
		/**
		 * LayoutInflater inflater 找我们的fragmentxml时实用的 ViewGroup
		 * 使用inflater时宽高相对条件 bundler 可以通过我们的bundle在fragment创建view时传递参数
		 * */
		View view = null;
		view = View.inflate(ctx, R.layout.fragment_my01, null);
		return view;
	}
}
