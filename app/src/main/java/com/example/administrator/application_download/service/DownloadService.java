package com.example.administrator.application_download.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;

import com.example.administrator.application_download.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 樱满集0_0 on 2017/5/25.
 */

public class DownloadService extends IntentService {
    public DownloadService() {
        super("download");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Notification.Builder builder=new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("下载文件")
                .setProgress(100,0,false)
                .setContentText("已下载0%");
        Notification notification=builder.build();//通过build()方法可以用Builder获得notification
        NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manager.notify(11,notification);
        try {
            String url = "http://mirror.bit.edu.cn/apache/tomcat/tomcat-7/v7.0.78/bin/apache-tomcat-7.0.78-windows-x64.zip";
            URL myUrl=new URL(url);
            HttpURLConnection connection = (HttpURLConnection)myUrl.openConnection();
            int total=connection.getContentLength();
            int downloadSize=0;
            byte[] bytes=new byte[1024];
            int in;
            InputStream input=connection.getInputStream();
            while((in=input.read())!=-1)
            {
                downloadSize=downloadSize+in;
                int progress=(int)(downloadSize*1.0/total*100);
                builder.setContentText("已下载"+progress+"%");
                builder.setProgress(100,progress,false);
                manager.notify(11,notification);
            }
            input.close();

        }
        catch (Exception e)
        {
            System.out.println("下载失败");
            System.out.println(e.getMessage());
        }
    }
}
