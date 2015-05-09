package com.infzm.slidingmenu.demo;

import android.os.Bundle;
import android.view.View;

public class AccountActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		
		initView();
	}

	@Override
	protected void initView() {
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("我的账户");
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

}
