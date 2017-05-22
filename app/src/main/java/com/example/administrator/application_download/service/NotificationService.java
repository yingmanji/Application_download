package com.example.administrator.application_download.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Environment;

import com.example.administrator.application_download.R;
import com.example.administrator.application_download.ServiceDownloadActivity;

import java.io.File;

/**
 * Created by Administrator on 2017/5/19.
 */

public class NotificationService extends IntentService {
    String downloadUrl;
    String savePath= Environment.getExternalStorageDirectory()+"/qq.apk";
    File qqFile;

    public NotificationService()
    {
        super("notification");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        qqFile=new File(savePath);
        downloadUrl=intent.getStringExtra("downloadUrl");
    }
    public void sendNotification()
    {
        Notification.Builder builder=new Notification.Builder(this);//添加通知栏的显示内容
        int requestCode=(int) System.currentTimeMillis();
        Intent myIntent=new Intent(this, ServiceDownloadActivity.class);
        int flags= PendingIntent.FLAG_CANCEL_CURRENT;
        PendingIntent pIntent=PendingIntent.getActivity(this,requestCode,myIntent,flags);
        builder.setContentTitle("title").setContentText("myText")
                .setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pIntent)
                .setAutoCancel(true);
        NotificationManager notiManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notiManager.notify(requestCode,builder.build());
    }
}
