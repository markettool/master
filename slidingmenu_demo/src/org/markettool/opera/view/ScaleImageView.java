package org.markettool.opera.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class ScaleImageView extends ImageView{
	
	protected float centerX = 0 ;
	protected float centerY = 0 ;
	public ScaleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void down(){
			centerX = getWidth() / 2.0f; 
			centerY = getHeight() / 2.0f; 
			RotateAnimation rotateAnimation = new RotateAnimation(0, 180, centerX,centerY); 
		    rotateAnimation.setDuration(600); 
		    rotateAnimation.setFillAfter(true); 
		    rotateAnimation.setAnimationListener(alDown);
		    startAnimation(rotateAnimation); 
	}
	public void up(){
			centerX = getWidth() / 2.0f; 
			centerY = getHeight() / 2.0f; 
			RotateAnimation rotateAnimation = new RotateAnimation(180, 360, centerX,centerY); 
		    rotateAnimation.setDuration(600); 
		    rotateAnimation.setFillAfter(true); 
		    rotateAnimation.setAnimationListener(alUp);
		    startAnimation(rotateAnimation); 
	}
	
	AnimationListener alDown = new AnimationListener() {
		@Override
		public void onAnimationStart(Animation animation) {
			 
			    
		}
		@Override
		public void onAnimationRepeat(Animation animation) {
		}
		
		@Override
		public void onAnimationEnd(Animation animation) {
		}
	};
	
	AnimationListener alUp = new AnimationListener() {
		
		@Override
		public void onAnimationStart(Animation animation) {
		}
		
		@Override
		public void onAnimationRepeat(Animation animation) {
			
		}
		
		@Override
		public void onAnimationEnd(Animation animation) {
		}
	};
	
	/**
	 * start浮动动画
	 */
	public void startFloat(){
		TranslateAnimation mTranslateAnimation = new TranslateAnimation(0, 0, 0f, -5f);
			mTranslateAnimation.setDuration(500);  
			mTranslateAnimation.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					TranslateAnimation mTranslateAnimation2 = new TranslateAnimation(0, 0, -5f, 5f);
					mTranslateAnimation2.setDuration(1000);  
					mTranslateAnimation2.setRepeatCount(Animation.INFINITE);  
					mTranslateAnimation2.setRepeatMode(Animation.REVERSE);  
					startAnimation(mTranslateAnimation2);
				}
			});
			startAnimation(mTranslateAnimation);
	}
	/**
	 * stop 浮动动画
	 */
	public void stopFload(){
		this.clearAnimation();
	}
	
}
