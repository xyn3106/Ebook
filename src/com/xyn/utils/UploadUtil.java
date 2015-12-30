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
	private static final String mUUID =  UUID.randomUUID().toString(); // �߽��ʶ �������
	private static final String PREFIX = "--";
	private static final String LINE_END = "\r\n";
	private static final String CONTENT_TYPE = "multipart/form-data"; // ��������

	private static final String TAG = "UploadUtil";
	private int readTimeOut = 10 * 1000; // ��ȡ��ʱ
	private int connectTimeout = 10 * 1000; // ��ʱʱ��
	
	/***
	 * ��¼����ʹ�õ�ʱ��
	 */
	private int requestTime = 0;
	
	private static final String CHARSET = "utf-8"; // ���ñ���

	/***
	 * �ϴ��ɹ�
	 */
	public static final int UPLOAD_SUCCESS_CODE = 1;
	
	/**
	 * �ļ�������
	 */
	public static final int UPLOAD_FILE_NOT_EXISTS_CODE = 2;
	
	/**
	 * android�ϴ��ļ���������
	 * 
	 * @param filePath
	 *            ��Ҫ�ϴ����ļ���·��
	 * @param fileKey
	 *            ����ҳ��<input type=file name=xxx/> xxx���������fileKey
	 * @param RequestURL
	 *            �����URL
	 */
	public void uploadFile(String filePath, String fileKey, String RequestURL, Map<String, String> param) {
		if (filePath == null) {
			sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE,"�ļ�������");
			return;
		}
		try {
			File file = new File(filePath);
			if(fileKey==null||fileKey.equals(""))
				fileKey=file.getName();
			uploadFile(file, fileKey, RequestURL, param);
		} catch (Exception e) {
			sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE,"�ļ�������");
			e.printStackTrace();
			return;
		}
	}

	/**
	 * android�ϴ��ļ���������
	 * 
	 * @param file
	 *            ��Ҫ�ϴ����ļ�
	 * @param fileKey
	 *            ����ҳ��<input type=file name=xxx/> xxx���������fileKey
	 * @param RequestURL
	 *            �����URL
	 */
	public void uploadFile(final File file, final String fileKey, final String RequestURL, final Map<String, String> param) {
		if (file == null || (!file.exists())) {
			sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE,"�ļ�������");
			return;
		}
		Log.i(TAG, "�����fileName=" + file.getName());
		Log.i(TAG, "�����fileKey=" + fileKey);
		new Thread(new Runnable() {  //�����߳��ϴ��ļ�
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
			conn.setDoInput(true); // ������
			conn.setDoOutput(true); // ����� 
			conn.setUseCaches(false); // ʹ�û���
			conn.setRequestMethod("POST"); // ����ʽ
			conn.setRequestProperty("Charset", CHARSET); // ���ñ���
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + mUUID);
			conn.connect();//
			/**
			 * ���ļ���Ϊ�գ����ļ���װ�����ϴ�
			 */
			OutputStream os = conn.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			StringBuffer sb = null;
			String params = "";
			/***
			 * �����������ϴ�����
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
			 * �����ص�ע�⣺ name�����ֵΪ����������Ҫkey ֻ�����key �ſ��Եõ���Ӧ���ļ�
			 * filename���ļ������֣�������׺���� ����:abc.png
			 */
			sb.append(PREFIX).append(mUUID).append(LINE_END);
			sb.append("Content-Disposition:form-data; name=\"" + fileKey
					+ "\"; filename=\"" + file.getName() + "\"" + LINE_END);
			sb.append("Content-Type:image/pjpeg" + LINE_END); // �������õ�Content-type����Ҫ�� �����ڷ������˱���ļ������͵�
			sb.append(LINE_END);
			params = sb.toString();
			sb = null;
			Log.e(TAG, file.getName()+"=" + params+"##");
			dos.write(params.getBytes());
			/**�ϴ��ļ�*/
			InputStream is = new FileInputStream(file);
			onUploadProcessListener.initUpload((int)file.length());
			byte[] bytes = new byte[8*1024]; //8k����
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

			int rc = conn.getResponseCode(); //��Ӧ�� 200=�ɹ� ����Ӧ�ɹ�����ȡ��Ӧ����
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
				sendMessage( 1, "�ϴ������"+ msg);
				return;
			} else {
				sendMessage( 3,"�ϴ�ʧ�ܣ�"+ msg);
				return;
			}
		} catch (Exception e) {
			Log.e(TAG, "request error");
			sendMessage(3,"�ϴ�����" + e.getMessage());
			e.printStackTrace();
			return;
		}
	}

	/**
	 * �����ϴ����
	 * @param responseCode
	 * @param responseMessage
	 */
	private void sendMessage(int responseCode,String responseMessage)
	{
		onUploadProcessListener.onUploadDone(responseCode, responseMessage);
	}
	
	/**
	 * ������һ���Զ���Ļص��������õ��ص��ϴ��ļ��Ƿ����
	 * 
	 * @author shimingzheng
	 * 
	 */
	public interface OnUploadProcessListener {
		/**
		 * �ϴ���Ӧ
		 * @param responseCode
		 * @param message
		 */
		void onUploadDone(int responseCode, String message);
		/**
		 * �ϴ���
		 * @param uploadSize
		 */
		void onUploadProcess(int uploadSize);
		/**
		 * ׼���ϴ�
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
	 * ��ȡ�ϴ�ʹ�õ�ʱ��
	 * @return
	 */
	public int getRequestTime() {
		return requestTime;
	}
	
}
