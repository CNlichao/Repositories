package com.docdeal.activity;

import com.docdeal.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore.Audio;
import android.text.format.Time;
import android.util.Log;
import android.widget.RemoteViews;

/**
* ��Ϣ����
*
* @author Msquirrel
*
*/
public class MessageService extends Service {

   private String TAG = "-----------";

   private MessageThread messageThread = null;

   // ����鿴
   private Intent messageIntent = null;
   private PendingIntent messagePendingIntent = null;

   // ֪ͨ����Ϣ
   private int messageNotificationID = 1000;
   private Notification messageNotification = null;                              // �Ǿ����״̬��֪ͨ���󣬿�������icon�����֡���ʾ�������񶯵ȵȲ�����
   private NotificationManager messageNotificatioManager = null; // ��״̬��֪ͨ�Ĺ����࣬����֪ͨ�����֪ͨ�ȡ�
   private RemoteViews contentView = null;
   private String USERSIGN;//�û���
   private static MessageService messageService;

   @Override
   public IBinder onBind(Intent intent) {
       // TODO Auto-generated method stub
       return null;
   }
   
   @Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		 // ��ʼ��
	       messageNotification = new Notification();
	       messageNotification.icon=R.drawable.ic_launcher;

	     /*  //������ʾ
	      messageNotification.defaults |= Notification.DEFAULT_SOUND;//����
	      messageNotification.sound = Uri.withAppendedPath(
	               Audio.Media.INTERNAL_CONTENT_URI, "2");// ѡ�����嵥�ĵ�2�׸�����Ϣ����
	                    */
	    
	     /*  //����ʾ
	       messageNotification.defaults |= Notification.DEFAULT_LIGHTS;//��
	       messageNotification.ledARGB = 0xff00ff00;//�Ƶ���ɫ
	       messageNotification.ledOnMS = 300; //����ʱ��
	       messageNotification.ledOffMS = 1000; //���ʱ��
	       messageNotification.flags |= Notification.FLAG_SHOW_LIGHTS;//��ʾ��
	*/     
	       /*//����ʾ
	       messageNotification.defaults |= Notification.DEFAULT_VIBRATE;//��
	       long v[]= {0,100,200,300}; //��Ƶ��
	       messageNotification.vibrate = v;*/

	       messageNotification.flags |= Notification.FLAG_AUTO_CANCEL;// �����Ϣ��,����Ϣ�Զ��˳�
	       //messageNotification.flags |= Notification.FLAG_ONGOING_EVENT;// ���Ϸ�������Ϣ���г���
	       // messageNotification.flags|=Notification.FLAG_NO_CLEAR;//����Ϣ���ᱻ���

	       messageNotificatioManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	       messageIntent = new Intent(this, DocumentList .class);// �����Ϣ��,Ҫ��ת�Ľ���  ( ��Ӧ ��ϸ��Ϣ�Ľ��� )
           messageNotification.tickerText= "�ٺ٣�������Ϣ����";
	}

   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
	   String userName=getSharedPreferences("service", 0).getString("USERSIGN", null);
	   //�ж��û��Ƿ��ѵ�¼
	   if (userName!=null) {
		   messageIntent.putExtra("USERSIGN", userName);
		   // �����߳�
		   if(messageThread!=null){
		       messageThread.isRunning = false;// ����Ϊfalse��,�߳�����ѭ����������      
		   }  
			   messageThread = new MessageThread();// ���߳�ÿ10��,����һ����Ϣ���� 
		       messageThread.isRunning = true;// ����Ϊfalse��,�߳�����ѭ����������
		       messageThread.start();
		       Log.i(TAG, "startCommand");	  
	}  
	   return super.onStartCommand(intent, flags, startId);
   }

   /**
    * �ӷ������˻�ȡ��Ϣ
    */
   class MessageThread extends Thread {
       // ����Ϊfalse��,�߳�����ѭ��������
       public boolean isRunning = true;

       public void run() {
           while (isRunning) {
               try {

                   String serverMessage = getServerMessage();
                   
                   if (serverMessage != null && !"".equals(serverMessage)) {
                       // ����֪ͨ��
                       messageNotification.when=System.currentTimeMillis();
                       messageIntent.putExtra("message", serverMessage);// Ϊ��ͼ��Ӳ���
                       messagePendingIntent = PendingIntent.getActivity(
                               MessageService.this, 0, messageIntent,
                               PendingIntent.FLAG_CANCEL_CURRENT);// ����ͼװ�� �ӳ���ͼ
                       messageNotification.setLatestEventInfo(MessageService.this, "����", serverMessage, messagePendingIntent);
                       messageNotification.contentIntent = messagePendingIntent;// ���ӳ���ͼװ����Ϣ
                       messageNotificatioManager.cancel(messageNotificationID);//ȡ����ǰ֪ͨ
                       messageNotificatioManager.notify(messageNotificationID,
                               messageNotification);// ������Notification

                       Log.i(TAG, "������Ϣ");
                       //messageNotificatioManager.cancel(messageNotificationID-1);//����Ϣ����,����֮ǰ��һ����Ϣ(ֻ��ʾ������Ϣ)
                       // ���ú�������Ϣ��id��
                       //messageNotificationID++;
                   }
                   // ��Ϣ10����
                   Thread.sleep(10000);
                   // ��ȡ��������Ϣ
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
       }
   }

   /**
    * ģ�·��������͹�������Ϣ������ʾ��
    *
    * @return ���ط�����Ҫ���͵���Ϣ���������Ϊ�յĻ���������
    */
   public String getServerMessage() {
       Log.i(TAG, "getmessage");
       Time time=new Time();
       time.setToNow();
       return  time.toString();

   }

   @Override
   public void onDestroy() {
       // System.exit(0);

       messageThread.isRunning = false;
       // ���ߣ���ѡһ���Ƽ�ʹ��System.exit(0)�����������˳��ĸ��ɾ�
       // messageThread.isRunning = false;
       super.onDestroy();
       Log.i(TAG, "destroy");
   }

}