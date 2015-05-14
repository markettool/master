package org.markettool.opera;

import java.io.File;

import net.youmi.android.AdManager;

import org.markettool.opera.beans.MyUser;
import org.markettool.opera.fragment.LeftFragment;
import org.markettool.opera.fragment.OperaFragment;
import org.markettool.opera.utils.FileDownloader;
import org.markettool.opera.utils.FileDownloader.IDownloadProgress;
import org.markettool.opera.utils.FileUtils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * @date 2014/11/14
 * @author wuwenjie
 * @description 主界面
 */
public class MainActivity extends SlidingFragmentActivity implements
		OnClickListener {
	
	/**
	 * SDK初始化建议放在启动页
	 */
	public static String APPID = "61cac84cd9fe5268b0ff9ff054236373";

	private ImageView topButton;
	private Fragment mContent;
	private TextView topTextView;
	
	private LeftFragment leftFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Bmob.initialize(getApplicationContext(),APPID);
		 AdManager.getInstance(this).init("8fc8db6c25bac8e1",
				 "b9aee7b0b8ce4419", false);
		 

		topButton = (ImageView) findViewById(R.id.topButton);
		topButton.setOnClickListener(this);
		topTextView = (TextView) findViewById(R.id.topTv);
		
		initSlidingMenu(savedInstanceState);
		
		updateVersion();
	}
	
	private void updateVersion(){
//		BmobUpdateAgent.initAppVersion(this);
		BmobUpdateAgent.update(this);
		BmobUpdateAgent.setUpdateOnlyWifi(false);
		 BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {

			 @Override
			 public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
			     // TODO Auto-generated method stub
			     //根据updateStatus来判断更新是否成功
				 Log.e("majie","updateStatus "+ updateStatus);
			 }
			});
	}

	/**
	 * 初始化侧边栏
	 */
	private void initSlidingMenu(Bundle savedInstanceState) {
		// 如果保存的状态不为空则得到之前保存的Fragment，否则实例化MyFragment
		if (savedInstanceState != null) {
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		}

		if (mContent == null) {
			mContent = new OperaFragment();
			switchContent(mContent, "乱弹");
		}

		// 设置左侧滑动菜单
		setBehindContentView(R.layout.menu_frame_left);
		leftFragment=new LeftFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, leftFragment).commit();

		// 实例化滑动菜单对象
		SlidingMenu sm = getSlidingMenu();
		// 设置可以左右滑动的菜单
		sm.setMode(SlidingMenu.LEFT);
		// 设置滑动阴影的宽度
		sm.setShadowWidthRes(R.dimen.shadow_width);
		// 设置滑动菜单阴影的图像资源
		sm.setShadowDrawable(null);
		// 设置滑动菜单视图的宽度
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		sm.setFadeDegree(0.35f);
		// 设置触摸屏幕的模式,这里设置为全屏
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// 设置下方视图的在滚动时的缩放比例
		sm.setBehindScrollScale(0.0f);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}

	/**
	 * 切换Fragment
	 * 
	 * @param fragment
	 */
	public void switchContent(Fragment fragment, String title) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		getSlidingMenu().showContent();
		topTextView.setText(title);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topButton:
			toggle();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
//		String dir=FileUtils.getSDCardRoot()+getPackageName()+File.separator+"opera"+File.separator;
//	    FileUtils.delete(dir);
	}
	
}
