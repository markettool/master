package com.infzm.slidingmenu.demo;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SettingActivity extends BaseActivity{
	
	private TextView tvAbout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		initView();
		setListeners();
	}

	@Override
	protected void initView() {
		tvAbout=(TextView) findViewById(R.id.tvAbout);
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		mBtnTitleMiddle.setText("设置");
	}

	@Override
	protected void initData() {
		
	}
	
	private void setListeners(){
		tvAbout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(AboutActivity.class);
			}
		});
	}


}
