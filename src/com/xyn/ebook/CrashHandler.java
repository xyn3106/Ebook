package com.xyn.ebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat") public class CrashHandler implements UncaughtExceptionHandler{
	
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	private static final String TAG = "CrashHandler";
	
	private static CrashHandler mInstance = new CrashHandler();
	
	private Context mContext;
	
	private Map<String, String> mLogInfo = new HashMap<String, String>();
	
	private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyyMMdd_HH-mm-ss");

	public static CrashHandler getInstance() {
		return mInstance;
	}

	public void init(Context context) {
		mContext = context;
		// 获取系统默认的UncaughtException处理
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果自定义的没有处理则让系统默认的异常处理器来处
			mDefaultHandler.uncaughtException(thread,ex);
		} else {
			try {
				// 如果处理了，让程序继续，再关闭，保证文件保存
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
	}

	
	public boolean handleException(Throwable paramThrowable) {
		if (paramThrowable == null)
			return false;
		new Thread() {
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "sorry,app is crash.",Toast.LENGTH_SHORT).show();
				Looper.loop();
			}
		}.start();
		// 获取设备参数信息
		getDeviceInfo(mContext);
		// 保存日志文件
		saveCrashLogToFile(paramThrowable);
		return true;
	}
	
	//获取设备参数
	public void getDeviceInfo(Context paramContext) {
		try {
			// 获得包管理器
			PackageManager mPackageManager = paramContext.getPackageManager();
			// 得到该应用的信息，即主Activity
			PackageInfo mPackageInfo = mPackageManager.getPackageInfo(paramContext.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (mPackageInfo != null) {
				String versionName = mPackageInfo.versionName == null ? "null" : mPackageInfo.versionName;
				String versionCode = mPackageInfo.versionCode + "";
				mLogInfo.put("versionName", versionName);
				mLogInfo.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		// 反射机制,通过Build获取手机状态和信息
		Field[] mFields = Build.class.getDeclaredFields();
		// 迭代Build的字段key-value获取runtime时候的出错信息
		for (Field field : mFields) {
			try {
				field.setAccessible(true);
				mLogInfo.put(field.getName(), field.get("").toString());
				Log.e(TAG, field.getName() + ":" + field.get(""));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	//将奔溃日志保存到本地
	private static final String LOG_DIR = Environment.getExternalStorageDirectory() + "/log/";

	private String saveCrashLogToFile(Throwable paramThrowable) {
		StringBuffer mStringBuffer = new StringBuffer();
		for (Map.Entry<String, String> entry : mLogInfo.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			mStringBuffer.append(key + "=" + value + "\r\n");
		}
		Writer mWriter = new StringWriter();
		PrintWriter mPrintWriter = new PrintWriter(mWriter);
		paramThrowable.printStackTrace(mPrintWriter);
		paramThrowable.printStackTrace();
		Throwable mThrowable = paramThrowable.getCause();
		while (mThrowable != null) {
			mThrowable.printStackTrace(mPrintWriter);
			// 换行 每个个异常栈之间换行
			mPrintWriter.append("\r\n");
			mThrowable = mThrowable.getCause();
		}
		mPrintWriter.close();
		final String mResult = mWriter.toString();
		mStringBuffer.append(mResult);
//		//发送错误日志
//		new Thread() {
//			public void run() {
//				Intent intent = new Intent(Intent.ACTION_SEND);
//				intent.setType("text/plain");
//				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //建立一个新的任务栈
//				intent.putExtra(Intent.EXTRA_TEXT, mResult);
//				mContext.startActivity(Intent.createChooser(intent, "请发送错误日志"));
//				Log.e(TAG, "选择发送错误日志");
//			}
//		}.start();
		// 保存文件，设置文件名为crashLog+时间+log
		String mTime = mSimpleDateFormat.format(new Date());
		String mFileName = "CrashLog-" + mTime + ".txt";
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			try {
				File mDirectory = new File(LOG_DIR);
				Log.e(TAG, mDirectory.toString());
				if (!mDirectory.exists())
					mDirectory.mkdirs();
				FileOutputStream mFileOutputStream = new FileOutputStream(mDirectory + "/" + mFileName);
				mFileOutputStream.write(mStringBuffer.toString().getBytes());
				mFileOutputStream.close();
				return mFileName;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
