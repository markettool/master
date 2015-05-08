package com.infzm.slidingmenu.demo;

import com.infzm.slidingmenu.demo.beans.MyUser;

import android.app.Application;

public class MyApplication extends Application {

	private MyUser user;

	public MyUser getUser() {
		return user;
	}

	public void setUser(MyUser user) {
		this.user = user;
	}
}
