package org.markettool.opera.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.markettool.opera.CommentActivity;
import org.markettool.opera.R;
import org.markettool.opera.beans.OperaBean;
import org.markettool.opera.utils.BitmapUtil;
import org.markettool.opera.utils.FileUtils;
import org.markettool.opera.utils.SharedPrefUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.listener.UpdateListener;

public class OperaAdapter extends BaseAdapter {
	
	private LayoutInflater mInflater;
	private List<OperaBean> beans;
	private Context context;
	private SharedPrefUtil su;
	
	public OperaAdapter(Context context,List<OperaBean> beans){
		this.context=context;
		this.beans=beans;
		this.mInflater=LayoutInflater.from(context);
		su=new SharedPrefUtil(context, "opera");
	}

	@Override
	public int getCount() {
		return beans.size();
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
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		ViewHolder holder=null;
		if(convertView==null){
			convertView=mInflater.inflate(R.layout.opera_item, null);
			holder=new ViewHolder();
			holder.ivUserPic=(ImageView) convertView.findViewById(R.id.user_pic);
			holder.tvUsername=(TextView) convertView.findViewById(R.id.user_name);
			holder.tvOperaContent=(TextView) convertView.findViewById(R.id.opera_content);
			holder.llLike=(LinearLayout) convertView.findViewById(R.id.ll_feed_like);
			holder.llComment=(LinearLayout) convertView.findViewById(R.id.ll_feed_comment);
			holder.tvLikeNum=(TextView) convertView.findViewById(R.id.tv_feed_like_num);
			holder.tvCommentNum=(TextView) convertView.findViewById(R.id.tv_feed_comment_num);

			convertView.setTag(holder);
		}
		else{
			holder=(ViewHolder) convertView.getTag();
		}
		final int position=arg0;
		if(position<beans.size()){
			holder.tvUsername.setText(beans.get(position).getUsername());
			holder.tvOperaContent.setText(beans.get(position).getOperaContent());
			holder.tvLikeNum.setText(""+beans.get(position).getLikeNum());
			holder.tvCommentNum.setText(""+beans.get(position).getCommentNum());
			
			String dir=FileUtils.getSDCardRoot()+context.getPackageName()+File.separator;
		    FileUtils.mkdirs(dir);
//		    String url=beans.get(position).getUserPicPath();
		    String savePath=dir+beans.get(position).getUsername();
		    File file=new File(savePath);
		    if(file.exists()){
				try {
					Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
					if(bitmap!=null)
			    	    holder.ivUserPic.setImageBitmap(bitmap);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
		    	
		    }
		}
		
		holder.llLike.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(beans.get(position).getObjectId()!=null&&!su.getValueByKey("like_"+beans.get(position).getObjectId(), "").equals("")){
					Toast.makeText(context, "不能重复点赞", Toast.LENGTH_SHORT).show();
					return;
				}
				beans.get(position).setLikeNum(beans.get(position).getLikeNum()+1);
				su.putValueByKey("like_"+beans.get(position).getObjectId(),"-");
				notifyDataSetChanged();
				updateLike(beans.get(position));
			}
		});
		holder.llComment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(context, CommentActivity.class);
				intent.putExtra("operaBean", beans.get(position));
				context.startActivity(intent);
			}
		});
		
		return convertView;
	}
	
	class ViewHolder{
		ImageView ivUserPic;
		TextView tvUsername;
		TextView tvOperaContent;
		LinearLayout llLike;
		LinearLayout llComment;
		TextView tvLikeNum;
		TextView tvCommentNum;
	}
	
	/**
	 * 更新对象
	 */
	private void updateLike(OperaBean bean) {
		final OperaBean p2 = new OperaBean();
		p2.setLikeNum(bean.getLikeNum());;
		p2.update(context, bean.getObjectId(), new UpdateListener() {

			@Override
			public void onSuccess() {
				Log.e("majie", "更新成功：" + p2.getUpdatedAt());
			}

			@Override
			public void onFailure(int code, String msg) {
				Log.e("majie", "更新失败：" + msg);
			}
		});

	}

}
