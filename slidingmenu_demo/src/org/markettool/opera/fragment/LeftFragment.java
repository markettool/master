package org.markettool.opera.fragment;

import org.markettool.opera.AccountActivity;
import org.markettool.opera.LoginActivity;
import org.markettool.opera.MainActivity;
import org.markettool.opera.MyDataActivity;
import org.markettool.opera.R;
import org.markettool.opera.SettingActivity;
import org.markettool.opera.beans.MyUser;
import org.markettool.opera.utils.BitmapUtil;

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
import android.widget.Toast;
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
		if(myUser!=null&&myUser.getFilePath()!=null){
			Bitmap b=BitmapUtil.decodeBitmap(myUser.getFilePath());
			if(b!=null){
				avatarPic.setImageBitmap(b);
			}else{
				avatarPic.setImageResource(R.drawable.wwj_748);
			}
			
		}
		
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
		case R.id.tvLastlist:// 往期列表
			Toast.makeText(getActivity(), "此功能暂时不开放,敬请期待", Toast.LENGTH_SHORT).show();
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
//		case R.id.avatar_pic:
//			
//			break;
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
	
}
