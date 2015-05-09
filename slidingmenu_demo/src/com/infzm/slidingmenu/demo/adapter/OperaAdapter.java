package com.infzm.slidingmenu.demo.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.listener.UpdateListener;

import com.infzm.slidingmenu.demo.R;
import com.infzm.slidingmenu.demo.beans.OperaBean;
import com.infzm.slidingmenu.demo.utils.SharedPrefUtil;

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
		holder.tvUsername.setText(beans.get(position).getUsername());
		holder.tvOperaContent.setText(beans.get(position).getOperaContent());
		holder.tvLikeNum.setText(""+beans.get(position).getLikeNum());
		
		holder.llLike.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(beans.get(position).getUserId()!=null&&!su.getValueByKey("like_"+beans.get(position).getUserId(), "").equals("")){
					Toast.makeText(context, "不能重复点赞", Toast.LENGTH_SHORT).show();
					return;
				}
				beans.get(position).setLikeNum(beans.get(position).getLikeNum()+1);
				su.putValueByKey("like_"+beans.get(position).getUserId(),"-");
				notifyDataSetChanged();
				updateLike(beans.get(position));
			}
		});
		
		return convertView;
	}
	
	class ViewHolder{
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
