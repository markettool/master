package org.markettool.opera;

import java.util.ArrayList;
import java.util.List;

import org.markettool.opera.beans.MyBmobFile;
import org.markettool.opera.beans.MyUser;
import org.markettool.opera.utils.BitmapUtil;
import org.markettool.opera.utils.FileUtils;
import org.markettool.opera.utils.ProgressUtil;
import org.markettool.opera.view.AlbumView;
import org.markettool.opera.view.AlbumView.onHandleListener;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;

public class MyDataActivity extends BaseActivity {
//	public int PICK_USER_PIC = 0;
	public int PICK_USER_PHOTO=1;
	private EditText username, userage;
	private RadioGroup group;
	private boolean gender = true;
	private ImageView userimg;
	private LinearLayout pswLayout;
    private RadioButton maleRb,femaleRb;
    private AlbumView albumView;
    private MyUser myUser;
    private String dir;
    private String name;
    private int age;
    private int width;
    private int index;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mydata);
		
		initView();
		setListeners();
		
		initData();
	}

	@Override
	protected void initView() {
		
		username = (EditText) findViewById(R.id.username);
		userage = (EditText) findViewById(R.id.userage);
		userimg = (ImageView) findViewById(R.id.userimg);
		pswLayout=(LinearLayout) findViewById(R.id.psw_layout);
		pswLayout.setVisibility(View.GONE);
		maleRb=(RadioButton) findViewById(R.id.male);
		femaleRb=(RadioButton) findViewById(R.id.female);
		group = (RadioGroup) findViewById(R.id.sex);
		albumView=(AlbumView) findViewById(R.id.albumview);
		
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("我的资料");
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		
		mImgLeft.setVisibility(View.VISIBLE);
		mImgLeft.setBackgroundResource(R.drawable.bt_back_dark);
		
		mBtnTitleRight.setVisibility(View.VISIBLE);
		mBtnTitleRight.setText("保存");
		mBtnTitleRight.setTextColor(getResources().getColor(R.color.white));
	}

	@Override
	protected void initData() {
		myUser = BmobUser.getCurrentUser(this, MyUser.class);
		username.setText(myUser.getUsername());
		username.setEnabled(false);
		userage.setText(""+myUser.getAge());
		boolean gender=myUser.getGender();
		if(gender){
			maleRb.setChecked(true);
		}else{
			femaleRb.setChecked(true);
		}	
		if(myUser.getBmobFiles()!=null&&myUser.getBmobFiles().size()!=0){
			BmobFile avatar=myUser.getBmobFiles().get(0);
			avatar.loadImageThumbnail(this, userimg, 60, 60);
		}
		
		getScreenSize();
		dir = FileUtils.PHOTO_PATH;
		FileUtils.mkdirs(dir);
		
		initAlbumView();
	}
	
	private void getScreenSize(){
		MyApplication app=(MyApplication)getApplication();
		int screenWidth=app.getScreenWidth();
		width=(screenWidth-40)/4;
	}
	
	private void initAlbumView(){
		List<BmobFile> bmobFiles=myUser.getBmobFiles();
		if(bmobFiles!=null&&bmobFiles.size()!=0){
			for(BmobFile file:bmobFiles){
				albumView.addData(file);
			}
		}
	}
	
	private void setListeners(){
		mBtnTitleRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				name = username.getText().toString();
				
				if (name.equals("") || userage.getText().toString().equals("")
						) {
					toastMsg("请填写基本资料");
					return;
				}
				age =new Integer(userage.getText().toString()) ;
				
				ProgressUtil.showProgress(MyDataActivity.this, "");
				if(albumView.getBmobFiles()!=null&&albumView.getBmobFiles().size()!=0){
					List<String> paths=new ArrayList<String>();
					for(BmobFile file:albumView.getBmobFiles()){
						if(file instanceof MyBmobFile){
							paths.add(((MyBmobFile) file).getLocalFilePath());
						}
					}
					if(paths.size()!=0){
						uploadPhotos(paths);
					}else{
						updateUser();
					}
					
				}else{
					updateUser();
				}

			}
		});

		
		group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.male) {
					gender = true;
				} else {
					gender = false;
				}

			}
		});

		userimg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getFileFromSD();
			}
		});
		
	    mImgLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	    
	    albumView.setOnHandleListener(new onHandleListener() {
			
			@Override
			public void onClick(int index) {
				MyDataActivity.this.index=index;
				getFileFromSD();
			}
		});
	}
	
	private void getFileFromSD() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);

		startActivityForResult(intent, PICK_USER_PHOTO);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Uri uri = data.getData();
			try {
				String[] pojo = { MediaStore.Images.Media.DATA };
				Cursor cursor = managedQuery(uri, pojo, null, null, null);
				if (cursor != null) {
					int colunm_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					String path = cursor.getString(colunm_index);

					saveThubPhoto(path, index);
					
				}

			} catch (Exception e) {
			}

		}
	}
	
	private void updateUser() {
		final MyUser bmobUser = BmobUser.getCurrentUser(this, MyUser.class);
		if (bmobUser != null) {
			MyUser newUser = new MyUser();
			newUser.setBmobFiles(myUser.getBmobFiles());;
			newUser.setAge(age);
			newUser.setGender(gender);
//			newUser.setFilePath(avatarPath);
			newUser.setObjectId(bmobUser.getObjectId());
			newUser.update(this,new UpdateListener() {

				@Override
				public void onSuccess() {
					ProgressUtil.closeProgress();
					toastMsg("更新用户信息成功");
					finish();
				}

				@Override
				public void onFailure(int code, String msg) {
					toastMsg("更新用户信息失败:" + msg);
					ProgressUtil.closeProgress();
				}
			});
		} 
	}
	
	private void uploadPhotos(final List<String> paths){
		String [] filePaths=new String[paths.size()];
		int i=0;
		for(String path:paths){
			filePaths[i]=path;
		    i++;
		}
		Bmob.uploadBatch(this, filePaths, new UploadBatchListener() {
			
			@Override
		    public void onSuccess(List<BmobFile> files,List<String> urls) {
//				Log.e("majie", "url ="+urls.size());
				if(urls.size()==paths.size()){
					if(myUser.getBmobFiles()!=null){
						myUser.getBmobFiles().addAll(files);
					}else{
						myUser.setBmobFiles(files);
					}
					
					updateUser();
					
				}
		    }

		    @Override
		    public void onError(int statuscode, String errormsg) {
		    	Log.e("majie", errormsg);
		    }

		    @Override
		    public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
		    }
		});
	}
	
	private void saveThubPhoto(final String path,final int position){
		new Thread(){
			public void run() {
				super.run();
				Bitmap bitmap=BitmapUtil.getThumbilBitmap(path, width);
				String thubPath=dir+myUser.getUsername()+"_photo_"+position+".png";
				BitmapUtil.saveBitmapToSdcard(bitmap, thubPath);
				Message message=new Message();
				message.obj=thubPath;
				handler.sendMessage(message);
			};
		}.start();
		
	}
	
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			MyBmobFile bmobFile=new MyBmobFile();
			bmobFile.setLocalFilePath((String) msg.obj);
			albumView.addData(bmobFile);
		};
	};
	
}
