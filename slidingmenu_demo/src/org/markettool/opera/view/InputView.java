package org.markettool.opera.view;

import org.markettool.opera.R;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class InputView extends LinearLayout {
	private View view;
	private Context context;
	private Button btSend;
	private EditText etMsg;
	
	private OnSendClickListener listener;

	public InputView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		view = LayoutInflater.from(context).inflate(R.layout.view_input,
				null, true);
		setOrientation(VERTICAL);
		addView(view, 0);
		
		initViews();
		setListeners();
		init();
	}
	
	private void initViews(){
		btSend = (Button) view.findViewById(R.id.bt_send);
		etMsg = (EditText) view.findViewById(R.id.et_msg);
	}
	
	private void setListeners(){
		btSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String msg=etMsg.getText().toString();
				if(TextUtils.isEmpty(msg)){
					Toast.makeText(context, "输入为空", Toast.LENGTH_SHORT).show();
					return;
				}
		
				if(listener!=null){
					listener.onClick(msg);
				}
				etMsg.setText("");
			}
		});
	}
	
	private void init(){
		
		
	}
	
	public interface OnSendClickListener{
		public void onClick(String msg);
	}

	public void setOnSendClickListener(OnSendClickListener listener){
		this.listener=listener;
	}

}
