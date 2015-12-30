package com.xyn.activity;

import java.io.IOException;

import com.xyn.ebook.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_webview);
		WebView webview = (WebView) findViewById(R.id.webview);
		Intent intent = getIntent();
		String Flag = intent.getStringExtra("Flag");
		String arg2 = intent.getStringExtra("Info");
//		try {
//			getAssets().open(fileName);//获取输入流
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		WebSettings mWebSettings = webview.getSettings(); 
//	        mWebSettings.setJavaScriptEnabled(true); 
	        mWebSettings.setBuiltInZoomControls(true); 
//	        mWebSettings.setLightTouchEnabled(true); 
	        mWebSettings.setSupportZoom(true); 
	        webview.setHapticFeedbackEnabled(false); 
	        // mWebView.setInitialScale(0); // 改变这个值可以设定初始大小 
	        final String html = "";
	        final String mimeType = "text/html"; 
	        final String encoding = "utf-8";
		    webview.loadUrl("file:///android_asset/"+Flag+"/"+arg2+".html");
//		    webview.setBackground(background);
//	        webview.loadData(html, mimeType, encoding); 
//	        webview.loadDataWithBaseURL("file:///sdcard/", html, mimeType, encoding, ""); 
	}

}
