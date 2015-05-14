package org.markettool.opera.fragment;

import java.io.File;

import org.markettool.opera.AccountActivity;
import org.markettool.opera.LoginActivity;
import org.markettool.opera.MainActivity;
import org.markettool.opera.MyDataActivity;
import org.markettool.opera.R;
import org.markettool.opera.SettingActivity;
import org.markettool.opera.beans.MyUser;
import org.markettool.opera.utils.BitmapUtil;
import org.markettool.opera.utils.FileDownloader;
import org.markettool.opera.utils.FileDownloader.IDownloadProgress;
import org.markettool.opera.utils.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
/**
 * @date 2014/11/14
 * @author wuwenjie
 * @description 侧边栏菜单
 */
public class LeftFragment extends Fragment implements OnClickListener{
	private View todayView;
	private View lastListView;
	private View settingsView;
	private RelativeLayout myData;
	private TextView username;
	private ImageView avatarPic;
	
	private MyUser myUser;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_menu, null);
		findViews(view);
		downloadUserPic();
		return view;
	}
	
	
	public void findViews(View view) {
		todayView = view.findViewById(R.id.tvToday);
		lastListView = view.findViewById(R.id.tvLastlist);
		settingsView = view.findViewById(R.id.tvMySettings);
		myData=(RelativeLayout) view.findViewById(R.id.my_data);
		username=(TextView) view.findViewById(R.id.user_name);
		avatarPic=(ImageView) view.findViewById(R.id.avatar_pic);
		avatarPic.setOnClickListener(this);
		todayView.setOnClickListener(this);
		lastListView.setOnClickListener(this);
		settingsView.setOnClickListener(this);
		myData.setOnClickListener(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		myUser = BmobUser.getCurrentUser(getActivity(),MyUser.class);
		setAvatarImage();
		refresh();
	};
	
	private void refresh(){
		if(myUser!=null){
			 username.setText(myUser.getUsername());
		}
		else{
			username.setText("未登录");
		}
	}
	
	@Override
	public void onClick(View v) {
		Fragment newContent = null;
		String title = null;
		switch (v.getId()) {
		case R.id.tvToday: // account
			getActivity().startActivity(new Intent(getActivity(), AccountActivity.class));
			break;
		case R.id.tvLastlist:// share
//			Toast.makeText(getActivity(), "此功能暂时不开放,敬请期待", Toast.LENGTH_SHORT).show();
			onClickShare();
			break;
		case R.id.tvMySettings: // 设置
			getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
			break;
		case R.id.my_data:
			if(myUser==null){
				getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
			}else{
				getActivity().startActivity(new Intent(getActivity(), MyDataActivity.class));
			}
			break;
		case R.id.avatar_pic:
			
			break;
		default:
			break;
		}
		if (newContent != null) {
			switchFragment(newContent, title);
		}
	}
	
	/**
	 * 切换fragment
	 * @param fragment
	 */
	private void switchFragment(Fragment fragment, String title) {
		if (getActivity() == null) {
			return;
		}
		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.switchContent(fragment, title);
		}
	}
	
	private void downloadUserPic(){
		String dir=FileUtils.getSDCardRoot()+getActivity().getPackageName()+File.separator;
		File dirFile=new File(dir);
		if(!dirFile.exists()){
			dirFile.mkdirs();
		}
		MyUser myUser=BmobUser.getCurrentUser(getActivity(), MyUser.class);
		if(myUser==null){
			return;
		}
		File file=new File(myUser.getFilePath());
		if(file.exists()){
			return;
		}
		FileDownloader downloader=new FileDownloader();
		downloader.setFileUrl(myUser.getAvatar().getFileUrl(getActivity()));
		downloader.setSavePath(myUser.getFilePath());
		downloader.setProgressOutput(new IDownloadProgress() {
			
			@Override
			public void downloadSucess() {
				setAvatarImage();
			}
			
			@Override
			public void downloadProgress(float progress) {
				
			}
			
			@Override
			public void downloadFail() {
				
			}
		});
		downloader.start();
	}
	
	private void setAvatarImage(){
		if(myUser!=null&&myUser.getFilePath()!=null){
			try{
				Bitmap b=BitmapUtil.getOriginBitmap(myUser.getFilePath());
				if(b!=null){
					avatarPic.setImageBitmap(b);
				}else{
					avatarPic.setImageResource(R.drawable.wwj_748);
				}
			}catch(Exception e){
				
			}
			
		}
	}
	
	private void onClickShare() {  
		  
        Intent intent=new Intent(Intent.ACTION_SEND);   
        intent.setType("text/plain");   
        intent.putExtra(Intent.EXTRA_SUBJECT, "乱弹");   
        intent.putExtra(Intent.EXTRA_TEXT, "发表乱弹既可赚钱，速速下载!\n" +
        		"http://markettool-app.stor.sinaapp.com/opera.apk\n"+"请将链接复制到浏览器进行下载。");    
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
        startActivity(Intent.createChooser(intent, getActivity().getTitle()));   
  
    } 
	
}
