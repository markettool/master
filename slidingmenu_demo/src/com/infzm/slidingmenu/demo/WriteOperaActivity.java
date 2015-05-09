package com.infzm.slidingmenu.demo;

import android.os.Bundle;
import android.view.View;

public class WriteOperaActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_writeopera);
		initView();
		initData();
	}

	@Override
	protected void initView() {
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("我爱乱弹");
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

}
