package com.infzm.slidingmenu.demo;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends BaseActivity {
	private Dialog dialog;
	private EditText username, userpsw;
	private Button signin;
	private TextView register;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		initView();
		setListeners();
		
		initData();
	}

    private void setListeners(){
    	signin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		register = (TextView) findViewById(R.id.register);
		register.setText(Html.fromHtml("<u> 用户注册 </u>"));
		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(RegisterActivity.class);
			}
		});
    }

	@Override
	protected void initView() {
		signin = (Button) findViewById(R.id.signin);
		username = (EditText) findViewById(R.id.username);
		userpsw = (EditText) findViewById(R.id.userpsw);
		
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("用户登录");
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
	}



	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}

}
