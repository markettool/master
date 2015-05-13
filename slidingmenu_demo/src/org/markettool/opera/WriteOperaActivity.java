package org.markettool.opera;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.banner.AdViewListener;

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
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class WriteOperaActivity extends BaseActivity {
	
	
	private EditText etOpera;
	private Button btPublish;
	private MyUser myUser;
	private RelativeLayout mAdContainer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_writeopera);
		initView();
		setListeners();
		initData();
		showBanner();
	}

	@Override
	protected void initView() {
		etOpera=(EditText) findViewById(R.id.et_opera);
		btPublish=(Button) findViewById(R.id.btn_write);
		mAdContainer = (RelativeLayout) findViewById(R.id.adcontainer);
		
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
			finish();
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
		
		final OperaBean p = new OperaBean();
		if(myUser.getAvatar()!=null){
			p.setUserPicPath(myUser.getAvatar().getFileUrl(this));
		}
		p.setUsername(myUser.getUsername());
		p.setOperaContent(etOpera.getText().toString());
		p.save(this, new SaveListener() {

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
	
	private void showBanner() {

		// 实例化LayoutParams(重要)
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		// 设置广告条的悬浮位置
//		layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT; // 这里示例为右下角
		// 实例化广告条
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		// 调用Activity的addContentView函数

		// 监听广告条接口
		adView.setAdListener(new AdViewListener() {

			@Override
			public void onSwitchedAd(AdView arg0) {
				Log.i("YoumiAdDemo", "广告条切换");
			}

			@Override
			public void onReceivedAd(AdView arg0) {
				Log.i("YoumiAdDemo", "请求广告成功");

			}

			@Override
			public void onFailedToReceivedAd(AdView arg0) {
				Log.i("YoumiAdDemo", "请求广告失败");
			}
		});
//		this.addContentView(adView, layoutParams);
		mAdContainer.addView(adView,layoutParams);
	}


}
