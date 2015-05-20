package org.markettool.opera.beans;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;

public class MyBmobFile extends BmobFile {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String localFilePath;
	private int index;
	
	public MyBmobFile(File file){
		super(file);
	}
	
	public String getLocalFilePath() {
		return localFilePath;
	}

	public void setLocalFilePath(String localFilePath) {
		this.localFilePath = localFilePath;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}


}
