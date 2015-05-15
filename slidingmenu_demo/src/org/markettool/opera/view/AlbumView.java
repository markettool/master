package org.markettool.opera.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.markettool.opera.R;
import org.markettool.opera.adapter.AlbumAdapter;
import org.markettool.opera.beans.MyUser;
import org.markettool.opera.utils.FileUtils;

import cn.bmob.v3.BmobUser;

import android.content.Context;
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

	public List<String> getThubPaths() {
		return adapter.getThubPaths();
	}
	
}
