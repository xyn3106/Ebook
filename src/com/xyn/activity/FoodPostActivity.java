package com.xyn.activity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xyn.ebook.Model;
import com.xyn.source.R;
import com.xyn.utils.BitMapUtil;
import com.xyn.utils.FileUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
	private ProgressDialog mDialog;
	private String picPath;
	int port;
	private MyHandler hand;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_food_post);
		initView();
		hand = new MyHandler(this);
		mDialog = new ProgressDialog(this);
		mDialog.setCanceledOnTouchOutside(false);
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
		builder.setMessage("ȷ��Ҫ������");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
//				FoodPostActivity.super.onBackPressed(); //���ø��෽��
				finish();
			}
		});
		builder.setNegativeButton("ȡ��",new DialogInterface.OnClickListener() {
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
		if(picPath == null||food_price.equals("")||food_price.length()>6||food_school.equals("")|| food_school.length()>9
				||food_canteen.equals("")||food_canteen.length()>9||food_desc.length()>200 ){
			Toast.makeText(this, "��Ϣ�д���~",Toast.LENGTH_SHORT).show();
			return;
		}
		mDialog.setMessage("�����ϴ�...");
        mDialog.show();
		final JSONObject jObject = new JSONObject();
		try {
		jObject.put("u_id",Model.u_id);
		jObject.put("f_name",food_name);
		jObject.put("f_price",food_price);
		jObject.put("s_id","1");
		jObject.put("s_name",food_school);
		jObject.put("c_id","1");
		jObject.put("c_name",food_canteen);
		jObject.put("f_desc",food_desc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		StringRequest stringRequest = new StringRequest(Request.Method.POST , Model.HTTPURL+"postFood",
				new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e(TAG, "onResponse: "+response);
//				DataInputStream input = null;
				try {
					JSONObject RJObject = new JSONObject(response);
					int ret = RJObject.getInt("ret");
					if(ret==0)
						port = RJObject.getInt("data");
					else
						throw new Exception(RJObject.getString("msg"));
					new Thread(){
						public void run(){
							Socket socket = null;
							DataOutputStream output = null;  
							FileInputStream fStream = null;
							try {
								socket = new Socket(Model.IP, port);
								output = new DataOutputStream(socket.getOutputStream());  
//								input = new DataInputStream(socket.getInputStream());  
								File file =new File(picPath);
								fStream = new FileInputStream(file);
//								String[] fileEnd = file.getName().split("\\.");  
//								output.writeUTF(BUFF + fileEnd[fileEnd.length - 1].toString());  
//								System.out.println("buffer------------------"+BUFF+fileEnd[fileEnd.length-1].toString());  
								int bufferSize = 16*1024;
								byte[] buffer = new byte[bufferSize];
								int length = 0;
								while ((length = fStream.read(buffer)) != -1)
									output.write(buffer, 0, length);
								output.flush();
//								socket.shutdownOutput(); // һ��Ҫ������䣬�����ղ������Է������˵���Ϣ����  
								/* ȡ��input���� */
//								String msg = input.readUTF();  
//								System.out.println("�ϴ��ɹ�  �ļ�λ��Ϊ��" + msg);
								Message msg = hand.obtainMessage();
								msg.what=200;
				        		hand.sendMessage(msg);
//								PostSucess();
							} catch (Exception e) {
								e.printStackTrace();
								Message msg = hand.obtainMessage();
								msg.what=201;
				        		hand.sendMessage(msg);
//								PostFailed();
							} finally {
									try {
					                    if (fStream != null)
					                    	fStream.close();  
					                    if (output != null)
					                    	output.close();  
					                    if (socket != null)
					                    	socket.close();  
					                } catch (IOException e) {  
					                    e.printStackTrace();  
					                }  
									}
							}
						}.start();
				} catch (Exception e) {
					e.printStackTrace();
					PostFailed();
					}
			}},new Response.ErrorListener() {
				@Override  
				public void onErrorResponse(VolleyError error) {
					Log.e(TAG, "onErrorResponse: "+error.toString());
					error.printStackTrace();
					PostFailed();
					}
				}){
			 @Override
			 protected Map<String, String> getParams() {
				 Map<String, String> params = new HashMap<String, String>();
				 params.put("u_id", Model.u_id);
				 params.put("data", jObject.toString());
				 return params;
			 }
		};
		stringRequest.setRetryPolicy(Model.RetryPolicy);
		FrameActivity.mRequestQueue.add(stringRequest);
		
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//					String response = ServerUtils.formUpload(Model.HTTPURL+"postFood", picPath);
//					Log.e("", "response:" + response);
//					JSONObject jObject;int res = 2;
//					try {
//						jObject = new JSONObject(response.toString());
//						res = jObject.getInt("ret");
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//					Message msg = hand.obtainMessage();
//	        		if(res==0)
//	        			msg.what=200;
//	        		else
//	        			msg.what=201;
//	        		hand.sendMessage(msg);
//			}
//		}).start();
		
//		UploadUtil upload = new UploadUtil();
//		OnUploadProcessListener listener = new OnUploadProcessListener(){
//			@Override
//			public void onUploadDone(int responseCode, String message) { //���̣߳����ܲ���UI
//        		Message msg = hand.obtainMessage();
//        		if(responseCode==1)
//        			msg.what=200;
//        		else
//        			msg.what=201;
//        		hand.sendMessage(msg);
//			}
//			public void onUploadProcess(int uploadSize){
//        		Log.e(TAG, "uploadSize = "+uploadSize);
//			}
//			@Override
//			public void initUpload(int fileSize) {
//        		Log.e(TAG, "fileSize = "+fileSize);
//			}
//		};
//		upload.setOnUploadProcessListener(listener);
//		Map<String, String> param = new HashMap<String, String>();//?u_id="+Model.u_id+"&data=//+jObject.toString()
//		upload.uploadFile(picPath, "toUploadImage", Model.HTTPURL+"postFood", null);
	}
	
	private void addImage(){
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			if (intent != null) {
                Uri URI = intent.getData();  
                //���ص�Uri��Ϊ��ʱ����ôͼƬ���ݻ���Uri�л��
                if (URI != null) {
            		Log.e(TAG, "URI = "+URI);
                    try {
                    	ContentResolver mContentResolver = this.getContentResolver();
                    	Bitmap bitmap = BitMapUtil.decodeStream_compress(mContentResolver.openInputStream(URI)
                    			,640, 640, 256*1024);
                        if (bitmap != null){
                        	food_post_addimage.setImageBitmap(bitmap);
                        	String Path = Environment.getExternalStorageDirectory() + "/Canteen/";
                        	String filename = "toUploadImage.j";
                        	FileUtil.BitmapToFile(Path, filename, bitmap);
                        	picPath = Path + filename;
                    		Log.e(TAG, "picPath = "+picPath);
//                        	picPath = UriPathUtil.getPath(this, URI);
                        }
//                      String[] pojo = {MediaStore.Images.Media.DATA};
//                		Cursor cursor = mContentResolver.query(URI, null, null, null,null);
//                		String value = null;
//                		if(cursor != null ){
//                			int columnIndex = cursor.getColumnCount();
//                    		Log.e(TAG, "RowsCount = "+cursor.getCount());
//                    		Log.e(TAG, "ColumnCount = "+columnIndex);
//                			cursor.moveToFirst();
//                    		for(int i=0; i<columnIndex; i++){
//                				Log.e(TAG, "ColumnName = "+cursor.getColumnName(i));
//                				value = cursor.getString(i);
//                        		Log.e(TAG, "value = "+value);
//                    		}
//                			cursor.close();
//                		}
                    } catch (Exception e) {
                        e.printStackTrace();  
                    }  
                } else {
                    Bundle extras = intent.getExtras();  
                    if (extras != null) {
                        //��Щ���պ��ͼƬ��ֱ�Ӵ�ŵ�Bundle�е����Դ������ȡBitmap
                        Bitmap image = extras.getParcelable("data");  
                        if (image != null)
                        	food_post_addimage.setImageBitmap(image);
                        }  
                    }  
                }
			}
		super.onActivityResult(requestCode, resultCode, intent);
	}

