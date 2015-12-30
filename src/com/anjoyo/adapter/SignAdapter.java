package com.anjoyo.adapter;

import java.util.List;

import com.xyn.bean.SignInfo;
import com.xyn.ebook.R;
import com.xyn.utils.SmileyParser;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SignAdapter extends BaseAdapter {

	private List<SignInfo> list;
	private Context ctx;
//	private LoadImg loadImg;
	private String[] mFaceValue;
	final String arrText1[] = new String[20];
	final String arrText2[] = new String[20];
	final String arrText3[] = new String[20];
	final String arrText4[] = new String[16];

	public SignAdapter(List<SignInfo> list, Context ctx) {
		this.list = list;
		this.ctx = ctx;
//		loadImg = new LoadImg(ctx);
		mFaceValue = this.ctx.getResources().getStringArray(R.array.default_smiley_texts);
		initModel();
	}

	private void initModel() {
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

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		final Holder hold;
		SmileyParser.init(ctx);
		final SmileyParser parser = SmileyParser.getInstance();
		if (arg1 == null) {
			hold = new Holder();
			//inflate一个新的视图
			arg1 = View.inflate(ctx, R.layout.item_bubble_list, null);
			hold.b_likecount = (TextView) arg1.findViewById(R.id.b_likecount);
			hold.b_content = (TextView) arg1.findViewById(R.id.b_content);
			hold.u_name = (TextView) arg1.findViewById(R.id.u_name);
			hold.b_time = (TextView) arg1.findViewById(R.id.b_time);
			hold.u_portrait = (ImageView) arg1.findViewById(R.id.u_portrait);
			hold.b_img = (ImageView) arg1.findViewById(R.id.b_img);
			hold.b_like_btn = (ImageView) arg1.findViewById(R.id.b_like_btn);
			hold.u_portrait.setOnClickListener(mMyOnclickListener);
			hold.u_name.setOnClickListener(mMyOnclickListener);
			hold.b_img.setOnClickListener(mMyOnclickListener);
			hold.b_time.setOnClickListener(mMyOnclickListener);
			hold.b_content.setOnClickListener(mMyOnclickListener);
			hold.b_like_btn.setOnClickListener(mMyOnclickListener);
			arg1.setTag(hold);
			hold.b_like_btn.setTag(hold); //反引用
		} else {
			hold = (Holder) arg1.getTag();
		}
		hold.b_content.setText(parser.addSmileySpans(list.get(arg0).getb_content()));
		hold.u_name.setText(list.get(arg0).getu_name());
//		hold.u_name.setTag(list.get(arg0).getu_id()); //setTag保存对用户id的引用
		hold.b_time.setText(list.get(arg0).getb_time());
		hold.b_likecount.setText(list.get(arg0).getb_likecount());
		if(list.get(arg0).isLiked())
			hold.b_like_btn.setBackgroundResource(R.drawable.emoji106);
		else
			hold.b_like_btn.setBackgroundResource(R.drawable.my_fans_icon_press);
		hold.obj = list.get(arg0);
//		int slevel = Integer.valueOf(list.get(arg0).getSignlevel());
//		switch (slevel) {
//		case 0:
//			hold.b_score.setImageResource(R.drawable.star0);
//			break;
//		case 1:
//			hold.b_score.setImageResource(R.drawable.star1);
//			break;
//		case 2:
//			hold.b_score.setImageResource(R.drawable.star2);
//			break;
//		case 3:
//			hold.b_score.setImageResource(R.drawable.star3);
//			break;
//		case 4:
//			hold.b_score.setImageResource(R.drawable.star4);
//			break;
//		case 5:
//			hold.b_score.setImageResource(R.drawable.star5);
//			break;
//		}

		//TODO 头像，图片的获取
		hold.u_portrait.setTag(list.get(arg0).getu_id());//setTag用来检验获取到的图片，防止错位
		hold.b_img.setTag(list.get(arg0).getb_img());
//		hold.b_img.setVisibility(View.INVISIBLE);
//		if (list.get(arg0).getSignimage().equals("")) {
//			hold.b_img.setVisibility(View.GONE);
//		}
//		Bitmap bit = loadImg.loadImage(hold.b_img, Model.SIGNLISTIMGURL
//				+ list.get(arg0).getSignimage(), new ImageDownloadCallBack() {
//			@Override
//			public void onImageDownload(ImageView imageView, Bitmap bitmap) {
//				// 网络交互时回调进来防止错位
//				if (hold.b_img.getTag().equals(
//						Model.SIGNLISTIMGURL + list.get(arg0).getSignimage())) {
//					hold.b_img.setVisibility(View.VISIBLE);
//					// 设置网络下载回来图片显示
//					hold.b_img.setImageBitmap(bitmap);
//				}
//			}
//		});
//		// 从本地获取的
//		if (bit != null) {
//			// 设置缓存图片显示
//			hold.b_img.setVisibility(View.VISIBLE);
//			hold.b_img.setImageBitmap(bit);
//		}

		return arg1;
	}
	
	static class Holder {
		ImageView b_img,b_like_btn,u_portrait;
		TextView b_content,u_name,b_time,b_likecount;
		SignInfo obj;
	}

	private void onUserClick(final String u_id){
		final EditText mEditText = new EditText(ctx);
		mEditText.setHint("关注前想对ta说的话...");
		AlertDialog dialog = new AlertDialog.Builder(ctx).setIcon(android.R.drawable.btn_star)
				.setTitle("关注此用户")
				.setView(mEditText)
				.setPositiveButton("关注",
			     new DialogInterface.OnClickListener() {
			      @Override
			      public void onClick(DialogInterface dialog, int which) {
			    	  String what = mEditText.getText().toString().trim();
			    	  Toast.makeText(ctx, "成功关注"+u_id+"用户，对ta说："+what, Toast.LENGTH_SHORT).show();
			      }
			      }).create();
		dialog.show();
	}
	
	private MyOnclickListener mMyOnclickListener = new MyOnclickListener();
	private class MyOnclickListener implements OnClickListener {
		public void onClick(View v) {
			int ID = v.getId();
			switch (ID) {
			case R.id.b_like_btn:
				Holder hold = (Holder) v.getTag();
				if(!hold.obj.isLiked()){
					v.setBackgroundResource(R.drawable.emoji106);
					hold.obj.setb_likecount(""+(Integer.parseInt(hold.obj.getb_likecount())+1));
					hold.b_likecount.setText(hold.obj.getb_likecount());
					hold.obj.setLiked(true);
				} else {
					v.setBackgroundResource(R.drawable.my_fans_icon_press);
					hold.obj.setb_likecount(""+(Integer.parseInt(hold.obj.getb_likecount())-1));
					hold.b_likecount.setText(hold.obj.getb_likecount());
					hold.obj.setLiked(false);
					}
				break;
			case R.id.u_portrait:
				onUserClick((String) v.getTag());
				break;
			case R.id.u_name:
//				onUserClick((String) v.getTag());
				break;
			}
		}
	}

}
