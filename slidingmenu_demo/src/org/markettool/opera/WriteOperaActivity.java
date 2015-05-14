package org.markettool.opera;

import java.io.File;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.banner.AdViewListener;

import org.markettool.opera.R;
import org.markettool.opera.beans.MyUser;
import org.markettool.opera.beans.OperaBean;
import org.markettool.opera.utils.BitmapUtil;
import org.markettool.opera.utils.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class WriteOperaActivity extends BaseActivity {
	int PICK_REQUEST_CODE = 0;
	
	private EditText etOpera;
	private Button btPublish;
	private ImageView ivAddImage;
	private MyUser myUser;
	private RelativeLayout mAdContainer;
	private String operaPicPath;
	
	private int screenWidth;
	private int screenHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_writeopera);
		initView();
		setListeners();
		initData();
		showBanner();
	}

	@Override
	protected void initView() {
		etOpera=(EditText) findViewById(R.id.et_opera);
		btPublish=(Button) findViewById(R.id.btn_write);
		mAdContainer = (RelativeLayout) findViewById(R.id.adcontainer);
		ivAddImage=(ImageView) findViewById(R.id.iv_addimage);
		
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
//					File file=new File(operaPicPath);
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
		
		MyApplication app=(MyApplication)getApplication();
		screenWidth=app.getScreenWidth();
		screenHeight=app.getScreenHeight();
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
		if(myUser.getAvatar()!=null){
			p.setUserPicPath(myUser.getAvatar().getFileUrl(this));
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
			}

			@Override
			public void onFailure(int code, String msg) {
				toastMsg("创建数据失败：" + msg);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		setResult(0x01);
	}
	
	private void showBanner() {

		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);

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
		mAdContainer.addView(adView,layoutParams);
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
					    Bitmap b= BitmapUtil.getThumbilBitmap(path,200);
					    int height=Math.min(b.getHeight()*screenWidth/b.getWidth(),screenHeight/3);
						b=Bitmap.createScaledBitmap(b, screenWidth, height, false);
					    etOpera.setBackgroundDrawable(new BitmapDrawable(b));
					    etOpera.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, height));
					    etOpera.setTextColor(getResources().getColor(R.color.white));
					    String dir=FileUtils.getSDCardRoot()+getPackageName()+File.separator;
					    FileUtils.mkdirs(dir);
					    operaPicPath=dir+path.substring(path.lastIndexOf("/")+1);
					    BitmapUtil.saveBitmapToSdcard(b, operaPicPath);
					    
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
				Log.i("majie", "-->uploadMovoieFile-->onFailure:" + arg0+",msg = "+arg1);
			}

		});

	}

}
