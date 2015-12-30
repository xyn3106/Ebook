package com.xyn.activity;

import com.xyn.ebook.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListViewActivity extends Activity {

	private ListView mListView;
	private static String[] list1 = {"“哀鸿”是指“哀声”吗？","“晋见”还是“觐见”？","“盛名之下，其实难副”的误用","“师心自用”，岂能乱用！",
			"“唯唯诺诺”是一种怎样的应答？","“炙手可热”是否真的很抢手？","别让你的庆贺成了贬义","“一发不可收拾”是贬义成语","不是所有相同的事情都叫做“无独有偶”"
			,"我们“一起齐”结婚吧！","这类恶性事故岂能“举一反三”","“暴风骤雨”≠“狂风暴雨”","“好一个”与“怎一个”","何谓“良莠不齐”？"
			,"怎样用成语“品味”中国古代建筑？","正能量可不能被“上行下效”","“不绝如缕”，并非连续不断","“辉煌”能“戛然而止”吗？",
			"你在“干嘛吗”","如此“不可理喻”","要不用这个，要不用那个","用错了地方的“如沐春风”"};
	private static String[] list2 = {"第一卷：别自暴（抱）自弃，来度（渡）个假吧！","第二卷：他悬梁刺股的气概真是震撼人心！",
			"第三卷：挖墙脚还是挖墙角？","第四卷：黄梁（粱）美梦可不是用来吃的！","第五卷：墨（默）守成规和出其（奇）不意",
			"第六卷：懂得人情世（事）故，才能谈笑风生（声）","第七卷：“趋之若鹜”从鸟，好高骛远从马","第八卷：月子是用来坐的"};
	private static String[] list3 = {"名家写作，注重炼字“的”","让议论文更有感染力","思辨，让你的文章更出彩","学会对材料进行哲学分析",
			"不要让“最”“首次”和“第一”不负责任地泛滥","海峡两岸作文课程标准有何异同？","不要生造短语","巧用二字典故，提升文章层次",
			"善用意象，让作文更有意蕴","通识博观，现实情怀","议论文主体段落的“深刻技术”","语不惊人誓不休","域外母语写作教学是怎样的？"};
	private ArrayAdapter<String> mAdapter;
	private int Flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_listview);
		Flag = getIntent().getIntExtra("what", 1);
//		list = getAssets().list(""+Flag);
		String[] list = null;
		switch(Flag){
		case 1:list = list1;
			break;
		case 2:list = list2;
			break;
		case 3:list = list3;
			break;
		}
		mAdapter = new ArrayAdapter<String>(this, R.layout.list_item, list);
		initView();
	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.listview);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new MainListOnItemClickListener());
	}

	private class MainListOnItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			Intent intent = new Intent(ListViewActivity.this,WebViewActivity.class);
			intent.putExtra("Flag", ""+Flag);
			intent.putExtra("Info", ""+(arg2+1));
			startActivity(intent);
		}
	}
}
