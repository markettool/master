package org.markettool.opera;

import org.markettool.opera.beans.OperaBean;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class OthersDataActivity extends BaseActivity {
	private TextView userage,usersex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_others_data);
		
		initView();
		setListeners();
		initData();
	}

	@Override
	protected void initView() {
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
//		mBtnTitleMiddle.setText("我的资料");
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		
		mImgLeft.setVisibility(View.VISIBLE);
		mImgLeft.setBackgroundResource(R.drawable.bt_back_dark);
		
		userage=(TextView) findViewById(R.id.userage);
		usersex=(TextView) findViewById(R.id.user_sex);
		
	}

	@Override
	protected void initData() {

		Intent intent=getIntent();
		String username=intent.getStringExtra("username");
		if(username!=null){
			mBtnTitleMiddle.setText(username);
		}
	}
	
	private void setListeners(){
		
	    mImgLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	    
	}
}
