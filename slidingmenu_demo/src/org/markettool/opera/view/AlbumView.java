package org.markettool.opera.view;

import java.util.ArrayList;
import java.util.List;

import org.markettool.opera.R;
import org.markettool.opera.adapter.AlbumAdapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import cn.bmob.v3.datatype.BmobFile;

public class AlbumView extends LinearLayout {
	private View view;
	private GridView gv;
	private Context context;
	private List<BmobFile> bmobFiles=new ArrayList<BmobFile>();
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
		
		adapter=new AlbumAdapter(context, bmobFiles);
		gv.setAdapter(adapter);
		
	}
	
	public void setLimit(int limit){
		adapter.setLimit(limit);
	}
	
	public void setIsCanAdd(boolean isCanAdd){
		adapter.setCanAdd(isCanAdd);
	}
	
	private void setListeners(){
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(listener!=null&&bmobFiles.size()<4){
					listener.onClick(arg2);
				}	
			}
		});
	}
	
	public void setOnHandleListener(onHandleListener listener){
		this.listener=listener;
	}
	
	public interface onHandleListener{
		public void onClick(int index);
	}
	
	public void addData(int index,BmobFile file){
		if(index<bmobFiles.size()){
			bmobFiles.set(index, file);
		}else{
			bmobFiles.add(file);
		}
		
		adapter.notifyDataSetChanged();
	}
	
	public void addData(BmobFile file){
		if(bmobFiles==null){
			bmobFiles=new ArrayList<BmobFile>();
		}
		bmobFiles.add(file);
		adapter.notifyDataSetChanged();
	}

	public List<BmobFile> getBmobFiles() {
		return bmobFiles;
	}
	
}
