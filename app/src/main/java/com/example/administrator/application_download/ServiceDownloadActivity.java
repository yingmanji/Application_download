package com.example.administrator.application_download;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.administrator.application_download.service.NotificationService;

import java.util.ArrayList;

public class ServiceDownloadActivity extends Activity {
    private static boolean mStoragePermissionCheck = false;     // group:android.permission-group.STORAGE
    private static final int MY_PERMISSIONS_ALL = 1;
    private static boolean mAllPermissionGranted = true;
    private static boolean mContactsPermissionCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_download);
    }
    public void startService(View view)
    {
        checkPermissions();
        String downloadUrl="http://gdown.baidu.com/data/wisegame/0852f6d39ee2e213/QQ_676.apk";//下载链接
        Intent intent=new Intent(this, NotificationService.class);
        Bundle bundle = new Bundle();
        bundle.putString("downloadUrl", downloadUrl);
        intent.putExtras(bundle);
        startService(intent);
    }
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

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_ALL: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mAllPermissionGranted = false;
                            break;
                        }
                        mAllPermissionGranted = true;
                    }
                } else {
                    mAllPermissionGranted = false;
                }
                break;
            }
        }

        // 显示需要单独申请授权的Dialog
        if (mAllPermissionGranted) {

        } else {

        }
    }
}
