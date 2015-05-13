package org.markettool.opera;

import java.io.File;

import org.markettool.opera.beans.MyUser;
import org.markettool.opera.utils.BitmapUtil;
import org.markettool.opera.utils.FileUtils;

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
import android.widget.RadioGroup;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class RegisterActivity extends BaseActivity {
	int PICK_REQUEST_CODE = 0;
	private EditText username, userpsw, userage;
	private Button submit;
	private RadioGroup group;
	private boolean gender = true;
	private ImageView userimg;
	private String avatarPath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivity_register);

		initView();
        setListeners();
        
        initData();
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
					    Bitmap b= BitmapUtil.decodeBitmap(path);
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

	@Override
	protected void initView() {
		
		username = (EditText) findViewById(R.id.username);
		userpsw = (EditText) findViewById(R.id.userpsw);
		userage = (EditText) findViewById(R.id.userage);
		userimg = (ImageView) findViewById(R.id.userimg);
		submit = (Button) findViewById(R.id.submit);
		group = (RadioGroup) findViewById(R.id.sex);
		
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("用户注册");
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
		
	}
	
	private void setListeners(){
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String name = username.getText().toString();
				String psw = userpsw.getText().toString();
				String age = userage.getText().toString();
				if (name.equals("") || psw.equals("") || age.equals("")
						) {
					toastMsg("请填写基本资料");
					return;
				}
				if(avatarPath==null){
					signUp(name, psw, new Integer(age), gender, null);
				}else{
					uploadAvatarFile(name, psw, new Integer(age), gender,new File(avatarPath));
				}
				
//				signUp(name, psw, new Integer(age), gender);
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
	
	/**
	 * 注册用户
	 */
	private void signUp(final String name,String psw,final int age,final boolean gender,BmobFile file) {
		
		final MyUser myUser = new MyUser();
		myUser.setUsername(name);
		myUser.setPassword(psw);
		myUser.setAge(age);
		myUser.setGender(gender);
		myUser.setAvatar(file);
		if(avatarPath!=null){
			myUser.setFilePath(avatarPath);
		}
//		
		myUser.signUp(this, new SaveListener() {

			@Override
			public void onSuccess() {
				toastMsg("注册成功:" + myUser.getUsername() + "-"
						+ myUser.getObjectId() + "-" + myUser.getCreatedAt()
						+ "-" + myUser.getSessionToken()+",是否验证："+myUser.getEmailVerified());
				
				finish();
			}

			@Override
			public void onFailure(int code, String msg) {
				toastMsg("注册失败:" + msg);
			}
		});
		
	}
	
	private void uploadAvatarFile(final String name,final String psw,final int age,final boolean gender,File file) {
		final BmobFile bmobFile = new BmobFile(file);
		
		bmobFile.uploadblock(this, new UploadFileListener() {

			@Override
			public void onSuccess() {
				Log.i("majie", "名称--"+bmobFile.getFileUrl(RegisterActivity.this)+"，文件名="+bmobFile.getFilename());
				signUp(name, psw, age, gender,bmobFile);
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
