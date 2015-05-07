package com.infzm.slidingmenu.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class InterceptTouchReletiveLayout extends RelativeLayout {

	public InterceptTouchReletiveLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				for (int i = 0; i < getChildCount(); i++) {
					View child = getChildAt(i);
					if (child.getVisibility() == View.VISIBLE) {
						child.performClick();
						break;
					}
				}
				break;

			default:
				break;
		}
		return true;
	}

}
