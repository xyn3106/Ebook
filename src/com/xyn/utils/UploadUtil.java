package com.xyn.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;

import android.util.Log;

public class UploadUtil {
	private static final String mUUID =  UUID.randomUUID().toString(); // 边界标识 随机生成
	private static final String PREFIX = "--";
	private static final String LINE_END = "\r\n";
	private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型

	private static final String TAG = "UploadUtil";
	private int readTimeOut = 10 * 1000; // 读取超时
	private int connectTimeout = 10 * 1000; // 超时时间
	
	/***
	 * 记录请求使用的时间
	 */
	private int requestTime = 0;
	
	private static final String CHARSET = "utf-8"; // 设置编码

	/***
	 * 上传成功
	 */
	public static final int UPLOAD_SUCCESS_CODE = 1;
	
	/**
	 * 文件不存在
	 */
	public static final int UPLOAD_FILE_NOT_EXISTS_CODE = 2;
	
	/**
	 * android上传文件到服务器
	 * 
	 * @param filePath
	 *            需要上传的文件的路径
	 * @param fileKey
	 *            在网页上<input type=file name=xxx/> xxx就是这里的fileKey
	 * @param RequestURL
	 *            请求的URL
	 */
	public void uploadFile(String filePath, String fileKey, String RequestURL, Map<String, String> param) {
		if (filePath == null) {
			sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE,"文件不存在");
			return;
		}
		try {
			File file = new File(filePath);
			if(fileKey==null||fileKey.equals(""))
				fileKey=file.getName();
			uploadFile(file, fileKey, RequestURL, param);
		} catch (Exception e) {
			sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE,"文件不存在");
			e.printStackTrace();
			return;
		}
	}

	/**
	 * android上传文件到服务器
	 * 
	 * @param file
	 *            需要上传的文件
	 * @param fileKey
	 *            在网页上<input type=file name=xxx/> xxx就是这里的fileKey
	 * @param RequestURL
	 *            请求的URL
	 */
	public void uploadFile(final File file, final String fileKey, final String RequestURL, final Map<String, String> param) {
		if (file == null || (!file.exists())) {
			sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE,"文件不存在");
			return;
		}
		Log.i(TAG, "请求的fileName=" + file.getName());
		Log.i(TAG, "请求的fileKey=" + fileKey);
		new Thread(new Runnable() {  //开启线程上传文件
			@Override
			public void run() {
				toUploadFile(file, fileKey, RequestURL, param);
			}
		}).start();
		
	}

	private void toUploadFile(File file, String fileKey, String RequestURL, Map<String, String> param) {
		requestTime= 0;
		long requestTime = System.currentTimeMillis();
		long responseTime = 0;

		try {
			URL url = new URL(RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(readTimeOut);
			conn.setConnectTimeout(connectTimeout);
			conn.setDoInput(true); // 输入流
			conn.setDoOutput(true); // 输出流 
			conn.setUseCaches(false); // 使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + mUUID);
			conn.connect();//
			/**
			 * 当文件不为空，把文件包装并且上传
			 */
			OutputStream os = conn.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			StringBuffer sb = null;
			String params = "";
			/***
			 * 以下是用于上传参数
			 */
			if (param != null && param.size() > 0) {
				Iterator<String> it = param.keySet().iterator();
				while (it.hasNext()) {
					sb = null;
					sb = new StringBuffer();
					String key = it.next();
					String value = param.get(key);
					sb.append(PREFIX).append(mUUID).append(LINE_END);
					sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END).append(LINE_END);
					sb.append(value).append(LINE_END);
					params = sb.toString();
					Log.e(TAG, key+"="+params+"##");
					dos.write(params.getBytes());
				}
			}
			sb = null;
			params = null;
			sb = new StringBuffer();
			/**
			 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
			 * filename是文件的名字，包含后缀名的 比如:abc.png
			 */
			sb.append(PREFIX).append(mUUID).append(LINE_END);
			sb.append("Content-Disposition:form-data; name=\"" + fileKey
					+ "\"; filename=\"" + file.getName() + "\"" + LINE_END);
			sb.append("Content-Type:image/pjpeg" + LINE_END); // 这里配置的Content-type很重要的 ，用于服务器端辨别文件的类型的
			sb.append(LINE_END);
			params = sb.toString();
			sb = null;
			Log.e(TAG, file.getName()+"=" + params+"##");
			dos.write(params.getBytes());
			/**上传文件*/
			InputStream is = new FileInputStream(file);
			onUploadProcessListener.initUpload((int)file.length());
			byte[] bytes = new byte[8*1024]; //8k缓存
			int len = 0;
			int curLen = 0;
			while ((len = is.read(bytes)) != -1) {
				curLen += len;
				dos.write(bytes, 0, len);
				onUploadProcessListener.onUploadProcess(curLen);
			}
			is.close();
			dos.write(LINE_END.getBytes());
			byte[] end_data = (PREFIX + mUUID + PREFIX + LINE_END).getBytes();
			dos.write(end_data);
			dos.flush();
			dos.close();
			os.close();

			int rc = conn.getResponseCode(); //响应码 200=成功 当响应成功，获取响应的流
			Log.e(TAG, "ResponseCode: " + rc);
			InputStream input = conn.getInputStream();
			StringBuffer sb1 = new StringBuffer();
			int ss;
			while ((ss = input.read()) != -1) {
				sb1.append((char) ss);
			}
			String response = sb1.toString();
			input.close();
			Log.e(TAG, "response:" + response);
			JSONObject jObject = new JSONObject(response.toString());
			int res = jObject.getInt("ret");
			String msg = "test";
//			String msg = jObject.getString("msg");
			responseTime = System.currentTimeMillis();
			this.requestTime = (int) ((responseTime-requestTime)/1000);
			Log.e(TAG, "ret:" + res);
			Log.e(TAG, "msg : " + msg);
			if (res == 0) {
				sendMessage( 1, "上传结果："+ msg);
				return;
			} else {
				sendMessage( 3,"上传失败："+ msg);
				return;
			}
		} catch (Exception e) {
			Log.e(TAG, "request error");
			sendMessage(3,"上传出错：" + e.getMessage());
			e.printStackTrace();
			return;
		}
	}

	/**
	 * 发送上传结果
	 * @param responseCode
	 * @param responseMessage
	 */
	private void sendMessage(int responseCode,String responseMessage)
	{
		onUploadProcessListener.onUploadDone(responseCode, responseMessage);
	}
	
	/**
	 * 下面是一个自定义的回调函数，用到回调上传文件是否完成
	 * 
	 * @author shimingzheng
	 * 
	 */
	public interface OnUploadProcessListener {
		/**
		 * 上传响应
		 * @param responseCode
		 * @param message
		 */
		void onUploadDone(int responseCode, String message);
		/**
		 * 上传中
		 * @param uploadSize
		 */
		void onUploadProcess(int uploadSize);
		/**
		 * 准备上传
		 * @param fileSize
		 */
		void initUpload(int fileSize);
	}
	
	private OnUploadProcessListener onUploadProcessListener;
	
	

	public void setOnUploadProcessListener(
			OnUploadProcessListener onUploadProcessListener) {
		this.onUploadProcessListener = onUploadProcessListener;
	}

	public int getReadTimeOut() {
		return readTimeOut;
	}

	public void setReadTimeOut(int readTimeOut) {
		this.readTimeOut = readTimeOut;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	/**
	 * 获取上传使用的时间
	 * @return
	 */
	public int getRequestTime() {
		return requestTime;
	}
	
}
