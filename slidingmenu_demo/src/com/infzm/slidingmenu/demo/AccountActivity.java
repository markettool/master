package com.infzm.slidingmenu.demo;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.infzm.slidingmenu.demo.beans.MyUser;
import com.infzm.slidingmenu.demo.beans.OperaBean;

public class AccountActivity extends BaseActivity {
	
	private TextView tvTotalFund;
	private Button btGetCash;
	private MyUser myUser;
	private float totalFund;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		
		initView();
		setListeners();
		initData();
	}

	@Override
	protected void initView() {
		tvTotalFund=(TextView) findViewById(R.id.total_fund);
		btGetCash=(Button) findViewById(R.id.getcash);
		
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("我的账户");
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
		btGetCash.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(totalFund<10){
					toastMsg("低于10元不能体现");
					return;
				}else{
//					startActivity(c);
					Intent intent=new Intent(AccountActivity.this, GetCashActivity.class);
					intent.putExtra("fund", totalFund);
					startActivity(intent);
				}
			}
		});
	}

	@Override
	protected void initData() {
		myUser=BmobUser.getCurrentUser(this, MyUser.class);
		totalFund=myUser.getFund();
		queryOperas();
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		myUser=BmobUser.getCurrentUser(this, MyUser.class);
	}
	
	private void queryOperas(){
		BmobQuery<OperaBean> bmobQuery	 = new BmobQuery<OperaBean>();
		bmobQuery.addWhereEqualTo("username", myUser.getUsername());
//		bmobQuery.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);	// 先从缓存取数据，如果没有的话，再从网络取。
		bmobQuery.findObjects(this, new FindListener<OperaBean>() {

			@Override
			public void onSuccess(List<OperaBean> object) {
				Log.e("majie", "查询成功：共"+object.size()+"条数据。");
				staticic(object);
				tvTotalFund.setText("总收益："+totalFund+" 元");
				updateUserAccount();
			}

			@Override
			public void onError(int code, String msg) {
				Log.e("majie","查询失败："+msg);
			}
		});
	}
	
	private void staticic(List<OperaBean> object){
		List<BmobObject> bos=new ArrayList<BmobObject>();
		for(OperaBean bean:object){
			totalFund+=((bean.getLikeNum()-bean.getStatLikeNum())*0.05f+(bean.getCommentNum()-bean.getStatCommentNum())*0.1f);
			OperaBean bo=new OperaBean();
			bo.setLikeNum(bean.getLikeNum());
			bo.setCommentNum(bean.getCommentNum());
			bo.setObjectId(bean.getObjectId());
			bo.setStatLikeNum(bean.getLikeNum());
			bo.setStatCommentNum(bean.getCommentNum());
			bos.add(bo);
		}
		batchUpdateOperas(bos);
	}
	/**
	 * 批量更新
	 */
	private void batchUpdateOperas(List<BmobObject> object){
		new BmobObject().updateBatch(AccountActivity.this, object, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				Log.e("majie","批量更新成功");
			}
			
			@Override
			public void onFailure(int code, String msg) {
				Log.e("majie","批量更新失败:"+msg);
			}
		});
	}
	
	private void updateUserAccount() {
		if (myUser != null) {
			MyUser newUser = new MyUser();
			newUser.setFund(totalFund);
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


}
