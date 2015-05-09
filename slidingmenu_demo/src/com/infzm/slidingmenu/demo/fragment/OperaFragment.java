package com.infzm.slidingmenu.demo.fragment;

import java.util.List;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.banner.AdViewListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.listener.FindListener;

import com.infzm.slidingmenu.demo.R;
import com.infzm.slidingmenu.demo.WriteOperaActivity;
import com.infzm.slidingmenu.demo.adapter.OperaAdapter;
import com.infzm.slidingmenu.demo.beans.OperaBean;
public class OperaFragment extends Fragment {
	public static final String PUBLISHER_ID = "56OJxFyIuN0CmR98Ua";
	public static final String InlinePPID = "16TLettoApHowNUdHoefcMUi";
	public static final String InterstitialPPID = "16TLwebvAchksY6iOa7F4DXs";
	public static final String SplashPPID = "16TLwebvAchksY6iOGe3xcik";
	
	private RelativeLayout mAdContainer;
	private ImageView btWrite;
	private ListView lv;
	
	private OperaAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		Log.e("majie", "oncreateview");
		View view = inflater.inflate(R.layout.frag_opera, null);
		mAdContainer = (RelativeLayout) view.findViewById(R.id.adcontainer);
		btWrite=(ImageView) view.findViewById(R.id.btn_write);
		lv=(ListView) view.findViewById(R.id.lv);
		setListeners();
		showBanner();
		queryOperas();
		return view;
	}
	
	private void setListeners(){
		btWrite.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().startActivityForResult(new Intent(getActivity(), WriteOperaActivity.class),0x01);
			}
		});
	}
	
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		queryOperas();
//	}
	
	private void showBanner() {

		// 广告条接口调用（适用于应用）
		// 将广告条adView添加到需要展示的layout控件中
		// LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
		// AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		// adLayout.addView(adView);

		// 广告条接口调用（适用于游戏）

		// 实例化LayoutParams(重要)
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		// 设置广告条的悬浮位置
//		layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT; // 这里示例为右下角
		// 实例化广告条
		AdView adView = new AdView(getActivity(), AdSize.FIT_SCREEN);
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
	
	private void queryOperas(){
		BmobQuery<OperaBean> bmobQuery	 = new BmobQuery<OperaBean>();
//		bmobQuery.addQueryKeys("objectId");
		bmobQuery.setLimit(10);
//		bmobQuery.order("createdAt");
//		bmobQuery.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);	// 先从缓存取数据，如果没有的话，再从网络取。
		bmobQuery.findObjects(getActivity(), new FindListener<OperaBean>() {

			@Override
			public void onSuccess(List<OperaBean> object) {
				Log.e("majie", "查询成功：共"+object.size()+"条数据。");
				setAdapter(object);
			}

			@Override
			public void onError(int code, String msg) {
				Log.e("majie","查询失败："+msg);
			}
		});
	}
	
	private void setAdapter(List<OperaBean> object){
		adapter=new OperaAdapter(getActivity(), object);
		lv.setAdapter(adapter);
	}
	
}
