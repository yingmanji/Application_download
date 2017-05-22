package com.example.administrator.application_download.service;

import android.Manifest;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.administrator.application_download.DownloadServiceActivity;
import com.example.administrator.application_download.R;
import com.example.administrator.application_download.ServiceDownloadActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/19.
 */

public class NotificationService extends IntentService {
    private static final String TAG = NotificationService.class.getSimpleName();
    private long fileLength, downloadLength;//注意必须换成长整型！
    private NotificationManager notificationManager;
    private Notification notification;
    private RemoteViews rViews;
    File qqFile;
    private Handler handler = new Handler();
    private Runnable run = new Runnable() {
        public void run() {
            //System.out.println("#执行run方法");
            rViews.setProgressBar(R.id.downloadFile_pb, 100, (int)(downloadLength * 100 / fileLength), false);
            //System.out.println(21498376*100/43915707);
            //System.out.println("#进度"+downloadLength * 100 / fileLength+"#dl"+downloadLength+"#fl"+fileLength);
            notification.contentView = rViews;
            notificationManager.notify(0, notification);
            handler.postDelayed(run, 1000);//一秒一次的定时器
        }
    };

    public NotificationService() {
        super("notification");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        String downloadUrl = bundle.getString("downloadUrl", "获取失败");
        File dirs = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download");
        // 检查文件夹是否存在，不存在则创建
        if (!dirs.exists()) {
            System.out.println("文件夹存在");
            dirs.mkdir();
        }
        qqFile = new File(dirs,"/qq.apk");
        System.out.println(qqFile.getPath());
        //设置通知栏
        setNotification();
        //开始下载
        downloadFile(downloadUrl, qqFile);
        //移除通知栏
        notificationManager.cancel(0);
        // 广播出去，由广播接收器来处理下载完成的文件
        Intent sendIntent = new Intent("com.example.administrator.application_download");
        // 把下载好的文件的保存地址加进Intent
        sendIntent.putExtra("downloadFile", qqFile.getPath());
        sendBroadcast(sendIntent);
    }

    public void setNotification() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification = new Notification(R.mipmap.ic_launcher, "版本更新下载", System.currentTimeMillis());
        Intent intentNotifi = new Intent(this, DownloadServiceActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentNotifi, 0);
        notification.contentIntent = pendingIntent;
        // 加载Notification的布局文件
        rViews = new RemoteViews(getPackageName(), R.layout.downloadfile_layout);
        // 设置下载进度条
        rViews.setProgressBar(R.id.downloadFile_pb, 100, 0, false);
        notification.contentView = rViews;
        notificationManager.notify(0, notification);
    }

    private void downloadFile(String downloadUrl, File file) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "找不到保存下载文件的目录");
            e.printStackTrace();
        }
        InputStream ips = null;
        try {
            URL url = new URL(downloadUrl);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("GET");
            huc.setReadTimeout(10000);
            huc.setConnectTimeout(3000);
            fileLength = Integer.valueOf(huc.getHeaderField("Content-Length"));
            System.out.println("#文件大小"+fileLength);
            ips = huc.getInputStream();
            // 拿到服务器返回的响应码
            int hand = huc.getResponseCode();
            if (hand == 200) {
                // 开始检查下载进度
                handler.post(run);
                // 建立一个byte数组作为缓冲区，等下把读取到的数据储存在这个数组
                byte[] buffer = new byte[43915707];
                int len = 0;
                while ((len = ips.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    downloadLength = downloadLength + len;
                }
                System.out.println("下载完成");
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

    public void onDestroy() {
        // 移除定時器
        handler.removeCallbacks(run);
        super.onDestroy();
    }
}
