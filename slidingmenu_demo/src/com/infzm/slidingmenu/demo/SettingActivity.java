package com.infzm.slidingmenu.demo;

import android.os.Bundle;
import android.view.View;

public class SettingActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		initView();
	}

	@Override
	protected void initView() {
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		mBtnTitleMiddle.setText("设置");
	}

	@Override
	protected void initData() {
		
	}


}
