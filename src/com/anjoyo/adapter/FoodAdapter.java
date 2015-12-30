package com.anjoyo.adapter;

import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.xyn.activity.FrameActivity;
import com.xyn.ebook.FoodModel;
import com.xyn.ebook.Model;
import com.xyn.ebook.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 食品列表的适配器
 */

public class FoodAdapter extends BaseAdapter {

	private List<FoodModel> list;
	private Context ctx;
	private ImageLoader mImageLoader = FrameActivity.mImageLoader;

	public FoodAdapter(List<FoodModel> list, Context ctx) {
		this.list = list;
		this.ctx = ctx;
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
	public View getView(final int arg0, View convertView, ViewGroup arg2) {
		final Holder hold;
		if (convertView == null) { //视图未生成
			hold = new Holder();
			convertView = View.inflate(ctx, R.layout.item_food_list, null);
			hold.mTitle = (TextView) convertView.findViewById(R.id.ShopItemTextView);
			hold.mImage = (ImageView) convertView.findViewById(R.id.ShopItemImage);
			hold.mMoney = (TextView) convertView.findViewById(R.id.ShopItemMoney);
			hold.mAddress = (TextView) convertView.findViewById(R.id.ShopItemAddress);
			hold.mStytle = (TextView) convertView.findViewById(R.id.ShopItemStytle);
			hold.mStar = (ImageView) convertView.findViewById(R.id.ShopItemStar);
			hold.mTuan = (ImageView) convertView.findViewById(R.id.ShopItemTuan);
			hold.mQuan = (ImageView) convertView.findViewById(R.id.ShopItemQuan);
			hold.mDing = (ImageView) convertView.findViewById(R.id.ShopItemDing);
			hold.mCard = (ImageView) convertView.findViewById(R.id.ShopItemCard);
			convertView.setTag(hold);
		} else { //视图可复用
			hold = (Holder) convertView.getTag();
		}
		hold.mTitle.setText(list.get(arg0).getf_name());
//		hold.mImage.setTag(Model.FOODLISTIMGURL + list.get(arg0).getImgSrc());
		hold.mMoney.setText(list.get(arg0).getf_price());
		hold.mAddress.setText("评价数："+list.get(arg0).getf_commentcount());
		hold.mStytle.setText("饭堂："+list.get(arg0).getc_name());
		int slevel = Integer.valueOf(list.get(arg0).getf_score());
		switch (slevel) {
		case 0:
			hold.mStar.setImageResource(R.drawable.star0);
			break;
		case 1:
			hold.mStar.setImageResource(R.drawable.star1);
			break;
		case 2:
			hold.mStar.setImageResource(R.drawable.star2);
			break;
		case 3:
			hold.mStar.setImageResource(R.drawable.star3);
			break;
		case 4:
			hold.mStar.setImageResource(R.drawable.star4);
			break;
		case 5:
			hold.mStar.setImageResource(R.drawable.star5);
			break;
		}
		// 获取图片
		ImageListener listener = ImageLoader.getImageListener(hold.mImage,
				R.drawable.shop_photo_frame, R.drawable.shop_photo_frame);//加载中和失败的图片
		mImageLoader.get(Model.FOOD_IMG_URL+list.get(arg0).getImgSrc(), listener, 480, 480);//URL和图片限制
		return convertView;
//		hold.mTuan.setVisibility(View.GONE); //TODO FoodModel里的团，券，订，卡四个标签参数
//		hold.mQuan.setVisibility(View.GONE); 
//		hold.mDing.setVisibility(View.GONE);
//		hold.mCard.setVisibility(View.GONE);
//		if (list.get(arg0).getSflag_tuan().equals("1")) {
//			hold.mTuan.setVisibility(View.VISIBLE);
//		}
//		if (list.get(arg0).getSflag_quan().equals("1")) {
//			hold.mQuan.setVisibility(View.VISIBLE);
//		}
//		if (list.get(arg0).getSflag_ding().equals("1")) {
//			hold.mDing.setVisibility(View.VISIBLE);
//		}
//		if (list.get(arg0).getSflag_ka().equals("1")) {
//			hold.mCard.setVisibility(View.VISIBLE);
//		}
		// 从网络和本地获取到图片
//		Bitmap bit = loadImg.loadImage(hold.mImage, Model.FOODLISTIMGURL + list.get(arg0).getImgSrc(),
//				new ImageDownloadCallBack() {
//			@Override
//			public void onImageDownload(ImageView imageView, Bitmap bitmap) {
//				// 网络交互时回调进来防止错位
//				if (hold.mImage.getTag().equals(
//						Model.FOODLISTIMGURL + list.get(arg0).getImgSrc())) {
//					// 设置网络下载回来图片显示
//					hold.mImage.setImageBitmap(bitmap);
//				}
//			}
//		});
//		if (bit != null) {
//			hold.mImage.setImageBitmap(bit);
//		}
	}

	private static class Holder {
		TextView mTitle, mMoney, mAddress, mStytle;
		ImageView mImage, mStar, mTuan, mQuan, mDing, mCard;
	}

}
