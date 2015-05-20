package org.markettool.opera;

import org.markettool.opera.R;
import org.markettool.opera.beans.GetCashBean;
import org.markettool.opera.beans.MyUser;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class GetCashActivity extends BaseActivity {
	
	private EditText etCash;
//	private Button btSubmit;
	private MyUser myUser;
	private float fund;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_getcash);
		
		initView();
		setListeners();
		initData();
	}

	@Override
	protected void initView() {
		etCash=(EditText) findViewById(R.id.et_opera);
//		btSubmit=(Button) findViewById(R.id.btn_write);
		
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("确认提现");
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		
		mImgLeft.setVisibility(View.VISIBLE);
		mImgLeft.setBackgroundResource(R.drawable.bt_back_dark);
		
		mBtnTitleRight.setVisibility(View.VISIBLE);
		mBtnTitleRight.setText("提交");
		mBtnTitleRight.setTextColor(getResources().getColor(R.color.white));
	
	}
	
	private void setListeners(){
		mBtnTitleRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!TextUtils.isEmpty(etCash.getText().toString())){
					
					requeryGetCash();
				}else{
					toastMsg("输入为空");
				}
			}
		});
		
	    mImgLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
	
	/**
	 * 插入对象
	 */
	private void requeryGetCash() {
		
		final GetCashBean p = new GetCashBean();
		p.setCreditInfo(etCash.getText().toString());
		p.setUsername(myUser.getUsername());
		p.setFund(fund);
		p.save(this, new SaveListener() {

			@Override
			public void onSuccess() {
				Log.d("bmob", "success  " );
				toastMsg("体现成功");
				finish();
			}

			@Override
			public void onFailure(int code, String msg) {
				toastMsg("体现失败：" + msg);
			}
		});
	}
	
	private void updateUserAccount() {
		if (myUser != null) {
			MyUser newUser = new MyUser();
			newUser.setFund(0);
			newUser.setObjectId(myUser.getObjectId());
			newUser.update(this,new UpdateListener() {

				@Override
				public void onSuccess() {
//					toastMsg("更新用户信息成功");
				}

				@Override
				public void onFailure(int code, String msg) {
//					toastMsg("更新用户信息失败:" + msg);
				}
			});
		} 
	}

	@Override
	protected void initData() {
		myUser=BmobUser.getCurrentUser(this, MyUser.class);
		fund=getIntent().getFloatExtra("fund", 0);
	}

}
