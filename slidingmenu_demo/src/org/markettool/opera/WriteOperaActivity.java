package org.markettool.opera;

import org.markettool.opera.R;
import org.markettool.opera.beans.MyUser;
import org.markettool.opera.beans.OperaBean;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class WriteOperaActivity extends BaseActivity {
	
	
	private EditText etOpera;
	private Button btPublish;
	private MyUser myUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_writeopera);
		initView();
		setListeners();
		initData();
	}

	@Override
	protected void initView() {
		etOpera=(EditText) findViewById(R.id.et_opera);
		btPublish=(Button) findViewById(R.id.btn_write);
		
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("我爱乱弹");
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
	
	private void setListeners(){
		btPublish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!TextUtils.isEmpty(etOpera.getText().toString())){
					
					writeOpera();
				}else{
					toastMsg("输入为空");
				}
			}
		});
	}

	@Override
	protected void initData() {

		myUser=BmobUser.getCurrentUser(this, MyUser.class);
		if(myUser==null){
			startActivity(LoginActivity.class);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		myUser=BmobUser.getCurrentUser(this, MyUser.class);
	}
	
	/**
	 * 插入对象
	 */
	private void writeOpera() {
		
		final OperaBean p2 = new OperaBean();
		p2.setUserId(myUser.getUsername()+new java.util.Date().getTime());
		p2.setUsername(myUser.getUsername());
		p2.setOperaContent(etOpera.getText().toString());
		p2.save(this, new SaveListener() {

			@Override
			public void onSuccess() {
				Log.d("bmob", "success  " );
				toastMsg("发表成功");
				finish();
			}

			@Override
			public void onFailure(int code, String msg) {
				toastMsg("创建数据失败：" + msg);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		setResult(0x01);
	}

}
