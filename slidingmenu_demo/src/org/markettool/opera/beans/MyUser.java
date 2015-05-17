package org.markettool.opera.beans;

import java.util.List;

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
	private List<String> photoUrls;
//	private BmobFile photo_1;
//	private BmobFile photo_2;
//	private BmobFile photo_3;
//	private BmobFile photo_4;
	
//	public BmobFile getPhoto_1() {
//		return photo_1;
//	}
//	public void setPhoto_1(BmobFile photo_1) {
//		this.photo_1 = photo_1;
//	}
//	public BmobFile getPhoto_2() {
//		return photo_2;
//	}
//	public void setPhoto_2(BmobFile photo_2) {
//		this.photo_2 = photo_2;
//	}
//	public BmobFile getPhoto_3() {
//		return photo_3;
//	}
//	public void setPhoto_3(BmobFile photo_3) {
//		this.photo_3 = photo_3;
//	}
//	public BmobFile getPhoto_4() {
//		return photo_4;
//	}
//	public void setPhoto_4(BmobFile photo_4) {
//		this.photo_4 = photo_4;
//	}
	private float fund;
	private int like;
	private int comment;
	
	private String filePath;
	
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
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public List<String> getPhotoUrls() {
		return photoUrls;
	}
	public void setPhotoUrls(List<String> photoUrls) {
		this.photoUrls = photoUrls;
	}
	
}
