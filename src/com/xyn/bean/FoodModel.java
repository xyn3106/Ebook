package com.xyn.bean;

import java.io.Serializable;

/**
 * 食物的bean,FoodModel
 * */
public class FoodModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private String fid;
	private String s_id;
	private String s_name;
	private String f_name;
	private String f_type;
	private String f_price;
	private String f_price2;
	private String f_commentcount;
	private String f_desc;
	private String c_name;
	private String f_date;
	private String f_score;
	private String imgSrc;
	private String longitude;
	private String latitude;
	public String getfid() {
		return fid;
	}
	public void setfid(String fid) {
		this.fid = fid;
	}
	public String getf_name() {
		return f_name;
	}
	public void setf_name(String f_name) {
		this.f_name = f_name;
	}
	public String getf_type() {
		return f_type;
	}
	public void setf_type(String f_type) {
		this.f_type = f_type;
	}
	public String getc_name() {
		return c_name;
	}
	public void setc_name(String c_id) {
		this.c_name = c_id;
	}
	public String getf_commentcount() {
		return f_commentcount;
	}
	public void setf_commentcount(String s) {
		this.f_commentcount = s;
	}
	public String getf_desc() {
		return f_desc;
	}
	public void setf_desc(String f_desc) {
		this.f_desc = f_desc;
	}
	public String getf_date() {
		return f_date;
	}
	public void setf_date(String f_date) {
		this.f_date = f_date;
	}
	public String getf_price() {
		return f_price;
	}
	public void setf_price(String f_price) {
		this.f_price = f_price;
	}
	public String getf_price2() {
		return f_price2;
	}
	public void setf_price2(String f_price) {
		this.f_price2 = f_price;
	}
	public String getImgSrc()
	{
		return imgSrc;
	}

	public void setImgSrc(String imgSrc)
	{
		this.imgSrc = imgSrc;
	}
	public String getf_score() {
		return f_score;
	}
	public void setf_score(String slevel) {
		this.f_score = slevel;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public void setSid(String string) {
		this.s_id=string;
	}
	public String getSid() {
		return s_id;
	}
	public void sets_name(String string) {
		this.s_name=string;
	}
	public String gets_name() {
		return s_name;
	}
}
