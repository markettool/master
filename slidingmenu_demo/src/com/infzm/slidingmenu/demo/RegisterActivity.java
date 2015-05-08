package com.infzm.slidingmenu.demo;

import java.io.File;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegisterActivity extends BaseActivity {
	int PICK_REQUEST_CODE = 0;
	private EditText username, userpsw, userage;
	private Button submit;
	private RadioGroup group;
	private String sex = "0";
	private ImageView userimg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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

					File file = new File(path);
					System.out.println("path  " + path);
					if (path != null) {
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
						|| sex.equals("")) {
					Toast.makeText(getApplicationContext(), "请填写基本资料", 2000)
							.show();
					return;
				}

			}
		});

		
		group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.male) {
					sex = "男";
					Toast.makeText(getApplicationContext(), "男", 2000).show();
				} else {
					sex = "女";
				}

			}
		});

		userimg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Toast.makeText(getApplicationContext(), "更改用户头像！", 2000).show();
				toastMsg("更改用户头像！");
				getFileFromSD();
			}
		});
	}


}
