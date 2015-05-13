package org.markettool.opera.beans;

import cn.bmob.v3.BmobObject;

public class CommentBean extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String comment;
	
	private String operaId;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getOperaId() {
		return operaId;
	}

	public void setOperaId(String operaId) {
		this.operaId = operaId;
	}


}
