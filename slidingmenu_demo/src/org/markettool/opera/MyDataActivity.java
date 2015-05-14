package org.markettool.opera;

import java.io.File;

import org.markettool.opera.R;
import org.markettool.opera.beans.MyUser;
import org.markettool.opera.utils.BitmapUtil;
import org.markettool.opera.utils.FileUtils;

import com.bmob.BTPFileResponse;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class MyDataActivity extends BaseActivity {
	int PICK_REQUEST_CODE = 0;
	private EditText username, userage;
	private Button submit;
	private RadioGroup group;
	private boolean gender = true;
	private ImageView userimg;
	private LinearLayout pswLayout;
    private RadioButton maleRb,femaleRb;
    private String avatarPath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivity_register);
		
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
		submit = (Button) findViewById(R.id.submit);
		submit.setText("保存");
		
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("我的资料");
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
		MyUser myUser = BmobUser.getCurrentUser(this, MyUser.class);
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
	}
	private void setListeners(){
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String name = username.getText().toString();
//				String psw = userpsw.getText().toString();
				String age = userage.getText().toString();
				if (name.equals("") || age.equals("")
						) {
					toastMsg("请填写基本资料");
					return;
				}

				if(avatarPath==null){
					updateUser(name, new Integer(age), gender, null);
				}else{
					uploadAvatarFile(name,  new Integer(age), gender, new File(avatarPath));
				}
				
			}
		});

		
		group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.male) {
					gender = true;
//					Toast.makeText(getApplicationContext(), "男", 2000).show();
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
	}
	
	private void getFileFromSD() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		// intent.setDataAndType(Uri.parse("/mnt/sdcard"), "text/plain");

		startActivityForResult(intent, PICK_REQUEST_CODE);
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
					    Bitmap b= BitmapUtil.getThumbilBitmap(path,100);
					    userimg.setImageBitmap(b);
					    String dir=FileUtils.getSDCardRoot()+getPackageName()+File.separator;
					    FileUtils.mkdirs(dir);
					    avatarPath=dir+path.substring(path.lastIndexOf("/")+1);
					    BitmapUtil.saveBitmapToSdcard(b, avatarPath);
					}
				}

			} catch (Exception e) {
			}

		}
	}
	
	private void updateUser(String name,int age,boolean gender,BmobFile bmobFile) {
		final MyUser bmobUser = BmobUser.getCurrentUser(this, MyUser.class);
		if (bmobUser != null) {
			Log.d("bmob", "getObjectId = " + bmobUser.getObjectId());
			Log.d("bmob", "getUsername = " + bmobUser.getUsername());
			MyUser newUser = new MyUser();
//			newUser.setPassword("123456");
			if(bmobFile!=null){
				newUser.setAvatar(bmobFile);
			}
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
	
	private void uploadAvatarFile(final String name,final int age,final boolean gender,File file) {
		final BmobFile bmobFile = new BmobFile(file);
		
		bmobFile.uploadblock(this, new UploadFileListener() {

			@Override
			public void onSuccess() {
				Log.i("majie", "名称--"+bmobFile.getFileUrl(MyDataActivity.this)+"，文件名="+bmobFile.getFilename());
				updateUser(name, age, gender, bmobFile);
			}

			@Override
			public void onProgress(Integer arg0) {
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				Log.i("majie", "-->uploadMovoieFile-->onFailure:" + arg0+",msg = "+arg1);
			}

		});

	}

	
}
