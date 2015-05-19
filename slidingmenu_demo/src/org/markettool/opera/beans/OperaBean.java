package org.markettool.opera.beans;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class OperaBean extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BmobFile userPic;
	private String username;
	private String operaContent;
	private int likeNum;
	private int commentNum;
	private int statedLikeNum;
	private int statedCommentNum;
	private BmobFile operaPic;
	
	public int getStatLikeNum() {
		return statedLikeNum;
	}
	public void setStatLikeNum(int statLikeNum) {
		this.statedLikeNum = statLikeNum;
	}
	public int getStatCommentNum() {
		return statedCommentNum;
	}
	public void setStatCommentNum(int statCommentNum) {
		this.statedCommentNum = statCommentNum;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getOperaContent() {
		return operaContent;
	}
	public void setOperaContent(String operaContent) {
		this.operaContent = operaContent;
	}
	public int getLikeNum() {
		return likeNum;
	}
	public void setLikeNum(int likeNum) {
		this.likeNum = likeNum;
	}
	public int getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}
	public BmobFile getOperaPic() {
		return operaPic;
	}
	public void setOperaPic(BmobFile operaPic) {
		this.operaPic = operaPic;
	}
	public BmobFile getUserPic() {
		return userPic;
	}
	public void setUserPic(BmobFile userPic) {
		this.userPic = userPic;
	}

}
