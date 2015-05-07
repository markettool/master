package com.infzm.slidingmenu.demo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import cn.domob.android.ads.AdEventListener;
import cn.domob.android.ads.AdManager.ErrorCode;
import cn.domob.android.ads.AdView;

import com.infzm.slidingmenu.demo.R;
public class TodayFragment extends Fragment {
	public static final String PUBLISHER_ID = "56OJyM1ouMGoaSnvCK";
	public static final String InlinePPID = "16TLwebvAchksY6iO_8oSb-i";
	public static final String InterstitialPPID = "16TLwebvAchksY6iOa7F4DXs";
	public static final String SplashPPID = "16TLwebvAchksY6iOGe3xcik";
	
	RelativeLayout mAdContainer;
	AdView mAdview;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		Log.e("majie", "oncreate");
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		Log.e("majie", "oncreateview");
		View view = inflater.inflate(R.layout.frag_today, null);
		mAdContainer = (RelativeLayout) view.findViewById(R.id.adcontainer);
//		addAdView();
		return view;
	}
	
	private void addAdView(){
		
		// Create ad view
		mAdview = new AdView(getActivity(), PUBLISHER_ID, InlinePPID);
		mAdview.setKeyword("game");
		mAdview.setUserGender("male");
		mAdview.setUserBirthdayStr("2000-08-08");
		mAdview.setUserPostcode("123456");
		mAdview.setAdEventListener(new AdEventListener() {
			@Override
			public void onAdOverlayPresented(AdView adView) {
				Log.i("DomobSDKDemo", "overlayPresented");
			}
			@Override
			public void onAdOverlayDismissed(AdView adView) {
				Log.i("DomobSDKDemo", "Overrided be dismissed");
			}
			@Override
			public void onAdClicked(AdView arg0) {
				Log.i("DomobSDKDemo", "onDomobAdClicked");
			}
			@Override
			public void onLeaveApplication(AdView arg0) {
				Log.i("DomobSDKDemo", "onDomobLeaveApplication");
			}
			@Override
			public Context onAdRequiresCurrentContext() {
				return getActivity();
			}
			@Override
			public void onAdFailed(AdView arg0, ErrorCode arg1) {
				Log.i("DomobSDKDemo", "onDomobAdFailed");
			}
			@Override
			public void onEventAdReturned(AdView arg0) {
				Log.i("DomobSDKDemo", "onDomobAdReturned");
			}
		});
		RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		layout.addRule(RelativeLayout.CENTER_HORIZONTAL);
		mAdview.setLayoutParams(layout);
		mAdContainer.addView(mAdview);
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
