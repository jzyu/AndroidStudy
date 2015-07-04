package com.gofish.szhd.model;


import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gofish.utils.L;
import com.gofish.utils.T;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import me.storm.volley.data.RequestManager;

public class Outlines {
    private static Outlines mInstance;
    List<OutlineItem> mItems = new ArrayList<OutlineItem>();
    boolean mNetworkFailed = false;
    boolean mNetworkLoading = false;

    public static Outlines getInstance() {
        if(mInstance==null) {
            mInstance = new Outlines();
        }
        return mInstance;
    }

    public List<OutlineItem> getItemList() {return mItems;}

}
