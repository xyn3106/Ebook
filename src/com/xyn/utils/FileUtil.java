package com.xyn.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

/**
 * 1.ǿ���ã�����ʵ����һ������
 * JVM�����ڴ��Ƿ���ϵͳ�������ͷ�����ڴ�
 * 2.�����ã�softReference��:������ϵͳ�ڴ治��ʱ�����ͷŵ�
 * 3.�����ã�������ϵͳ�����ڴ�ʱ������һ�������ö���ֱ�������
 * 4.�����ã������������ڴ�ʱ��
 * �������ö������һ��������е��У�
 * �����ǳ���Ա���浱ǰ�����״̬
 * FileUtiles ����: ������SDcard���������������ͼƬ,����BitmapͼƬ������
 * */
public class FileUtil {

	private static String TAG = "FileUtiles";
	private Context ctx;

	public FileUtil(Context ctx){
		this.ctx = ctx;
	}

	//��ȡ�ֻ���sdcard����ͼƬ�ĵ�ַ
	public String getAbsolutePath(){
		File root = ctx.getExternalFilesDir(null);
		//�����ֻ��˵ľ���·�������������ж�أ��Լ�������ʱ�ᱻ�����
		if(root != null)
			return root.getAbsolutePath();
		return null;
	}

	//�ж�ͼƬ�ڱ��ػ��浱���Ƿ���ڣ�������ڷ���һ��true
	public boolean isBitmap(String name){
		File root = ctx.getExternalFilesDir(null);
		//file��ַƴ��
		File file = new File(root,name);
		return file.exists();
	}

	//��ӵ����ػ��浱��
	public void saveBitmap(String name,Bitmap bitmap){
		if(bitmap == null)
			return;
		//���sdcard����ʹ��
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			Log.e(TAG, "saveBitmapʧ��:SDcard����ʹ��(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))");
			return;
		}
		//ƴ��ͼƬҪ���浽sd���ĵ�ַ
		String BitPath = getAbsolutePath()+"/"+name;
		//mtn/sdcard/android/com.anjoyo.zhangxinyi/files/
		try {
			FileOutputStream fos = new FileOutputStream(BitPath);
			/**
			 * bitmap.compress��ͼƬͨ����������浽����
			 * Bitmap.CompressFormat.JPEG ����ͼƬ�ĸ�ʽ
			 * 100 ���浽���ص�ͼƬ��������Ҫѹ��ʱ�ʵ�������С
			 * */
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void BitmapToFile(final String Path, final String filename, final Bitmap bitmap){
		new Thread(){
			@Override
			public void run(){
				File f = new File(Path,filename);
				if (f.exists()) {
					f.delete();
					}
				else
					f.getParentFile().mkdirs();
				try {
					f.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
						}
		  	  try {
		  		  FileOutputStream out = new FileOutputStream(f);
		  		  bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
		  		  out.flush();
		  		  out.close();
		  	  } catch (Exception e) {
		  	   e.printStackTrace();
		  	  } 
			}
		}.start();
	}

}
