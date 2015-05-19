package org.markettool.opera.fragment;

import java.util.ArrayList;
import java.util.List;

import org.markettool.opera.CommentActivity;
import org.markettool.opera.R;
import org.markettool.opera.WriteOperaActivity;
import org.markettool.opera.adapter.OperaAdapter;
import org.markettool.opera.beans.OperaBean;
import org.markettool.opera.utils.BitmapHelp;
import org.markettool.opera.utils.ProgressUtil;
import org.markettool.opera.view.RefreshableView;
import org.markettool.opera.view.RefreshableView.PullToLoadListener;
import org.markettool.opera.view.RefreshableView.PullToRefreshListener;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.lidroid.xutils.bitmap.PauseOnScrollListener;
public class OperaFragment extends Fragment {
	public static final String PUBLISHER_ID = "56OJxFyIuN0CmR98Ua";
	public static final String InlinePPID = "16TLettoApHowNUdHoefcMUi";
	public static final String InterstitialPPID = "16TLwebvAchksY6iOa7F4DXs";
	public static final String SplashPPID = "16TLwebvAchksY6iOGe3xcik";
	
	private RelativeLayout mAdContainer;
	private ImageView btWrite;
	private ListView lv;
	private RefreshableView mRefreshableView;
	
	private OperaAdapter adapter;
	
	public static final int FINISH_REFRESHING=0;
	public static final int FINISH_LOADING=1;
	
	private int focusSkip,nearSkip;
	private List<OperaBean> operaBeans=new ArrayList<OperaBean>();
	
	private int oldSize=0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_opera, null);
		mAdContainer = (RelativeLayout) view.findViewById(R.id.adcontainer);
		btWrite=(ImageView) view.findViewById(R.id.btn_write);
		lv=(ListView) view.findViewById(R.id.lv);
		mRefreshableView=(RefreshableView) view.findViewById(R.id.refreshableview);
		lv.setOnScrollListener(new PauseOnScrollListener(BitmapHelp.getBitmapUtils(getActivity()), false, true));
		setAdapter();
		setListeners();
		queryFocusOperas(FINISH_REFRESHING);
		return view;
	}
	
	private void setListeners(){
		btWrite.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().startActivityForResult(new Intent(getActivity(), WriteOperaActivity.class),0x01);
			}
		});
		
        mRefreshableView.setOnRefreshListener(new PullToRefreshListener() {
			
			@Override
			public void onRefresh() {
				Log.e("majie", "refresh");
				operaBeans.clear();
				focusSkip=0;
				nearSkip=0;
				queryFocusOperas(FINISH_REFRESHING);
			}
		}, 1, false);
		
		mRefreshableView.setOnLoadListener(new PullToLoadListener() {
			
			@Override
			public void onLoad() {
				queryFocusOperas(FINISH_LOADING);
			}
		});
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent=new Intent(getActivity(), CommentActivity.class);
				intent.putExtra("operaBean", operaBeans.get(arg2));
				getActivity().startActivity(intent);
			}
		});
	}
	
	private void queryFocusOperas(final int handle){
		synchronized (OperaFragment.this) {
			BmobQuery<OperaBean> focusQuery	 = new BmobQuery<OperaBean>();
			focusQuery.order("-commentNum,-likeNum");
			focusQuery.setLimit(10);
			focusQuery.setSkip(focusSkip);
			focusQuery.findObjects(getActivity(), new FindListener<OperaBean>() {

				@Override
				public void onSuccess(List<OperaBean> object) {
					Log.e("majie", "查询成功：共"+object.size()+"条数据。");
					oldSize=operaBeans.size();
					focusSkip+=object.size();
					operaBeans.addAll(object);
//					queryNearOperas(handle);
					
					mHandler.sendEmptyMessage(handle);
				}

				@Override
				public void onError(int code, String msg) {
					Log.e("majie","查询失败："+msg);
					mHandler.sendEmptyMessage(handle);
				}
			});
		}
		
	}
	
//	private void queryNearOperas(final int handle){
//		BmobQuery<OperaBean> nearQuery	 = new BmobQuery<OperaBean>();
//		nearQuery.setLimit(5);
//		nearQuery.order("-updatedAt");
//		nearQuery.setSkip(nearSkip);
//		nearQuery.findObjects(getActivity(), new FindListener<OperaBean>() {
//
//			@Override
//			public void onSuccess(List<OperaBean> object) {
//				Log.e("majie", "查询成功：共"+object.size()+"条数据。");
//				oldSize=operaBeans.size();
//				nearSkip+=object.size();
//				operaBeans.addAll(object);
//				
//				mHandler.sendEmptyMessage(handle);
//			}
//
//			@Override
//			public void onError(int code, String msg) {
//				Log.e("majie","查询失败："+msg);
//				mHandler.sendEmptyMessage(handle);
//			}
//		});
//	}
	
	private void setAdapter(){
		adapter=new OperaAdapter(getActivity(), operaBeans);
		lv.setAdapter(adapter);
	}
	
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case FINISH_REFRESHING:
				mRefreshableView.finishRefreshing();
				break;

			case FINISH_LOADING:
				mRefreshableView.finishLoading();
				if(oldSize+1<operaBeans.size()){
					lv.setSelection(oldSize+1);
				}
				break;
				
			}
			synchronized (OperaFragment.this) {
				adapter.notifyDataSetChanged();
			}
			
		};
	};
	
}
