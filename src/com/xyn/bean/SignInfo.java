package com.xyn.bean;
/**
 * 签到数据封装类
 * */
public class SignInfo {

	private String b_id;
	private String u_id;
	private String u_name;
	private String b_content;
	private String b_img;
	private String b_time;
	private String b_likecount;
	private boolean liked;

	public boolean isLiked() {
		return liked;
	}
	public void setLiked(boolean b) {
		liked = b;
	}
	public String getb_likecount() {
		return b_likecount;
	}
	public void setb_likecount(String signtime) {
		this.b_likecount = signtime;
	}
	public String getu_name() {
		return u_name;
	}
	public void setu_name(String name) {
		this.u_name = name;
	}
	public String getb_id() {
		return b_id;
	}
	public void setb_id(String signid) {
		this.b_id = signid;
	}
	public String getu_id() {
		return u_id;
	}
	public void setu_id(String pid) {
		this.u_id = pid;
	}
	public String getb_content() {
		return b_content;
	}
	public void setb_content(String signcontent) {
		this.b_content = signcontent;
	}
	public String getb_img() {
		return b_img;
	}
	public void setb_img(String signimage) {
		this.b_img = signimage;
	}
	public String getb_time() {
		return b_time;
	}
	public void setb_time(String signtime) {
		this.b_time = signtime;
	}



}
