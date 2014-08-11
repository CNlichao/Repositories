package com.docdeal.util;

import com.docdeal.R;
import com.docdeal.adapterview.TitleBar;

import android.app.Activity;
import android.view.Window;

public class ActivityUtil {
	 //为了实现每次使用该类时不创建新的对象而创建的静态对象  
    private static ActivityUtil instance; 
    //为了实现每次使用该类时不创建新的对象而创建的静态对象  
    private TitleBar titleBar; 
    //构造方法  
    private ActivityUtil(){}  
    //实例化一次  
    public synchronized static ActivityUtil getInstance(){   
        if (null == instance) {   
            instance = new ActivityUtil();   
        }   
        return instance;   
    } 
//初始化activity,设置标题栏等相关信息	
	public void initActivity(Activity activity,int contentview){
		Window win = activity.getWindow();
        win.requestFeature(Window.FEATURE_CUSTOM_TITLE);//注意顺序
        activity.setContentView(contentview);//注意顺序
        win.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);//注意顺序
        //设置标题栏
        titleBar=new TitleBar(activity);
        //注册到activity管理类中
        SysApplication.getInstance().addActivity(activity); 
	}
	public TitleBar getTitleBar() {
		return titleBar;
	}
	public void setTitleBar(TitleBar titleBar) {
		this.titleBar = titleBar;
	}

}
