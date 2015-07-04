package com.gofish.szhd;


import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.gofish.szhd.model.Outlines;
import com.gofish.utils.T;

import me.storm.volley.data.RequestManager;

public class MyApplication extends Application{

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        RequestManager.init(this);
        T.init(this);
        ActiveAndroid.initialize(this);
        //Cube.onCreate(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }

    public static MyApplication getInstance() {return mInstance;}

    public Outlines getOutlines() {return Outlines.getInstance();}
}
