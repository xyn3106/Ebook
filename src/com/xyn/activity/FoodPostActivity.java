package com.xyn.activity;

import com.xyn.bean.FoodModel;
import com.xyn.source.R;
import com.xyn.utils.BitMapUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
public class FoodPostActivity extends Activity {

	private static String TAG = "FoodPostActivity";
	private TextView food_post_tittle_username;
	private ImageView mShoplist_back;
	private EditText food_name,food_price,food_school,food_canteen,food_desc;
	private ImageView food_post_addimage;
	private TextView mshop_qiandao_OK;
	private ProgressBar loading_progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_food_post);
		initView();
	}

	private void initView() {
		food_post_tittle_username = (TextView) findViewById(R.id.food_post_tittle_username);
		mShoplist_back = (ImageView) findViewById(R.id.Shoplist_back);
		food_name = (EditText) findViewById(R.id.food_name);
		food_price = (EditText) findViewById(R.id.food_price);
		food_school = (EditText) findViewById(R.id.food_school);
		food_canteen = (EditText) findViewById(R.id.food_canteen);
		food_desc = (EditText) findViewById(R.id.food_desc);
		food_post_addimage = (ImageView) findViewById(R.id.food_post_addimage);
		mshop_qiandao_OK = (TextView) findViewById(R.id.shop_qiandao_OK);
		MyOnClickListener myonclick = new MyOnClickListener();
		mShoplist_back.setOnClickListener(myonclick);
		food_post_addimage.setOnClickListener(myonclick);
		mshop_qiandao_OK.setOnClickListener(myonclick);
	}
	
	@Override
	public void onBackPressed(){
		showExitDialog();
	}
	
	private void showExitDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("确定要返回吗？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				FoodPostActivity.super.onBackPressed(); //调用父类方法
			}
		});
		builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {}
			});
		builder.create().show();
	}

	private class MyOnClickListener implements View.OnClickListener {
		public void onClick(View v) {
			int mID = v.getId();
			if (mID == R.id.shop_qiandao_OK) {
				postFood();
			}
			else if(mID == R.id.Shoplist_back){
				showExitDialog();
			}
			else if(mID == R.id.food_post_addimage){
				addImage();
			}
		}
	}

	private void postFood() {
		String food_name = this.food_name.getText().toString().trim();
		String food_price = this.food_price.getText().toString().trim();
		String food_school = this.food_school.getText().toString().trim();
		String food_canteen = this.food_canteen.getText().toString().trim();
		String food_desc = this.food_desc.getText().toString().trim();
		String food_image = "";//TODO 图片
		FoodModel food = new FoodModel();
		food.setf_name(food_name);
		food.setf_price(food_price);
		food.setf_price2(food_price);
		food.sets_id("");//TODO s_id,c_id
		food.setc_id("");
		food.setc_name(food_canteen);
		food.setf_desc(food_desc);
		food.setImgSrc(food_image);
//		ThreadPoolUtils.execute(new HttpPostThread(hand, Model.SIGNURL, "1", obj));
	}
	
	private void addImage(){
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			if (data != null) {  
                Uri mImageUri = data.getData();  
                //返回的Uri不为空时，那么图片数据会在Uri中获得
                if (mImageUri != null) {
                    try {  
//                    	Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver()
//                    			.openInputStream(mImageUri));
//                        if (bitmap != null) {
                    	Bitmap bitmap = BitMapUtil.compressBitmap(this.getContentResolver()
                    			.openInputStream(mImageUri), 720, 1280, 256*1024);
                    	food_post_addimage.setImageBitmap(bitmap);
//                        }  
                    } catch (Exception e) {  
                        e.printStackTrace();  
                    }  
                } else {
                    Bundle extras = data.getExtras();  
                    if (extras != null) {
                        //有些拍照后的图片是直接存放到Bundle中的所以从这里获取Bitmap
                        Bitmap image = extras.getParcelable("data");  
                        if (image != null) {  
                        	food_post_addimage.setImageBitmap(image);
                        }  
                    }  
                }  
			}
//		String[] projection = { MediaStore.Images.Media.DATA };
//		ContentResolver ContentResolver = this.getContentResolver();
//		Bitmap bitmap = BitmapFactory.decodeStream(ContentResolver.openInputStream(uri));
//		food_post_addimage.setImageBitmap(bitmap);
//		Cursor cursor = ContentResolver.query(uri, projection, null, null, null);
//	if (cursor != null) {
//		Log.e(TAG, "ContentResolver = " + ContentResolver);
//		int image_colunm_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//		Log.e(TAG, "colunm_index = " + image_colunm_index);
//		boolean b = cursor.moveToFirst();
//		Log.e(TAG, "moveToFirst() = " + b);
//		String path = cursor.getString(image_colunm_index);
//		Log.e(TAG, "path = " + path);
//		if (path.endsWith("jpg") || path.endsWith("png")) {
//			picPath = path;
//			Bitmap bitmap = BitmapFactory.decodeStream(ContentResolver.openInputStream(uri));
//			food_post_addimage.setImageBitmap(bitmap);
//			} else {
//				alert();
//				}
//		} else {
//			alert();
//			}
	}
	super.onActivityResult(requestCode, resultCode, data);
	}
//	private void alert() {
//		Dialog dialog = new AlertDialog.Builder(this).setTitle("提示")
//		.setMessage("选择的图片出错")
//		.setPositiveButton("返回", new DialogInterface.OnClickListener() {
//		public void onClick(DialogInterface dialog, int which) { }
//		}).create();
//		dialog.show();
//		}

	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 200) {
				Toast.makeText(FoodPostActivity.this, "上传成功，感谢提交~",Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	};

}
