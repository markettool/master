package org.markettool.opera.adapter;

import java.util.ArrayList;
import java.util.List;

import org.markettool.opera.MyApplication;
import org.markettool.opera.R;
import org.markettool.opera.beans.MyUser;
import org.markettool.opera.utils.BitmapUtil;
import org.markettool.opera.utils.FileUtils;

import cn.bmob.v3.BmobUser;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class AlbumAdapter extends BaseAdapter {
	
	private List<String > paths;
	private Context context;
	private LayoutInflater inflater;
	
	private int screenWidth;
	private int width;
	private String dir;
	
	private MyUser myUser;
	
	private List<String > thubPaths=new ArrayList<String>();
	
	public AlbumAdapter(Context context,List<String > paths){
		this.context=context;
		this.paths=paths;
		this.inflater=LayoutInflater.from(context);
		
		MyApplication app=(MyApplication)((Activity)context).getApplication();
		screenWidth=app.getScreenWidth();
		width=(screenWidth-40)/4;
		
		dir = FileUtils.PHOTO_PATH;
		FileUtils.mkdirs(dir);
		
		myUser = BmobUser.getCurrentUser(context, MyUser.class);
	}

	@Override
	public int getCount() {
		return Math.min(paths.size()+1, 4);
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ImageView view=(ImageView) inflater.inflate(R.layout.album_item, null);
		if(arg0<paths.size()){
			if(paths.get(arg0).contains("photo")){
				Bitmap bitmap=BitmapUtil.getOriginBitmap(paths.get(arg0));
				view.setImageBitmap(BitmapUtil.getCanvasBitmap(bitmap, width, width));
				return view;
			}
			Bitmap bitmap=BitmapUtil.getThumbilBitmap(paths.get(arg0), width);
			saveThubPhoto(bitmap, arg0);
			view.setImageBitmap(BitmapUtil.getCanvasBitmap(bitmap, width, width));
			bitmap.recycle();
		}else{
			Bitmap bitmap=BitmapUtil.getBitmapFromRes(context, R.drawable.ic_photo_add);
			view.setImageBitmap(BitmapUtil.getCanvasBitmap(bitmap, width, width));
		}
		
		return view;
	}
	
	private List<String > saveThubPhoto(Bitmap bitmap,int position){
		List<String > thubPaths=new ArrayList<String>();
		String thubPath=dir+myUser.getUsername()+"_photo_"+position+".png";
		thubPaths.add(thubPath);
		BitmapUtil.saveBitmapToSdcard(bitmap, thubPath);	
		return thubPaths;
	}

	public List<String> getThubPaths() {
		return thubPaths;
	}

}
