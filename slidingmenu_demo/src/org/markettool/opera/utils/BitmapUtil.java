package org.markettool.opera.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.markettool.opera.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.util.Log;

public class BitmapUtil {
	
	public static Bitmap getOriginBitmap(String path){
		try {
			Bitmap bitmap=BitmapFactory.decodeStream(new FileInputStream(path));
			return bitmap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Bitmap getThumbilBitmap(String srcPath,int width)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 通过这个bitmap获取图片的宽和高&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, options);
        if (bitmap == null)
        {
        	Log.e("majie", "bitmap为空");
//        	return null;
        }
        float realWidth = options.outWidth;
        float realHeight = options.outHeight;
        Log.e("majie", "真实图片高度：" + realHeight + "宽度:" + realWidth);
        // 计算缩放比&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
        int scale = (int) ((realHeight < realWidth ? realHeight : realWidth) / width);
        if (scale <= 0)
        {
            scale = 1;
        }
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        // 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来的。&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
        bitmap = BitmapFactory.decodeFile(srcPath, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Log.e("majie", "缩略图高度：" + h + "宽度:" + w);
        return bitmap;
    }
	
	public static void saveBitmapToSdcard(Bitmap bitmap ,String desPath){
		File file=new File(desPath);
        try {
            FileOutputStream out=new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)){
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}
	
	   /***
     * 将图片画在画布中间
     * @param bm
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getCanvasBitmap(Bitmap bm,int width,int height){
//    	if(bm.getWidth()>width){
//    		return bm;
//    	}
    	Bitmap bitmap=Bitmap.createBitmap(width, height, Config.ARGB_8888);
    	Canvas canvas=new Canvas(bitmap);
//    	int left=(width-bm.getWidth())/2;
//    	int top=(height-bm.getHeight())/2;
    	canvas.drawBitmap(bm, 0, 0, null);
    	canvas.save(Canvas.ALL_SAVE_FLAG);
    	canvas.restore();
    	return bitmap;
    }
    
    public static Bitmap getBitmapFromRes(Context context,int resId){
    	Bitmap bitmap=BitmapFactory.decodeResource(context.getResources(), resId);
    	return bitmap;
    }

}
