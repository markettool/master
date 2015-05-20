package org.markettool.opera;

import java.io.File;

import org.markettool.opera.beans.MyBmobFile;
import org.markettool.opera.beans.MyUser;
import org.markettool.opera.beans.OperaBean;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class WriteOperaActivity extends BaseActivity {
	int PICK_REQUEST_CODE = 0;
	
	private EditText etOpera;
	private AlbumView albumView;
	private String dir;
	private MyUser myUser;
	private MyBmobFile bmobFile;
//	private String operaPicPath;
	
//	private int screenWidth;
//	private int screenHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_writeopera);
		initView();
		setListeners();
		initData();
	}

	@Override
	protected void initView() {
		etOpera=(EditText) findViewById(R.id.et_opera);
//		btPublish=(Button) findViewById(R.id.btn_write);
//		ivAddImage=(ImageView) findViewById(R.id.iv_addimage);
//		ivOperaPic=(ImageView) findViewById(R.id.opera_pic);
		albumView=(AlbumView) findViewById(R.id.albumview);
		albumView.setLimit(1);
		
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("我爱乱弹");
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		
		mImgLeft.setVisibility(View.VISIBLE);
		mImgLeft.setBackgroundResource(R.drawable.bt_back_dark);
		
		mBtnTitleRight.setVisibility(View.VISIBLE);
		mBtnTitleRight.setText("发表");
		mBtnTitleRight.setTextColor(getResources().getColor(R.color.white));
		
	}
	
	private void publish(){
		if(!TextUtils.isEmpty(etOpera.getText().toString())){
			ProgressUtil.showProgress(WriteOperaActivity.this, "");
			if(bmobFile!=null){
				uploadFile();
			}else{
				writeOpera(null);
			}
			
		}else{
			toastMsg("输入为空");
		}
	}
	
	private void setListeners(){
        mBtnTitleRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				publish();
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
		
		dir = FileUtils.PHOTO_PATH;
		FileUtils.mkdirs(dir);
		
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
//				Log.d("bmob", "success  " );
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
						saveThubPhoto(path);
					}
				}

			} catch (Exception e) {
			}

		}
	}
	
	private void uploadFile() {
		
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
	
	private void saveThubPhoto(final String path){
		new Thread(){
			public void run() {
				super.run();
				Bitmap bitmap=BitmapUtil.getThumbilBitmap(path, 200);
				String thubPath=dir+myUser.getUsername()+"_opera_tmp"+".png";
				BitmapUtil.saveBitmapToSdcard(bitmap, thubPath);
				Message message=new Message();
				message.obj=thubPath;
//				message.arg1=position;
				handler.sendMessage(message);
			};
		}.start();
		
	}
	
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			bmobFile=new MyBmobFile(new File((String) msg.obj));
			bmobFile.setLocalFilePath((String) msg.obj);
			albumView.addData(bmobFile);
		};
	};

}
