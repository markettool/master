package org.markettool.opera.adapter;

import java.io.File;
import java.util.List;

import org.markettool.opera.CommentActivity;
import org.markettool.opera.MyApplication;
import org.markettool.opera.OthersDataActivity;
import org.markettool.opera.R;
import org.markettool.opera.beans.OperaBean;
import org.markettool.opera.utils.BitmapUtil;
import org.markettool.opera.utils.FileUtils;
import org.markettool.opera.utils.SharedPrefUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import cn.bmob.v3.listener.UpdateListener;

public class OperaAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<OperaBean> beans;
	private Context context;
	private SharedPrefUtil su;

	private int screenWidth;
	private int screenHeight;

	public OperaAdapter(Context context, List<OperaBean> beans) {
		this.context = context;
		this.beans = beans;
		this.mInflater = LayoutInflater.from(context);
		su = new SharedPrefUtil(context, "opera");
		MyApplication app = (MyApplication) ((Activity) context)
				.getApplication();
		screenWidth = app.getScreenWidth();
		screenHeight = app.getScreenHeight();
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
			holder.ivUserPic = (ImageView) convertView
					.findViewById(R.id.user_pic);
			holder.tvUsername = (TextView) convertView
					.findViewById(R.id.user_name);
			holder.tvOperaContent = (TextView) convertView
					.findViewById(R.id.opera_content);
			holder.llLike = (LinearLayout) convertView
					.findViewById(R.id.ll_feed_like);
			holder.llComment = (LinearLayout) convertView
					.findViewById(R.id.ll_feed_comment);
			holder.tvLikeNum = (TextView) convertView
					.findViewById(R.id.tv_feed_like_num);
			holder.tvCommentNum = (TextView) convertView
					.findViewById(R.id.tv_feed_comment_num);

			holder.rlOperaBg = (RelativeLayout) convertView
					.findViewById(R.id.opera_item_bg);
			holder.ivOperaPic = (ImageView) convertView
					.findViewById(R.id.opera_pic);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final int position = arg0;
		try {

			if (position < beans.size()) {
				holder.tvUsername.setText(beans.get(position).getUsername());
				holder.tvOperaContent.setText(beans.get(position)
						.getOperaContent());
				holder.tvLikeNum.setText("" + beans.get(position).getLikeNum());
				holder.tvCommentNum.setText(""
						+ beans.get(position).getCommentNum());

				String dir = FileUtils.getSDCardRoot()
						+ context.getPackageName() + File.separator + "opera"
						+ File.separator;
				FileUtils.mkdirs(dir);
				String savePath = dir + beans.get(position).getUsername();
				File file = new File(savePath);
				if (file.exists()) {
					Bitmap bitmap = BitmapUtil.getOriginBitmap(savePath);
					if (bitmap != null)
						holder.ivUserPic.setImageBitmap(bitmap);
					else {
						holder.ivUserPic.setImageResource(R.drawable.wwj_748);
					}

				}
				String operaPicPath = dir + beans.get(position).getObjectId();
				File operaFile = new File(operaPicPath);
				if (operaFile.exists()) {
					Bitmap bitmap = BitmapUtil.getOriginBitmap(operaPicPath);

					if (bitmap != null) {
						holder.ivOperaPic.setImageBitmap(bitmap);
					}

				} else {
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
						&& !su.getValueByKey(
								"like_" + beans.get(position).getObjectId(), "")
								.equals("")) {
					Toast.makeText(context, "不能重复点赞", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				beans.get(position).setLikeNum(beans.get(position).getLikeNum() + 1);
				su.putValueByKey("like_" + beans.get(position).getObjectId(),
						"-");
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
				intent.putExtra("operaBean", beans.get(position));
				context.startActivity(intent);
			}
		});

		return convertView;
	}

	class ViewHolder {
		ImageView ivUserPic;
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
//				Log.e("majie", "更新成功：" + p.getUpdatedAt());
			}

			@Override
			public void onFailure(int code, String msg) {
//				Log.e("majie", "更新失败：" + msg);
			}
		});

	}

}
