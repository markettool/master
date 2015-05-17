package org.markettool.opera;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.markettool.opera.beans.MyUser;
import org.markettool.opera.utils.BitmapUtil;
import org.markettool.opera.utils.FileUtils;
import org.markettool.opera.view.AlbumView;
import org.markettool.opera.view.AlbumView.onHandleListener;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import cn.bmob.v3.listener.UploadFileListener;

public class MyDataActivity extends BaseActivity {
	public int PICK_USER_PIC = 0;
	public int PICK_USER_PHOTO=1;
	private EditText username, userage;
	private RadioGroup group;
	private boolean gender = true;
	private ImageView userimg;
	private LinearLayout pswLayout;
    private RadioButton maleRb,femaleRb;
    private String avatarPath;
    private AlbumView albumView;
    private MyUser myUser;
    private String dir;
    private String name;
    private int age;
//    private List<String> photoUrls;
	
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
		try{
			if(myUser.getFilePath()!=null){
				userimg.setImageBitmap(BitmapUtil.getOriginBitmap(myUser.getFilePath()));
			}
		}catch(Exception e){
			
		}
		
		dir = FileUtils.PHOTO_PATH;
		initAlbumView();
	}
	
	private void initAlbumView(){
		List<String > initialPaths=new ArrayList<String>();
		int i=0;
		
		while(i<4){
			String path=dir+myUser.getUsername()+"_photo_"+i+".png";
			initialPaths.add(path);
			i++;
		}
		albumView.setInitialPaths(initialPaths);
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
				
				if(albumView.getThubPaths().size()!=0){
					uploadPhotos(albumView.getThubPaths());
				}else{
					uploadOrUpdate();
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
				getFileFromSD(PICK_USER_PIC);
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
			public void onClick() {
				getFileFromSD(PICK_USER_PHOTO);
			}
		});
	}
	
	private void getFileFromSD(int requestCode) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);

		startActivityForResult(intent, requestCode);
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

//					Log.e("majie", "path  " + path);
					
					if (path != null) {
						if(requestCode==PICK_USER_PIC){
							Bitmap b= BitmapUtil.getThumbilBitmap(path,100);
						    userimg.setImageBitmap(b);
						    String dir=FileUtils.getSDCardRoot()+getPackageName()+File.separator;
						    FileUtils.mkdirs(dir);
//						    avatarPath=dir+path.substring(path.lastIndexOf("/")+1);
						    BitmapUtil.saveBitmapToSdcard(b, avatarPath);
						}else if(requestCode==PICK_USER_PHOTO){
							albumView.refresh(path);
						}
					    
					}
				}

			} catch (Exception e) {
			}

		}
	}
	
	private void updateUser(BmobFile bmobFile) {
		final MyUser bmobUser = BmobUser.getCurrentUser(this, MyUser.class);
		if (bmobUser != null) {
			MyUser newUser = new MyUser();
//			newUser.setPassword("123456");
//			if(bmobFile!=null){
//				newUser.setAvatar(bmobFile);
//			}
			newUser.setBmobFiles(myUser.getBmobFiles());;
			newUser.setAge(age);
			newUser.setGender(gender);
			newUser.setFilePath(avatarPath);
			newUser.setObjectId(bmobUser.getObjectId());
			newUser.update(this,new UpdateListener() {

				@Override
				public void onSuccess() {
					toastMsg("更新用户信息成功");
					finish();
				}

				@Override
				public void onFailure(int code, String msg) {
					toastMsg("更新用户信息失败:" + msg);
				}
			});
		} 
	}
	
//	private void uploadAvatarFile() {
//		final BmobFile bmobFile = new BmobFile(new File(avatarPath));
//		
//		bmobFile.uploadblock(this, new UploadFileListener() {
//
//			@Override
//			public void onSuccess() {
////				Log.i("majie", "名称--"+bmobFile.getFileUrl(MyDataActivity.this)+"，文件名="+bmobFile.getFilename());
//				updateUser(bmobFile);
//			}
//
//			@Override
//			public void onProgress(Integer arg0) {
//			}
//
//			@Override
//			public void onFailure(int arg0, String arg1) {
////				Log.i("majie", "-->uploadMovoieFile-->onFailure:" + arg0+",msg = "+arg1);
//			}
//
//		});
//	}
	
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
				Log.e("majie", "url ="+urls.size());
				if(urls.size()==paths.size()){
					myUser.setBmobFiles(files);;
					uploadOrUpdate();
					
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
	
	private void uploadOrUpdate(){
//		if(avatarPath!=null){
//			uploadAvatarFile();
//		}else{
			updateUser(null);
//		}
	}
	
}
