package org.markettool.opera.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

public class FileUtils {
	
	
	public static String getSDCardRoot() throws NoSdcardException{
			String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
		return SDCardRoot;
	}
	
	public static String getRoot(){
		return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
	}


	public static boolean isFileExist(String path) {
		if (TextUtils.isEmpty(path))
			return false;
		File file = new File(path);
		return file.exists();
	}
	
	public static boolean isfileExist(String dirpath,String fileName){
		File dir = new File(dirpath);
		if (!dir.exists())dir.mkdirs();
		File file = new File(dirpath+fileName);
		return file.exists();
	}
	
	public static void copyFile(String fromStr,String toStr){
		
		File fromFile = new File(fromStr);
		File toFile = new File(toStr);
		if(!toFile.getParentFile().exists()){
			toFile.getParentFile().mkdirs();
		}
		if(toFile.exists()){
			toFile.delete();
		}
		try{			
			FileInputStream fosfrom = new FileInputStream(fromFile);
			FileOutputStream fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while((c = fosfrom.read(bt))>0){
				fosto.write(bt,0,c);
			}
			fosfrom.close();
			fosto.close();
		}catch(Exception e){
			Log.e("file", e.getMessage());
		}
	}
	public void closeInputStream(InputStream inputStream){
		if(inputStream!=null){
			try {
				inputStream.close();
			} catch (IOException e) {
				Log.e("error", "close failed");
				e.printStackTrace();
			}
		}
	}
	public class NoSdcardException extends Exception{
		
	}
	
	public static String replaceChars(String file){
		if(file.contains("-")||file.contains(" ")||file.contains(":")){
			file=file.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
		}
		return file;
	}
	
	public void persistFileToSdCard(String filePath, String fileName, InputStream in, OnFileSaveListener onFileSaveListener) {
		File dir = new File(filePath);
		if (!dir.exists() || (dir.exists() && !dir.isDirectory()))
			dir.mkdirs();
		File target = new File(dir, fileName + ".tmp");
		OutputStream os = null;
		try {
			os = new FileOutputStream(target);
			byte[] buffer = new byte[4 * 1024];
			int len = -1;
			while ((len = in.read(buffer)) != -1) {
				os.write(buffer, 0, len);
				if (onFileSaveListener != null) {
					onFileSaveListener.onFileSaving(len);
				}
			}
			os.flush();
			target.renameTo(new File(dir, fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	
	/**
	 * 文件下载的监听器
	 * 
	 * @author foxchan@live.cn
	 * @version 1.0.0
	 * @create 2014年1月9日
	 */
	public static interface OnFileDownloadListener {

		/**
		 * 当文件正在下载的时候将触发该事件
		 * 
		 * @param totalLength
		 *            正在下载的文件的总长度，单位：字节
		 */
		void onFileDownloading(long totalLength);

	}

	public static interface OnFileSaveListener {

		/**
		 * 当文件正在保存的时候将触发该事件
		 * 
		 * @param savingLength
		 *            本次保存的文件的大小，单位：字节
		 */
		void onFileSaving(long savingLength);

	}
	
	/**
	 * 获得文件的拓展名
	 * 
	 * @param fileName
	 *            文件名
	 * @return 返回文件的拓展名
	 */
	public static String getExt(String fileName) {
		String ext = "";
		int pointIndex = fileName.lastIndexOf(".");
		if (pointIndex > -1) {
			ext = fileName.substring(pointIndex + 1).toLowerCase();
		}
		return ext;
	}

	public static boolean isJpg(String ext) {
		if (TextUtils.isEmpty(ext))
			return false;
		return "jpg|jpeg".contains(ext.toLowerCase());
	}

	public static boolean isPng(String ext) {
		if (TextUtils.isEmpty(ext))
			return false;
		return "png".contains(ext.toLowerCase());
	}

	public static boolean isGif(String ext) {
		if (TextUtils.isEmpty(ext))
			return false;
		return "gif".contains(ext.toLowerCase());
	}
	
	public static void persistFileToSdcard(byte [] buff,String filePath , String fileName){
		File dir = new File(filePath);
		if (!dir.exists())
			dir.mkdirs();
		File target = new File(dir, fileName);
		OutputStream os = null;
		try {
			os = new FileOutputStream(target);
            os.write(buff);
			os.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void renameFile(String oldFileName,String newFileName){
		File file=new File(oldFileName);
		file.renameTo(new File(newFileName));
	}
}
