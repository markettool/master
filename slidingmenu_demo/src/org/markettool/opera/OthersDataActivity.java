package org.markettool.opera;

import java.io.File;
import java.util.List;

import org.markettool.opera.beans.MyUser;
import org.markettool.opera.utils.FileUtils;
import org.markettool.opera.view.AlbumView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;

public class OthersDataActivity extends BaseActivity {
	private TextView userage,usersex;
	private AlbumView albumView;
	private MyUser othersUser;
	private List<BmobFile>  bmobFiles;
	private String dir;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_others_data);
		
		initView();
		setListeners();
		initData();
	}

	@Override
	protected void initView() {
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		
		mImgLeft.setVisibility(View.VISIBLE);
		mImgLeft.setBackgroundResource(R.drawable.bt_back_dark);
		
		userage=(TextView) findViewById(R.id.userage);
		usersex=(TextView) findViewById(R.id.user_sex);
		albumView=(AlbumView) findViewById(R.id.albumview);
		albumView.setIsCanAdd(false);
	}

	@Override
	protected void initData() {

		Intent intent=getIntent();
		String username=intent.getStringExtra("username");
		dir=FileUtils.getSDCardRoot()+"opera"+File.separator+"others"+File.separator;
	    FileUtils.mkdirs(dir);
		if(username!=null){
			mBtnTitleMiddle.setText(username);
			findBmobUser(username);
		}
	}
	
	/**
	 * 查询用户
	 */
	private void findBmobUser(String username) {
		BmobQuery<MyUser> query = new BmobQuery<MyUser>();
		query.addWhereEqualTo("username", username);
		query.findObjects(this, new FindListener<MyUser>() {

			@Override
			public void onSuccess(List<MyUser> object) {
//				toastMsg("查询用户成功：" + object.size());

				if(object.size()!=0){
					othersUser=object.get(0);
					userage.setText(othersUser.getAge()+"岁");
					if(othersUser.getGender()){
						usersex.setText("男");
					}else{
						usersex.setText("女");
					}
					bmobFiles=othersUser.getBmobFiles();
					if(bmobFiles!=null&&bmobFiles.size()!=0){
						albumView.setVisibility(View.VISIBLE);
						for(BmobFile file:bmobFiles){
							albumView.addData(file);
						}
					}
					
				}
			}

			@Override
			public void onError(int code, String msg) {
//				toastMsg("查询用户失败：" + msg);
			}
		});
	}
	
	private void setListeners(){
		
	    mImgLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	    
	}
	
//	private void downloadUserPhotos(int index){
//		if(index>=bmobFiles.size()){
//			return;
//		}
//		final int tem=index+1;
//		
//	    String url=bmobFiles.get(index).getFileUrl(this);
//	    String savePath=dir+bmobFiles.get(index).getFilename();
//	    if(new File(savePath).exists()||url==null){
//	    	albumView.notifyDataSetChanged();
//	    	downloadUserPhotos(tem);
//	    	return;
//	    }
//		FileDownloader downloader=new FileDownloader();
//	    downloader.setFileUrl(url);
//	    downloader.setSavePath(savePath);
//	    downloader.setProgressOutput(new IDownloadProgress() {
//			
//			@Override
//			public void downloadSucess() {
//				downloadUserPhotos(tem);
//				albumView.notifyDataSetChanged();
//			}
//			
//			@Override
//			public void downloadProgress(float progress) {
//				
//			}
//			
//			@Override
//			public void downloadFail() {
//				
//			}
//		});
//	    downloader.start();	    
//	}
}
