package com.gofish.szhd.model;


import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gofish.szhd.event.RequestResultEvent;
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

import de.greenrobot.event.EventBus;
import in.srain.cube.views.list.ListPageInfo;
import in.srain.cube.views.list.PagedListDataModel;
import me.storm.volley.data.RequestManager;

public class OutlineListDataModel extends PagedListDataModel<OutlineItem> {

    public OutlineListDataModel(int numPerPage) {
        mListPageInfo = new ListPageInfo<OutlineItem>(numPerPage);
    }

    @Override
    protected void doQueryData() {
        String update_dt;
        OutlineItem lastItem = null;

        if (mListPageInfo.getStart() > 0) {
            lastItem = mListPageInfo.getItem(mListPageInfo.getStart() - 1);
        }

        if (lastItem == null) {
            Date cur_dt = new Date();
            DateFormat dt_format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String cur_dt_str = dt_format.format(cur_dt);

            L.v("loadData() cur_dt_str = " + cur_dt_str);
            update_dt = cur_dt_str;
        } else {
            update_dt = lastItem.update_dt;
        }

        List<OutlineItem> items = queryDataFromDatabase(update_dt, mListPageInfo.getNumPerPage());
        if (items == null) {
            queryDataFromNetwork(update_dt, mListPageInfo.getNumPerPage());
        } else {
            setRequestResult(items, true);
            EventBus.getDefault().post(new RequestResultEvent(true, null));
        }
    }

    private void queryDataFromNetwork(String update_dt, int range) {
        String url = null;

        url = String.format("http://wx4399.com:5000/get_outlines?update_dt=%s&range=%d",
                URLEncoder.encode(update_dt), range);
        L.v("queryDataFromNetwork(): url = " + url);

        JsonObjectRequest req = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jso) {
                        // read json to item list
                        List<OutlineItem> itemList = null;
                        try {
                            OutlineItem[] itemArray = new Gson().fromJson(
                                    jso.getJSONArray("items").toString(), OutlineItem[].class);
                            itemList = Arrays.asList(itemArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //save to db
                        if (itemList != null) {
                            try {
                                ActiveAndroid.beginTransaction();
                                for (OutlineItem item : itemList) {
                                    OutlineItem dbItem = new Select()
                                            .from(OutlineItem.class)
                                            .where("oid = ?", item.oid)
                                            .executeSingle();
                                    if (dbItem == null) {
                                        item.save();
                                    }
                                }
                                ActiveAndroid.setTransactionSuccessful();
                            }
                            finally {
                                ActiveAndroid.endTransaction();
                            }
                        }

                        setRequestResult(itemList, true);
                        EventBus.getDefault().post(new RequestResultEvent(true, null));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        setRequestFail();
                        EventBus.getDefault().post(new RequestResultEvent(false, error.getMessage()));
                    }
                }
        );
        RequestManager.addRequest(req, null);
    }

    private List<OutlineItem> queryDataFromDatabase(String update_dt, int nr) {
        List<OutlineItem> items = null;

        try {
            items = new Select()
                    .from(OutlineItem.class)
                    .where("update_dt < ?", update_dt)
                    .orderBy("update_dt DESC")
                    .limit(nr)
                    .execute();
            if (items != null && items.size() == 0) {
                items = null;
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return items;
    }
}
