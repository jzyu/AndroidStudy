package com.gofish.study.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.zhy_baseadapterhelper.CommonAdapter;
import com.example.zhy_baseadapterhelper.ViewHolder;
import com.gofish.study.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;


public class BaseAdapterHelperActivity extends Activity {

    RequestQueue mReqQueue = null;
    List<Outline> mItemList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_adapter);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button:
                        doHttpGet();
                        break;
                }
            }
        });

        mReqQueue = Volley.newRequestQueue(this);
    }

    private void doHttpGet() {
        JsonObjectRequest req = new JsonObjectRequest("http://wx4399.com:5000/get_outlines", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jso) {
                        try {
                            //Log.d("TAG", str);
                            Outline[] items = new Gson().fromJson(
                                    jso.getJSONArray("items").toString(), Outline[].class);
                            mItemList = Arrays.asList(items);
                            setListContent();
                        } catch (JSONException e) {
                            Log.e("TAG", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("TAG", volleyError.getMessage(), volleyError);
                    }
                }
        );
        mReqQueue.add(req);
    }

    public static class Outline {
        public int id;
        public short category_id;
        public String title;
        public String desc;
        public String detail_url;
        public String img_url;
        public String update_dt;
    }

    private void setListContent() {
        ListView lv = (ListView)findViewById(R.id.lv);

        lv.setAdapter(new CommonAdapter<Outline>(this, mItemList, R.layout.listitem) {
            @Override
            public void convert(ViewHolder helper, Outline item) {
                helper.setText(R.id.ItemTitle, item.title);
                helper.setText(R.id.ItemText, item.desc);
                helper.setText(R.id.ItemUpdateDt, item.update_dt);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setTitle("select index = " + position);
            }
        });
    }
}
