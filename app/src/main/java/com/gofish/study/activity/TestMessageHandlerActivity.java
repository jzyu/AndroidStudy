package com.gofish.study.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;

import com.gofish.study.R;
import com.gofish.study.utils.L;

//todo: add bimtmap download

public class TestMessageHandlerActivity extends Activity implements View.OnClickListener{
    public static final String TAG = "TestMessageHandlerActivity";
    private ProgressBar mProgressBar;
    private MyThread mWorkThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_message_handler);
        mProgressBar = (ProgressBar)findViewById(R.id.download_progress_bar);

        findViewById(R.id.button_start_download).setOnClickListener(this);
        findViewById(R.id.button_stop_download).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_start_download:
                if (mWorkThread == null) {
                    mWorkThread = new MyThread(new MyHandler());
                    mWorkThread.start();
                }
                break;
            case R.id.button_stop_download:
                mProgressBar.setProgress(0);
                if (mWorkThread != null) {
                    mWorkThread.stopMe();
                    mWorkThread = null;
                }
                break;
        }
    }

    private void progressIncrease(int percent) {
        mProgressBar.incrementProgressBy(percent);
        if (mProgressBar.getProgress() >= 100 && mWorkThread != null) {
            L.d(TAG, "percent = 100!");
            mWorkThread.stopMe();
            mWorkThread = null;
        }
    }

    class MyHandler extends Handler {
        public static final String TAG = "MyHandler";
        @Override
        public void handleMessage(Message msg) {
            L.d(TAG, "handleMessage. ThreadID=" + Thread.currentThread().getId());
            super.handleMessage(msg);

            int percent = msg.getData().getInt("percent");
            progressIncrease(percent);
        }
    }

    class MyThread extends Thread{
        public static final String TAG = "MyThread";
        Handler handler;
        private boolean finished = false;

        public MyThread(Handler handler) {
            L.d(TAG, "constructed");
            this.handler = handler;
        }

        public void stopMe() {
            L.d(TAG, "stopMe, ThreadID=" + Thread.currentThread().getId());
            finished = true;
        }

        @Override
        public void run() {
            try {
                while (! finished) {
                    Thread.sleep(1000);
                    Message msg = prepareMsg();
                    handler.sendMessage(msg);
                    L.d(TAG, "msg sent. ThreadID=" + Thread.currentThread().getId());
                }
            } catch (InterruptedException e) {
                L.d(TAG, "interrupted");
            }

            L.d(TAG, "run terminated!");
        }

        private Message prepareMsg() {
            Message msg = handler.obtainMessage();
            Bundle data = new Bundle();
            data.putInt("percent", 20);
            msg.setData(data);
            return msg;
        }
    }
}
