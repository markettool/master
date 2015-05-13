package org.markettool.opera;

import org.markettool.opera.beans.CommentBean;
import org.markettool.opera.beans.OperaBean;

import cn.bmob.v3.listener.SaveListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CommentActivity extends BaseActivity {
	
	private EditText etComment;
	private Button btSubmit;
	private String operaId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		
		initView();
		setListeners();
		initData();
	}

	private void setListeners(){
		btSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String comment=etComment.getText().toString();
						
				if(TextUtils.isEmpty(comment)){
					toastMsg("输入为空");
					return;
				}
				writeComment(comment, operaId);
			}
		});
	}
	
	@Override
	protected void initView() {

		etComment=(EditText) findViewById(R.id.et_comment);
		btSubmit=(Button) findViewById(R.id.submit);
	}

	@Override
	protected void initData() {

		operaId=getIntent().getStringExtra("operaId");
	}
	
    private void writeComment(String comment,String operaId) {
		
		final CommentBean p2 = new CommentBean();
		p2.setComment(comment);
		p2.setOperaId(operaId);
		p2.save(this, new SaveListener() {

			@Override
			public void onSuccess() {
				Log.d("bmob", "success  " );
				toastMsg("发表成功");
//				finish();
			}

			@Override
			public void onFailure(int code, String msg) {
				toastMsg("创建数据失败：" + msg);
			}
		});
	} 

}
