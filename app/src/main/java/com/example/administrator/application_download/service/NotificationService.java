package com.example.administrator.application_download.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.example.administrator.application_download.R;
import com.example.administrator.application_download.ServiceDownloadActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/5/19.
 */

public class NotificationService extends IntentService {
    String downloadUrl;
    String savePath= Environment.getExternalStorageDirectory()+"/qq.apk";
    private static final String TAG = NotificationService.class.getSimpleName();
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
    private void downloadFile(String downloadUrl, File file){
                FileOutputStream fos = null;
                try {
                         fos = new FileOutputStream(file);
                     } catch (FileNotFoundException e) {
                         Log.e(TAG, "找不到保存下载文件的目录");
                         e.printStackTrace();
                     }
                 InputStream ips = null;
                 try {
                        URL url=new URL(downloadUrl);
                        HttpURLConnection huc = (HttpURLConnection) downloadUrl.openConnection();
                         huc.setRequestMethod("GET");
                         huc.setReadTimeout(10000);
                         huc.setConnectTimeout(3000);
                     fileLength = Integer.valueOf(huc.getHeaderField("Content-Length"));
                         ips = huc.getInputStream();
                         // 拿到服务器返回的响应码
             hand = huc.getResponseCode();
                         if (hand == 200) {
                                 // 开始检查下载进度
                                 handler.post(run);
                                 // 建立一个byte数组作为缓冲区，等下把读取到的数据储存在这个数组
                                 byte[] buffer = new byte[8192];
                                 int len = 0;
                                 while ((len = ips.read(buffer)) != -1) {
                                         fos.write(buffer, 0, len);
                                         downloadLength = downloadLength + len;
                                     }
                             } else {
                                 Log.e(TAG, "服务器返回码" + hand);
                             }

                     } catch (ProtocolException e) {
                         e.printStackTrace();
                     } catch (MalformedURLException e) {
                         e.printStackTrace();
                     } catch (IOException e) {
                         e.printStackTrace();
                     } finally {
                         try {
                                 if (fos != null) {
                                         fos.close();
                                     }
                                 if (ips != null) {
                                         ips.close();
                                     }
                             } catch (IOException e) {
                                 e.printStackTrace();
                             }
                     }
             }
}
