package com.xyn.source;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.app.Application;

public class MyApplication extends Application{
	
	private static MyApplication instance;

	public void onCreate()
	{
		super.onCreate();
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(this);
	}

	public static MyApplication getInstance() {
		if (instance == null) {
			instance = new MyApplication();
		}
		return instance;
	}
	
	@Override
	public void onTerminate()
	{
		super.onTerminate();
	}
	
}
