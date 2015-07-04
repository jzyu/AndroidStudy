package com.gofish.study.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import com.gofish.study.R;
import com.gofish.study.service.MyService;
import com.gofish.study.utils.L;

/*
startService和bindService的使用场景，有网友这么说：

1.通过startService开启的服务.一旦服务开启, 这个服务和开启他的调用者之间就没有任何的关系了.
  调用者不可以访问 service里面的方法. 调用者如果被系统回收了或者调用了onDestroy方法, service还会继续存在
2.通过bindService开启的服务,服务开启之后,调用者和服务之间 还存在着联系 , 一旦调用者挂掉了.service也会跟着挂掉
 */

public class TestServiceActivity extends Activity implements View.OnClickListener{
    public static final String TAG = "TestServiceActivity";
    private boolean mIsBind = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        L.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_service);
        findViewById(R.id.button_start_service).setOnClickListener(this);
        findViewById(R.id.button_stop_service).setOnClickListener(this);
        findViewById(R.id.button_bind_service).setOnClickListener(this);
        findViewById(R.id.button_unbind_service).setOnClickListener(this);
        findViewById(R.id.button_start_foreground_service).setOnClickListener(this);
        findViewById(R.id.button_stop_foreground_service).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        L.d(TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.d(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        L.d(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        L.d(TAG, "onStop()");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_start_service:
                L.d(TAG, "startService button clicked");
                startService(new Intent(this, MyService.class));
                break;
            case R.id.button_stop_service:
                L.d(TAG, "stopService button clicked");
                stopService(new Intent(this, MyService.class));
                break;
            case R.id.button_bind_service:
                L.d(TAG, "bindService button clicked");
                // 重复bind没事
                mIsBind = bindService(new Intent(this, MyService.class), mServiceConn, BIND_AUTO_CREATE);
                break;
            case R.id.button_unbind_service:
                L.d(TAG, "unbindService button clicked");
                if (mIsBind) {
                    // 未bind的unbind会引发异常
                    unbindService(mServiceConn);
                    mIsBind = false;
                }
                break;
        }
    }

    private ServiceConnection mServiceConn = new ServiceConnection() {
        public static final String TAG = "ServiceConnection";

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            L.d(TAG, "onServiceConnected()");
            MyService.MyBinder myBinder = (MyService.MyBinder)service;
            myBinder.startDownload("www.baidu.com");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            L.d(TAG, "onServiceDisconnected()");
        }
    };
}
