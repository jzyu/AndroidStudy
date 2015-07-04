package com.gofish.study.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.gofish.study.R;
import com.gofish.study.utils.L;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;

public class TestMessageHandler2Activity extends Activity implements View.OnClickListener{
    public static final String TAG = "TestMessageHandler2Activity";

    private MyThread mWorkThread;
    private Bitmap mBitmap;
    private ProgressDialog mPrgDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_message_handler2);

        findViewById(R.id.button_download_image).setOnClickListener(this);
    }

    class MyHandler extends Handler {
        public static final String TAG = "MyHandler";
        @Override
        public void handleMessage(Message msg) {
            L.d(TAG, "handleMessage. ThreadID=" + Thread.currentThread().getId());
            super.handleMessage(msg);

            ImageView imgView = (ImageView)findViewById(R.id.image_view);
            imgView.setImageBitmap(mBitmap);
            mPrgDlg.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_start_download:
                if (mWorkThread == null) {
                    mPrgDlg = ProgressDialog.show(TestMessageHandler2Activity.this, "", "Loading...");
                    mWorkThread = new MyThread(new MyHandler());
                    mWorkThread.start();
                }
                break;
        }
    }

    class MyThread extends Thread{
        public static final String TAG = "MyThread";
        Handler handler;

        public MyThread(Handler handler) {
            L.d(TAG, "constructed");
            this.handler = handler;
        }

        @Override
        public void run() {
            mBitmap = downloadPic();
            handler.sendEmptyMessage(0);
        }

        public static final String PIC_URL = "http://www.szhuodong.com/uploadfile/logo.png";

        private Bitmap downloadPic(){
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpReq = new HttpGet(PIC_URL);

            try {
                HttpResponse response = httpClient.execute(httpReq);
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    L.e(TAG, "DownloadPic() Error=" + statusCode);
                }

                HttpEntity entity = response.getEntity();
                if (entity != null){
                    InputStream inputStream = null;
                    try {
                        inputStream = entity.getContent();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        return bitmap;
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        entity.consumeContent();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
