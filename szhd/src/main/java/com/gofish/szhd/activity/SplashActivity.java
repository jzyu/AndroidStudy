package com.gofish.szhd.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.android.volley.VolleyError;
import com.gofish.szhd.MyApplication;
import com.gofish.szhd.R;
import com.gofish.szhd.model.Outlines;


public class SplashActivity extends Activity {
    public static final int SHOW_TIME_MIN_MS = 2000;

    public static final int SUCCESS = 0;
    public static final int FAILURE = 1;
    public static final int OFFLINE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                int result;
                long startMs = System.currentTimeMillis();

                result = loadingCache();

                long loadingMs = System.currentTimeMillis() - startMs;
                if (loadingMs < SHOW_TIME_MIN_MS) {
                    try {
                        Thread.sleep(SHOW_TIME_MIN_MS - loadingMs);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                return result;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                startActivity(new Intent(SplashActivity.this, OutlineListActivity.class));
                finish();
            }
        }.execute();

    }

    private int loadingCache() {
        return SUCCESS;
    }
}
