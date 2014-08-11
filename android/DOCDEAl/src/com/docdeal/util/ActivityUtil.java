package com.docdeal.util;

import com.docdeal.R;
import com.docdeal.adapterview.TitleBar;

import android.app.Activity;
import android.view.Window;

public class ActivityUtil {
	 //Ϊ��ʵ��ÿ��ʹ�ø���ʱ�������µĶ���������ľ�̬����  
    private static ActivityUtil instance; 
    //Ϊ��ʵ��ÿ��ʹ�ø���ʱ�������µĶ���������ľ�̬����  
    private TitleBar titleBar; 
    //���췽��  
    private ActivityUtil(){}  
    //ʵ����һ��  
    public synchronized static ActivityUtil getInstance(){   
        if (null == instance) {   
            instance = new ActivityUtil();   
        }   
        return instance;   
    } 
//��ʼ��activity,���ñ������������Ϣ	
	public void initActivity(Activity activity,int contentview){
		Window win = activity.getWindow();
        win.requestFeature(Window.FEATURE_CUSTOM_TITLE);//ע��˳��
        activity.setContentView(contentview);//ע��˳��
        win.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);//ע��˳��
        //���ñ�����
        titleBar=new TitleBar(activity);
        //ע�ᵽactivity��������
        SysApplication.getInstance().addActivity(activity); 
	}
	public TitleBar getTitleBar() {
		return titleBar;
	}
	public void setTitleBar(TitleBar titleBar) {
		this.titleBar = titleBar;
	}

}
