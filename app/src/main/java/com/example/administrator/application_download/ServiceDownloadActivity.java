package com.example.administrator.application_download;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.administrator.application_download.service.NotificationService;

public class ServiceDownloadActivity extends Activity {
    String downloadUrl="http://sw.bos.baidu.com/sw-search-sp/software/1e41f08ea1bea/QQ_8.9.2.20760_setup.exe";//下载链接
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_download);
    }
    public void startService(View view)
    {
        Intent intent=new Intent(this, NotificationService.class);
        intent.putExtra("downloadUrl",downloadUrl);
        startService(intent);
    }
}
