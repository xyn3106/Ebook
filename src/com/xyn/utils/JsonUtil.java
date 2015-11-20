package com.xyn.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.xyn.bean.CommentsInfo;
import com.xyn.bean.FoodModel;
import com.xyn.bean.SignInfo;

import android.util.Log;

/**
 * Json字符串解析工具类
 */

public class JsonUtil {
	
	private static String TAG = "MyJson";
	private ArrayList<SignInfo> SignList = new ArrayList<SignInfo>();
	private ArrayList<CommentsInfo> CommentsList = new ArrayList<CommentsInfo>();
	private ArrayList<FoodModel> FoodList = new ArrayList<FoodModel>();

	public static List<FoodModel> getFoodList(String retStr) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(retStr);
		//获取返回码，0表示成功
		int retCode = jsonObject.getInt("ret");
		if (0==retCode){
			List<FoodModel> Foodlist = new ArrayList<FoodModel>();
			JSONArray objArray = jsonObject.getJSONArray("data");
			for(int i=0; i<objArray.length(); i++){
					JSONObject obj = (JSONObject)objArray.get(i); 
					FoodModel food = new FoodModel();
					food.setf_id(obj.getString("f_id"));
//					food.sets_id(obj.getString("s_id"));
					food.setc_id(obj.getString("c_id"));
					food.setc_name(obj.getString("c_name"));
					food.setf_name( obj.getString("f_name"));
					food.setf_price(obj.getString("f_price"));
					food.setf_price2(obj.getString("f_price2"));
					food.setf_date(obj.getString("f_date"));
					food.setf_desc(obj.getString("f_desc"));
					food.setf_commentcount(obj.getString("f_commentcount"));
					food.setImgSrc(obj.getString("imgsrc"));
					food.setf_score(obj.getString("f_score"));
					Foodlist.add(food);
					}
			return Foodlist;
//				}
			}
		else{
			Log.e(TAG, "retCode="+retCode);
			Log.e(TAG, "msg="+jsonObject.getString("msg"));
		}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<FoodModel> getFoodList_old(String value) {
		List<FoodModel> list = null;
		try {
			JSONArray jay = new JSONArray(value);
			list = new ArrayList<FoodModel>();
			for (int i = 0; i < jay.length(); i++) {
				JSONObject job = jay.getJSONObject(i);
				FoodModel info = new FoodModel();
//				info.setSid(job.getString("sid"));
//				info.setSname(job.getString("sname"));
//				info.setStype(job.getString("stype"));
//				info.setSaddress(job.getString("saddress"));
//				info.setSnear(job.getString("snear"));
//				info.setStel(job.getString("stel"));
//				info.setStime(job.getString("stime"));
//				info.setSzhekou(job.getString("szhekou"));
//				info.setSmembercard(job.getString("smembercard"));
//				info.setSper(job.getString("sper"));
//				info.setSmoney(job.getString("smoney"));
//				info.setSnum(job.getString("snum"));
//				info.setSlevel(job.getString("slevel"));
//				info.setSflag_tuan(job.getString("sflag_tuan"));
//				info.setSflag_quan(job.getString("sflag_quan"));
//				info.setSflag_ding(job.getString("sflag_ding"));
//				info.setSflag_ka(job.getString("sflag_ka"));
//				info.setLongitude(job.getString("longitude"));
//				info.setLatitude(job.getString("latitude"));
//				info.setSintroduction(job.getString("sintroduction"));
//				info.setSdetails(job.getString("sdetails"));
//				info.setStips(job.getString("stips"));
//				info.setSflag_promise(job.getString("sflag_promise"));
//				info.setIname(job.getString("iname"));
				list.add(info);
			}
		} catch (JSONException e) {
		}
		return list;
	}

	// 被解析的json以及回调接口实现    
	public void getShopDetail(String mJson, DetailCallBack callback) {
		try {
			JSONObject job = new JSONObject(mJson);
			String result = job.getString("result");
			Log.e("result", "result:" + result);
			if (result.equalsIgnoreCase("ok")) {
				Log.e("result", "result:" + result);
				String signValue = job.getString("sign");
				String commentsValue = job.getString("comments");
				String foodValue = job.getString("food");
				JSONArray signArray = new JSONArray(signValue);
				JSONArray commentsArray = new JSONArray(commentsValue);
				JSONArray foodArray = new JSONArray(foodValue);
				for (int i = 0; i < foodArray.length(); i++) {
					JSONObject sJob = foodArray.getJSONObject(i);
					FoodModel info = new FoodModel();
//					info.setfid(sJob.getString("foodid"));
//					info.setSid(sJob.getString("sid"));
//					info.setf_name(sJob.getString("foodname"));
//					info.setImgSrc(sJob.getString("foodphotoid"));
					FoodList.add(info);
				}
				for (int i = 0; i < commentsArray.length(); i++) {
					JSONObject sJob = commentsArray.getJSONObject(i);
					CommentsInfo info = new CommentsInfo();
					info.setCid(sJob.getString("cid"));
					info.setSid(sJob.getString("sid"));
					info.setPid(sJob.getString("pid"));
					info.setName(sJob.getString("name"));
					info.setTime(sJob.getString("time"));
					info.setComments(sJob.getString("comments"));
					info.setClevel(sJob.getString("clevel"));
					info.setKouweilevel(sJob.getString("kouweilevel"));
					info.setHuanjinglevel(sJob.getString("huanjinglevel"));
					info.setFuwulevel(sJob.getString("fuwulevel"));
					info.setCpermoney(sJob.getString("cpermoney"));
					CommentsList.add(info);
				}
				for (int i = 0; i < signArray.length(); i++) {
					JSONObject sJob = signArray.getJSONObject(i);
					SignInfo info = new SignInfo();
//					info.setSignid(sJob.getString("signid"));
//					info.setSid(sJob.getString("sid"));
//					info.setPid(sJob.getString("pid"));
//					info.setName(sJob.getString("name"));
//					info.setSigncontent(sJob.getString("signcontent"));
//					info.setSignlevel(sJob.getString("signlevel"));
//					info.setSignimage(sJob.getString("signimage"));
//					info.setSigntime(sJob.getString("signtime"));
					SignList.add(info);
				}
				Log.e("result", "SignList:" + SignList.size() + " CommentsList"
						+ CommentsList.size() + " FoodList" + FoodList.size());
				callback.getList(SignList, CommentsList, FoodList);
			} else {
				callback.getList(SignList, CommentsList, FoodList);
			}
		} catch (JSONException e) {
			callback.getList(SignList, CommentsList, FoodList);
		}
	}

	//签到信息的解析
	public List<SignInfo> getSignList(String value) {
		List<SignInfo> list = new ArrayList<SignInfo>();
		try {
			JSONArray jay = new JSONArray(value);
			for (int i = 0; i < jay.length(); i++) {
				JSONObject job = jay.getJSONObject(i);
				SignInfo info = new SignInfo();
//				info.setName(job.getString("name"));
//				info.setSigncontent(job.getString("signcontent"));
//				info.setSignimage(job.getString("signimage"));
//				info.setSignlevel(job.getString("signlevel"));
//				info.setSigntime(job.getString("signtime"));
				list.add(info);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public interface DetailCallBack {
		public void getList(ArrayList<SignInfo> SignList,
				ArrayList<CommentsInfo> CommentsList,
				ArrayList<FoodModel> FoodList);
	}
}
