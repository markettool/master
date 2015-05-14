package org.markettool.opera;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.banner.AdViewListener;

import org.markettool.opera.adapter.CommentAdapter;
import org.markettool.opera.beans.CommentBean;
import org.markettool.opera.beans.MyUser;
import org.markettool.opera.beans.OperaBean;
import org.markettool.opera.utils.BitmapUtil;
import org.markettool.opera.utils.FileUtils;
import org.markettool.opera.view.RefreshableView;
import org.markettool.opera.view.RefreshableView.PullToRefreshListener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class CommentActivity extends BaseActivity {
	
	private EditText etComment;
	private Button btSubmit;
	private OperaBean operaBean;
	private ListView lv;
	private RefreshableView mRefreshableView;
	private RelativeLayout mAdContainer;
	private TextView tvOpera;
	
	private int skip;
	
	public static final int FINISH_REFRESHING=0;
	public static final int FINISH_LOADING=1;
	
	private List<CommentBean> commentBeans=new ArrayList<CommentBean>();
	
	private CommentAdapter adapter=null;
	
	private MyUser myuser;
	
	private int screenWidth;
	private int screenHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		
		initView();
		setAdapter();
		
		setListeners();
		initData();
		queryComments(FINISH_REFRESHING);
		showBanner();
	}

	private void setListeners(){
		btSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String comment=etComment.getText().toString();
						
				if(TextUtils.isEmpty(comment)){
					toastMsg("输入为空");
					return;
				}
				writeComment(comment, operaBean.getObjectId());
			}
		});
		
        mRefreshableView.setOnRefreshListener(new PullToRefreshListener() {
			
			@Override
			public void onRefresh() {
				Log.e("majie", "refresh");
				commentBeans.clear();
				skip=0;
				queryComments(FINISH_REFRESHING);
			}
		}, 1, false);
		
//		mRefreshableView.setOnLoadListener(new PullToLoadListener() {
//			
//			@Override
//			public void onLoad() {
//				queryComments(FINISH_LOADING);
//			}
//		});
	}
	
	@Override
	protected void initView() {

		etComment=(EditText) findViewById(R.id.et_comment);
		btSubmit=(Button) findViewById(R.id.submit);
		mAdContainer = (RelativeLayout) findViewById(R.id.adcontainer);
		tvOpera=(TextView) findViewById(R.id.tv_opera);
		
		lv=(ListView) findViewById(R.id.lv);
		mRefreshableView=(RefreshableView) findViewById(R.id.refreshableview);
		
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("发表评论");
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

		operaBean=(OperaBean) getIntent().getSerializableExtra("operaBean");
		String operaText=operaBean.getOperaContent();
		tvOpera.setText(operaText);
		myuser=BmobUser.getCurrentUser(this, MyUser.class);
		if(myuser==null){
			startActivity(LoginActivity.class);
			finish();
		}
		MyApplication app=(MyApplication)getApplication();
		screenWidth=app.getScreenWidth();
		screenHeight=app.getScreenHeight();
		String dir=FileUtils.getSDCardRoot()+getPackageName()+File.separator+"opera"+File.separator;
		String operaPicPath=dir+operaBean.getObjectId();
	    File operaFile=new File(operaPicPath);
	    if(operaFile.exists()){
				Bitmap bitmap = BitmapUtil.getOriginBitmap(operaPicPath);
				
				if(bitmap!=null){
					int height=Math.min(bitmap.getHeight()*screenWidth/bitmap.getWidth(),screenHeight/3);
					bitmap=Bitmap.createScaledBitmap(bitmap, screenWidth, height, false);
					tvOpera.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, height));
					tvOpera.setBackgroundDrawable(new BitmapDrawable(bitmap));
					tvOpera.setTextColor(getResources().getColor(R.color.white));
				}
	    }
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		myuser=BmobUser.getCurrentUser(this, MyUser.class);
	}
	
    private void writeComment(String comment,String operaId) {
		
		final CommentBean p2 = new CommentBean();
		p2.setComment(comment);
		p2.setOperaId(operaId);
		
		p2.setUsername(myuser.getUsername());
		p2.save(this, new SaveListener() {

			@Override
			public void onSuccess() {
				Log.d("bmob", "success  " );
				toastMsg("发表成功");
				etComment.setText("");
				updateComment(operaBean);
				if(commentBeans.size()==0){
					queryComments(FINISH_REFRESHING);
				}
//				finish();
			}

			@Override
			public void onFailure(int code, String msg) {
				toastMsg("创建数据失败：" + msg);
			}
		});
	} 
    
    private void queryComments(final int handle){
		BmobQuery<CommentBean> bmobQuery	 = new BmobQuery<CommentBean>();
//		bmobQuery.setLimit(10);
		bmobQuery.addWhereEqualTo("operaId", operaBean.getObjectId());
//		bmobQuery.order("-likeNum");
//		bmobQuery.setSkip(skip);
		bmobQuery.findObjects(this, new FindListener<CommentBean>() {

			@Override
			public void onSuccess(List<CommentBean> object) {
				Log.e("majie", "查询成功：共"+object.size()+"条数据。");
				skip+=object.size();
				commentBeans.addAll(object);
				
				mHandler.sendEmptyMessage(handle);
			}

			@Override
			public void onError(int code, String msg) {
				Log.e("majie","查询失败："+msg);
				mHandler.sendEmptyMessage(handle);
			}
		});
	}
    
    private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case FINISH_REFRESHING:
				mRefreshableView.finishRefreshing();
				
				break;

			case FINISH_LOADING:
				mRefreshableView.finishLoading();
				if(skip+2<commentBeans.size()){
					lv.setSelection(skip+2);
				}
				
				break;
			}
			adapter.notifyDataSetChanged();
			
		};
	};
	
	private void setAdapter(){
		adapter=new CommentAdapter(this, commentBeans);
		lv.setAdapter(adapter);
	}
	
	/**
	 * 更新对象
	 */
	private void updateComment(OperaBean bean) {
		final OperaBean p2 = new OperaBean();
		p2.setCommentNum(bean.getCommentNum()+1);
		p2.update(this, bean.getObjectId(), new UpdateListener() {

			@Override
			public void onSuccess() {
				Log.e("majie", "更新成功：" + p2.getUpdatedAt());
			}

			@Override
			public void onFailure(int code, String msg) {
				Log.e("majie", "更新失败：" + msg);
			}
		});

	}
	
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
