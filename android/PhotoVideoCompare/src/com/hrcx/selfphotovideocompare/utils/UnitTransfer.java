package com.hrcx.selfphotovideocompare.utils;

import android.content.Context;

/**
 * Android大小单位转换工具�?
 * 
 */
public class UnitTransfer {
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
