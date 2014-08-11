package com.hrcx.selfphotovideocompare.checkview;

public interface CheckAction 
{
	
	// 设置验证码暂时定为四个，但是以后可以改
	public void setCheckNum(int [] chenckNum);
	
	// 获取验证码
	public int [] getCheckNum();
	
	// 更新验证码显示
	public void invaliChenkNum();
}
