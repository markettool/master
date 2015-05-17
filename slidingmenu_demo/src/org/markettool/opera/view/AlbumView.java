package org.markettool.opera.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.markettool.opera.R;
import org.markettool.opera.adapter.AlbumAdapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

public class AlbumView extends LinearLayout {
	private View view;
	private GridView gv;
	private Context context;
	private List<String> paths=new ArrayList<String>();
	private AlbumAdapter adapter;
	private onHandleListener listener;
	
//	private boolean isCanAdd=true;
	
//	private String dir;
//	private MyUser myUser;

	public AlbumView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		view = LayoutInflater.from(context).inflate(R.layout.view_album,
				null, true);
		addView(view, 0);
		
		initViews();
		setListeners();
		init();
	}
	
	private void initViews(){
		gv = (GridView) view.findViewById(R.id.gv);
		setOrientation(VERTICAL);
	}
	
	private void init(){
		
		adapter=new AlbumAdapter(context, paths);
		gv.setAdapter(adapter);
		
//		myUser = BmobUser.getCurrentUser(context, MyUser.class);
//		dir = FileUtils.PHOTO_PATH;
		
//		setUserPhotos();
	}
	
	public void setIsCanAdd(boolean isCanAdd){
		adapter.setIsCanAdd(isCanAdd);
	}
	
	private void setListeners(){
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(arg2==paths.size()){
					if(listener!=null&&paths.size()<5){
						listener.onClick();
					}
				}
			}
		});
	}
	
	public void setOnHandleListener(onHandleListener listener){
		this.listener=listener;
	}
	
	public interface onHandleListener{
		public void onClick();
	}


	public void refresh(String path){
		paths.add(path);
		adapter.notifyDataSetChanged();
	}

	public List<String> getPaths() {
		return paths;
	}
	
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			refresh((String) msg.obj);
		};
	};
	
	public void setInitialPaths(final List<String> initialPaths){
		new Thread(){
			public void run() {
				super.run();
				for(String path:initialPaths){
					File file=new File(path);
					if(file.exists()){
						Message msg=new Message();
						msg.obj=path;
						handler.sendMessage(msg);
						
					}else{
						break;
					}
				}
			};
		}.start();
	}
	
}
