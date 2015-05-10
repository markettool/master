package org.markettool.opera;

import org.markettool.opera.R;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends BaseActivity {
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
				String name = username.getText().toString();
				String psw = userpsw.getText().toString();
				
				login(name, psw);
			}
		});

		register = (TextView) findViewById(R.id.register);
		register.setText(Html.fromHtml("<u> 用户注册 </u>"));
		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(RegisterActivity.class);
				finish();
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
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 登陆用户
	 */
	private void login(String name,String psw) {
		final BmobUser bu2 = new BmobUser();
		bu2.setUsername(name);
		bu2.setPassword(psw);
		bu2.login(this, new SaveListener() {

			@Override
			public void onSuccess() {
				toastMsg(bu2.getUsername() + "登陆成功");
				finish();
			}

			@Override
			public void onFailure(int code, String msg) {
				toastMsg("登陆失败:" + msg);
				
			}
		});
	}
	
//	private void save(final String name,final int age,final boolean gender){
//		SharedPrefUtil spu=new SharedPrefUtil(this, "user");
//		spu.putValueByKey("name", name);
//		spu.putValueByKey("age", ""+age);
//		spu.putValueByKey("gender", ""+gender);
//	}

}
