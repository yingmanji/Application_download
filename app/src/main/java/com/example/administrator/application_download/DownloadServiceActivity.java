package com.example.administrator.application_download;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.administrator.application_download.service.NotificationService;

import java.io.File;
import java.util.ArrayList;

public class DownloadServiceActivity extends AppCompatActivity {
    private static boolean mStoragePermissionCheck = false;     // group:android.permission-group.STORAGE
    private static final int MY_PERMISSIONS_ALL = 1;
    private static boolean mAllPermissionGranted = true;
    private static boolean mContactsPermissionCheck = false;
    private Context mContext;
    BroadcastReceiver receiver;
    IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_service);
    }
    public void startService(View view)
    {
        //checkPermissions();
        //System.out.println("#获取权限失败"+deniedPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE));
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
        System.out.println(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED);
        final String downloadUrl="http://gdown.baidu.com/data/wisegame/0852f6d39ee2e213/QQ_676.apk";//下载链接
        Intent intent=new Intent(this, NotificationService.class);
        Bundle bundle = new Bundle();
        bundle.putString("downloadUrl", downloadUrl);
        intent.putExtras(bundle);
        startService(intent);
        // 设置广播接收器，当新版本的apk下载完成后自动弹出安装界面
        intentFilter = new IntentFilter("com.example.administrator.myapplication");
        receiver = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {
                if(intent!=null) {
                    /*Intent install = new Intent(Intent.ACTION_VIEW);
                    String pathString = intent.getStringExtra("downloadFile");
                    install.setDataAndType(Uri.fromFile(new File(pathString)), "application/vnd.android.package-archive");
                    install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(install);*/
                    Log.e("OpenFile", intent.getStringExtra("downloadFile"));
                    //System.out.println("#df"+intent.getStringExtra("downloadFile"));
                    Intent sendIntent = new Intent();
                    Uri uri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//7.0启动姿势<pre name="code" class="html">    //com.xxx.xxx.fileprovider为上述manifest中provider所配置相同；apkFile为问题1中的外部存储apk文件
                        uri = FileProvider.getUriForFile(context, "com.example.administrator.application_download.fileprovider", new File(intent.getStringExtra("downloadFile")));
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        sendIntent.setDataAndType(uri,"application/vnd.android.package-archive");
                        startActivity(intent);
                    }

                }
            }
        };
        registerReceiver(receiver, intentFilter);
    }
    protected void onDestroy() {
        // 移除广播接收器
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }
    /*public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }*/
    private void checkPermissions() {

        ArrayList<String> arrayList = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                arrayList.add(Manifest.permission.READ_CONTACTS);
                mContactsPermissionCheck = false;
            } else {
                mContactsPermissionCheck = true;
            }
        }

        if (mContactsPermissionCheck) {

        } else {
            String[] strings = arrayList.toArray(new String[arrayList.size()]);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(strings, MY_PERMISSIONS_ALL);
            }
        }

    }
    private boolean deniedPermission(String permission){
        System.out.println(ContextCompat.checkSelfPermission(mContext,permission)==PackageManager.PERMISSION_DENIED);
        return    ContextCompat.checkSelfPermission(mContext,permission)==PackageManager.PERMISSION_DENIED;
    }
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1
                    : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] ==PackageManager.PERMISSION_GRANTED) {
                    System.out.println("获取权限成功");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    System.out.println("获取权限失败");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
}
