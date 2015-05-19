package org.markettool.opera.utils;

import org.markettool.opera.view.LoadingDialog;
import org.markettool.opera.view.LoadingDialog.OnDialogBackListener;

import android.content.Context;

/**
 * 通用进度条
 * 
 * @author liurun {mobile:15010123578, email:liurun_225@sina.com, qq:305760407}
 * @date 2014-05-23
 */
public class ProgressUtil {
	public static void showProgress(Context context, CharSequence message) {
		LoadingDialog.showDialog(context);
	}
	
	/*
	 * 监听dialog back 返回键操作
	 */
	public static void showProgressWithBackListener(Context context, OnDialogBackListener listener) {
		LoadingDialog.showDialogWithListener(context, listener);
	}
	
	/**
	 * 屏蔽dialog back返回键盘。
	 * @param context
	 */
	public static void showProgressNoBack(Context context) {
		LoadingDialog.showDialogNoBack(context);
	}
	
	public static void closeProgress() {
		LoadingDialog.closeDialog();
	}
}
