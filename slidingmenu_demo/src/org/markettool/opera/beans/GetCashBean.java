package org.markettool.opera.beans;

import cn.bmob.v3.BmobObject;

public class GetCashBean extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String creditInfo;
	private float fund;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public float getFund() {
		return fund;
	}
	public void setFund(float fund) {
		this.fund = fund;
	}
	public String getCreditInfo() {
		return creditInfo;
	}
	public void setCreditInfo(String creditInfo) {
		this.creditInfo = creditInfo;
	}

}
