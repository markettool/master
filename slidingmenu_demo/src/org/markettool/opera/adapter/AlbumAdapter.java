package org.markettool.opera.adapter;

import java.util.List;

import org.markettool.opera.MyApplication;
import org.markettool.opera.R;
import org.markettool.opera.beans.MyBmobFile;
import org.markettool.opera.utils.BitmapHelp;
import org.markettool.opera.utils.BitmapUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import cn.bmob.v3.datatype.BmobFile;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

public class AlbumAdapter extends BaseAdapter {
	
	private List<BmobFile > bmobFiles;
	private Context context;
	private LayoutInflater inflater;
	
	private int screenWidth;
	private int width;
	
	private boolean isCanAdd=true;
	
	private BitmapUtils bitmapUtils;
//	private BitmapDisplayConfig config;
	private int limit=0;
	
	public AlbumAdapter(Context context,List<BmobFile> paths){
		this.context=context;
		this.bmobFiles=paths;
		this.inflater=LayoutInflater.from(context);
		
		MyApplication app=(MyApplication)((Activity)context).getApplication();
		screenWidth=app.getScreenWidth();
		width=(screenWidth-40)/4;
		
		bitmapUtils=BitmapHelp.getBitmapUtils(context);
//		config=BitmapHelp.getDisplayConfig(context, width, width);
	}
	
	public void setLimit(int limit){
		this.limit=limit;
	}
	
	public void setCanAdd(boolean isCanAdd){
		this.isCanAdd=isCanAdd;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(limit!=0){
			return limit;
		}
		if(isCanAdd){
			return bmobFiles.size()+1;
		}else{
			return bmobFiles.size();
		}
		
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
	public View getView(int pos, View convertView, ViewGroup arg2) {
		if(convertView==null){
			convertView=inflater.inflate(R.layout.album_item, null);
		}
		final ImageView iv=(ImageView) convertView;
		if(pos<bmobFiles.size()){
			if(bmobFiles.get(pos) instanceof MyBmobFile){
				MyBmobFile bmobFile=(MyBmobFile) bmobFiles.get(pos);
				Bitmap bitmap=BitmapUtil.getThumbilBitmap(bmobFile.getLocalFilePath(), width);
				iv.setImageBitmap(BitmapUtil.getCanvasBitmap(bitmap, width, width));
			}else{
				bitmapUtils.display(iv, bmobFiles.get(pos).getFileUrl(context),new BitmapLoadCallBack<View>() {

					@Override
					public void onLoadCompleted(View arg0, String arg1,
							Bitmap bitmap, BitmapDisplayConfig config,
							BitmapLoadFrom arg4) {
						iv.setImageBitmap(BitmapUtil.getCanvasBitmap(bitmap, width, width));
					}

					@Override
					public void onLoadFailed(View arg0, String arg1, Drawable arg2) {
						Log.e("majie", "download pic failed");
					}
				});
			}
			
		}else{
			Bitmap bitmap=BitmapUtil.getBitmapFromRes(context, R.drawable.ic_photo_add);
			iv.setImageBitmap(BitmapUtil.getCanvasBitmap(bitmap, width, width));
		}
		
		return convertView;
	}
	
}
