package org.markettool.opera.view;

import org.markettool.opera.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 进度条
 * 
 * @author liurun {mobile:15010123578, email:liurun_225@sina.com, qq:305760407}
 * @date 2014-05-23
 */
public class LoadingDialog extends Dialog {

	private static final int ANDROID_5_0 = 21;

	private static LoadingDialog instance;
	private RelativeLayout layout;
	private ImageView img;
	private RotateAnimation circleAnim;

	private LoadingDialog(Context context, int theme) {
		super(context, theme);
		initView();
		setCanceledOnTouchOutside(false);//点击外部区域不会消失
	}

	private void initView() {
		setContentView(R.layout.loading_dialog);
		layout = (RelativeLayout) findViewById(R.id.loading_dialog_layout);
		img = (ImageView) findViewById(R.id.loading_dialog_img);
		img.setAnimation(circleAnim = new RotateAnimation(360f, 0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f));
		circleAnim.setDuration(1000);
		circleAnim.setInterpolator(new LinearInterpolator());
		circleAnim.setRepeatCount(Animation.INFINITE);
	}

	public static void showDialog(Context context) {
		if (context == null
				|| (context instanceof Activity 
						&& ((Activity) context).isFinishing()))
			return;

		synchronized (LoadingDialog.class) {
			if (instance == null || !instance.isShowing()) {
				instance = new LoadingDialog(context, R.style.LoadingDialog);
				if (Build.VERSION.SDK_INT >= ANDROID_5_0) {
					instance.getWindow().setType(
							WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				} else {
					// 在5.0部分手机上转圈圈转得不正常，显示有问题
					// 在5.1上，加上这个句会崩，所以做了一个判断
					instance.getWindow().setType(PixelFormat.TRANSLUCENT);
				}
				//注释的部分，屏蔽点击事件，后面的view不会再接收event，
				// instance.getWindow().addFlags(WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW);
				instance.show();
				instance.animation(true);
			}
		}
	}

	/*
	 * 取消dialog响应back键
	 */
	public static void showDialogNoBack(Context context) {
		if (context == null
				|| (context instanceof Activity && ((Activity) context)
						.isFinishing()))
			return;

		synchronized (LoadingDialog.class) {
			if (instance == null || !instance.isShowing()) {
				instance = new LoadingDialog(context, R.style.LoadingDialog);
				// instance.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				instance.setCancelable(false);// 取消返回键事件响应
				instance.show();
				instance.animation(true);
			}
		}
	}

	public static void showDialogWithListener(Context context,
			final OnDialogBackListener listener) {
		if (context == null
				|| (context instanceof Activity && ((Activity) context)
						.isFinishing()))
			return;

		synchronized (LoadingDialog.class) {
			if (instance == null || !instance.isShowing()) {
				instance = new LoadingDialog(context, R.style.LoadingDialog);
				// instance.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				instance.setOnKeyListener(new DialogInterface.OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						if (keyCode == KeyEvent.KEYCODE_BACK
								&& event.getAction() == KeyEvent.ACTION_UP
								&& !event.isCanceled()) {

							listener.onKeyBackListener4Dialog();// 监听back 键盘操作

							// dialog.cancel();
							closeDialog();
							return true;
						}
						return false;
					}
				});
				instance.show();
				instance.animation(true);
			}
		}
	}

	public interface OnDialogBackListener {
		public void onKeyBackListener4Dialog();
	}

	public static void closeDialog() {
		synchronized (LoadingDialog.class) {
			if (instance != null && instance.isShowing()) {
				instance.animation(false);
				instance.clear();
			}
		}
	}

	private Handler handler = new Handler();

	private void clear() {
		handler.postDelayed(new Runnable() {
			public void run() {
				try {
					if (instance != null && instance.isShowing()) {
						instance.cancel();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					instance = null;
				}
			}
		}, 300);
	}

	private void animation(boolean flag) {
		ScaleAnimation scaleAnim = new ScaleAnimation(flag ? 0 : 1, flag ? 1
				: 0, flag ? 0 : 1, flag ? 1 : 0, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnim.setDuration(500);
		scaleAnim.setInterpolator(new LinearInterpolator());
		layout.setAnimation(scaleAnim);
		layout.setAnimationCacheEnabled(false);
		layout.startLayoutAnimation();
	}

	public interface AlphaListener {
		public void onProcess();
	}

	public static void appearAlphaAnim(View v, final AlphaListener listener) {
		v.startAnimation(alphaAnim(true, new AnimationListener() {
			public void onAnimationStart(Animation animation) {
				listener.onProcess();
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
			}
		}));
	}

	public static void disappearAlphaAnim(View v, final AlphaListener listener) {
		v.startAnimation(alphaAnim(false, new AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				listener.onProcess();
			}
		}));
	}

	private static AlphaAnimation alphaAnim(boolean show,
			AnimationListener listener) {
		AlphaAnimation anim = new AlphaAnimation(show ? 0 : 1, show ? 1 : 0);
		anim.setDuration(500);
		anim.setFillAfter(true);
		anim.setAnimationListener(listener);
		return anim;
	}
}
