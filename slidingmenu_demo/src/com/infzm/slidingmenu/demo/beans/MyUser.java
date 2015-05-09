package com.infzm.slidingmenu.demo.beans;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class MyUser extends BmobUser {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer age;
	private Boolean gender;
	// ...
	private BmobFile avatar;
	
	private float fund;
	private int like;
	private int comment;
	
	public  float getFund() {
		return fund;
	}
	public void setFund(float fund) {
		this.fund = fund;
	}
	public int getLike() {
		return like;
	}
	public void setLike(int like) {
		this.like = like;
	}
	public int getComment() {
		return comment;
	}
	public void setComment(int comment) {
		this.comment = comment;
	}
	public BmobFile getAvatar() {
		return avatar;
	}
	public void setAvatar(BmobFile avatar) {
		this.avatar = avatar;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Boolean getGender() {
		return gender;
	}
	public void setGender(Boolean gender) {
		this.gender = gender;
	}
}
