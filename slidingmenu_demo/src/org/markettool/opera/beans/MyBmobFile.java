package org.markettool.opera.beans;

import cn.bmob.v3.datatype.BmobFile;

public class MyBmobFile extends BmobFile {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String localFilePath;
	
//	public MyBmobFile(BmobFile file){
//	}

	public String getLocalFilePath() {
		return localFilePath;
	}

	public void setLocalFilePath(String localFilePath) {
		this.localFilePath = localFilePath;
	}


}
