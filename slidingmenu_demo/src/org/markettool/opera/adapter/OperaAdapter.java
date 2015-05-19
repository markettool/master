package org.markettool.opera.adapter;

import java.util.List;

import org.markettool.opera.CommentActivity;
import org.markettool.opera.OthersDataActivity;
import org.markettool.opera.R;
import org.markettool.opera.beans.OperaBean;
import org.markettool.opera.utils.BitmapHelp;
import org.markettool.opera.utils.SharedPrefUtil;
import org.markettool.opera.view.CircleImageView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;

public class OperaAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<OperaBean> beans;
	private Context context;
	private SharedPrefUtil su;
	
	private BitmapUtils bitmapUtils;
//	private BitmapDisplayConfig config;

	public OperaAdapter(Context context, List<OperaBean> beans) {
		this.context = context;
		this.beans = beans;
		this.mInflater = LayoutInflater.from(context);
		su = new SharedPrefUtil(context, "opera");
		
		bitmapUtils=BitmapHelp.getBitmapUtils(context);
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
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.opera_item, null);
			holder = new ViewHolder();
			holder.ivUserPic = (CircleImageView) convertView.findViewById(R.id.user_pic);
			holder.tvUsername = (TextView) convertView.findViewById(R.id.user_name);
			holder.tvOperaContent = (TextView) convertView.findViewById(R.id.opera_content);
			holder.llLike = (LinearLayout) convertView.findViewById(R.id.ll_feed_like);
			holder.llComment = (LinearLayout) convertView.findViewById(R.id.ll_feed_comment);
			holder.tvLikeNum = (TextView) convertView.findViewById(R.id.tv_feed_like_num);
			holder.tvCommentNum = (TextView) convertView.findViewById(R.id.tv_feed_comment_num);

			holder.rlOperaBg = (RelativeLayout) convertView.findViewById(R.id.opera_item_bg);
			holder.ivOperaPic = (ImageView) convertView.findViewById(R.id.opera_pic);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final int position = arg0;
		try {

			if (position < beans.size()) {
				holder.tvUsername.setText(beans.get(position).getUsername());
				holder.tvOperaContent.setText(beans.get(position).getOperaContent());
				holder.tvLikeNum.setText("" + beans.get(position).getLikeNum());
				holder.tvCommentNum.setText(""+ beans.get(position).getCommentNum());

				OperaBean bean=beans.get(position);
				BmobFile userPic=bean.getUserPic();
				if(userPic!=null){
					userPic.loadImageThumbnail(context, holder.ivUserPic, 60, 60);
//					Log.e("majie", userPic.getFileUrl(context));
//					bitmapUtils.display(holder.ivUserPic, "http://f10.topitme.com/l/201009/03/12835051651175.jpg");
				}else{
					holder.ivUserPic.setImageResource(R.drawable.wwj_748);
				}
				
				BmobFile operaPic=bean.getOperaPic();
				if(operaPic!=null){
					bitmapUtils.display(holder.ivOperaPic, operaPic.getFileUrl(context));
				}else{
					holder.ivOperaPic.setImageBitmap(null);
				}
			}
		} catch (Exception e) {

			Log.e("majie", e.getMessage());
		}

		holder.llLike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (beans.get(position).getObjectId() != null
						&& !su.getValueByKey("like_" + beans.get(position).getObjectId(), "").equals("")) {
					Toast.makeText(context, "不能重复点赞", Toast.LENGTH_SHORT).show();
					return;
				}
				beans.get(position).setLikeNum(beans.get(position).getLikeNum() + 1);
				su.putValueByKey("like_" + beans.get(position).getObjectId(),"-");
				notifyDataSetChanged();
				updateLike(beans.get(position));
			}
		});
		holder.llComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, CommentActivity.class);
				intent.putExtra("operaBean", beans.get(position));
				context.startActivity(intent);
			}
		});
		
		holder.ivUserPic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, OthersDataActivity.class);
				intent.putExtra("username", beans.get(position).getUsername());
				context.startActivity(intent);
			}
		});

		return convertView;
	}
	
	class ViewHolder {
		CircleImageView ivUserPic;
		TextView tvUsername;
		TextView tvOperaContent;
		LinearLayout llLike;
		LinearLayout llComment;
		TextView tvLikeNum;
		TextView tvCommentNum;
		RelativeLayout rlOperaBg;
		ImageView ivOperaPic;
	}

	/**
	 * 更新对象
	 */
	private void updateLike(OperaBean bean) {
		final OperaBean p = new OperaBean();
		p.setLikeNum(bean.getLikeNum());
		p.update(context, bean.getObjectId(), new UpdateListener() {

			@Override
			public void onSuccess() {
				Log.e("majie", "更新成功：" + p.getUpdatedAt());
			}

			@Override
			public void onFailure(int code, String msg) {
				Log.e("majie", "更新失败：" + msg);
			}
		});

	}

}