//	private void PostSucess(){
//		mDialog.dismiss();
//		Toast.makeText(this, "�ϴ��ɹ�����л�ύ~",Toast.LENGTH_SHORT).show();
//		finish();
//	}
	private void PostFailed(){
		mDialog.dismiss();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("�ϴ�ʧ�ܣ����Ժ�����");
		builder.setPositiveButton("����", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
//				mFoodPostActivity.finish();
			}
		});
		builder.create().show();
	}
	
	static class MyHandler extends Handler {
	    private WeakReference<FoodPostActivity> mWeakReference;
	    FoodPostActivity mFoodPostActivity;
	    public MyHandler(FoodPostActivity activity){
			mWeakReference = new WeakReference<FoodPostActivity>(activity);
			mFoodPostActivity = mWeakReference.get();
		}
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
			case 200:
				mFoodPostActivity.mDialog.dismiss();
				Toast.makeText(mFoodPostActivity, "�ϴ��ɹ�����л�ύ~",Toast.LENGTH_SHORT).show();
				mFoodPostActivity.finish();
				break;
			case 201:
				mFoodPostActivity.mDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(mFoodPostActivity);
				builder.setMessage("�ϴ�ʧ�ܣ����Ժ�����");
				builder.setPositiveButton("����", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
//						mFoodPostActivity.finish();
					}
				});
				builder.create().show();
				break;
			}
		}
	}

}
