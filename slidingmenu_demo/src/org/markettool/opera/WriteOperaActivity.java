package org.markettool.opera;

import java.io.File;

import org.markettool.opera.beans.MyUser;
import org.markettool.opera.beans.OperaBean;
import org.markettool.opera.utils.BitmapUtil;
import org.markettool.opera.utils.FileUtils;
import org.markettool.opera.utils.ProgressUtil;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class WriteOperaActivity extends BaseActivity {
	int PICK_REQUEST_CODE = 0;
	
	private EditText etOpera;
	private Button btPublish;
	private ImageView ivAddImage;
	private ImageView ivOperaPic;
	private MyUser myUser;
	private String operaPicPath;
	
//	private int screenWidth;
//	private int screenHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_writeopera);
		initView();
		setListeners();
		initData();
//		showBanner();
	}

	@Override
	protected void initView() {
		etOpera=(EditText) findViewById(R.id.et_opera);
		btPublish=(Button) findViewById(R.id.btn_write);
		ivAddImage=(ImageView) findViewById(R.id.iv_addimage);
		ivOperaPic=(ImageView) findViewById(R.id.opera_pic);
		
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("我爱乱弹");
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
	
	private void setListeners(){
		btPublish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!TextUtils.isEmpty(etOpera.getText().toString())){
					ProgressUtil.showProgress(WriteOperaActivity.this, "");
					if(operaPicPath!=null){
						uploadFile(new File(operaPicPath));
					}else{
						writeOpera(null);
					}
					
				}else{
					toastMsg("输入为空");
				}
			}
		});
		
		ivAddImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				getFileFromSD();
			}
		});
	}

	@Override
	protected void initData() {

		myUser=BmobUser.getCurrentUser(this, MyUser.class);
		if(myUser==null){
			startActivity(LoginActivity.class);
			finish();
		}
		
//		MyApplication app=(MyApplication)getApplication();
//		screenWidth=app.getScreenWidth();
//		screenHeight=app.getScreenHeight();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		myUser=BmobUser.getCurrentUser(this, MyUser.class);
	}
	
	/**
	 * 插入对象
	 */
	private void writeOpera(BmobFile file) {
		final OperaBean p = new OperaBean();
		if(myUser.getBmobFiles().size()!=0){
			p.setUserPic(myUser.getBmobFiles().get(0));
		}
		p.setUsername(myUser.getUsername());
		p.setOperaContent(etOpera.getText().toString());
		if(file!=null){
			p.setOperaPic(file);
		}
		p.save(this, new SaveListener() {

			@Override
			public void onSuccess() {
				Log.d("bmob", "success  " );
				toastMsg("发表成功");
				finish();
				ProgressUtil.closeProgress();
			}

			@Override
			public void onFailure(int code, String msg) {
				toastMsg("失败：" + msg);
				ProgressUtil.closeProgress();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		setResult(0x01);
	}
	
	private void getFileFromSD() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
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
					    Bitmap b= BitmapUtil.getThumbilBitmap(path,200);
					    int width=Math.min(b.getWidth(), b.getHeight());
					    Bitmap bitmap= BitmapUtil.getCanvasBitmap(b, width, width);
					    ivOperaPic.setImageBitmap(bitmap);
					    String dir=FileUtils.getSDCardRoot()+getPackageName()+File.separator;
					    FileUtils.mkdirs(dir);
					    operaPicPath=dir+path.substring(path.lastIndexOf("/")+1);
					    BitmapUtil.saveBitmapToSdcard(b, operaPicPath);
					    b.recycle();
					}
				}

			} catch (Exception e) {
			}

		}
	}
	
	private void uploadFile(File file) {
		final BmobFile bmobFile = new BmobFile(file);
		
		bmobFile.uploadblock(this, new UploadFileListener() {

			@Override
			public void onSuccess() {
				Log.e("majie", "success");
				writeOpera(bmobFile);
			}

			@Override
			public void onProgress(Integer arg0) {
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				Log.i("majie", "fail:" + arg0+",msg = "+arg1);
			}

		});

	}

}
