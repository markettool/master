package org.markettool.opera;

import org.markettool.opera.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;

public class SettingActivity extends BaseActivity{
	
	private TextView tvAbout;
	private Button btLogout;
	
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
		btLogout=(Button) findViewById(R.id.logout);
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		mBtnTitleMiddle.setText("设置");
		
		mImgLeft.setVisibility(View.VISIBLE);
		mImgLeft.setBackgroundResource(R.drawable.bt_back_dark);
		mImgLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
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
		
		btLogout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BmobUser.logOut(SettingActivity.this);
				finish();
			}
		});
	}


}
