package com.gofish.study.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.gofish.study.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;


public class BaseAdapterLvActivity extends Activity {

    List<Outline> mItemList = null;
    RequestQueue mReqQueue = null;

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

    private class MyBaseAdapter extends BaseAdapter {
        private LayoutInflater mInflater = null;

        private MyBaseAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return mItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.v("MyBaseAdapter", "getView " + position + " " + convertView);

            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.listitem, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.ItemTitle);
                viewHolder.text = (TextView) convertView.findViewById(R.id.ItemText);
                viewHolder.update_dt = (TextView) convertView.findViewById(R.id.ItemUpdateDt);
                viewHolder.image = (NetworkImageView) convertView.findViewById(R.id.network_image_view);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Outline item = mItemList.get(position);
            viewHolder.title.setText(item.title);
            viewHolder.text.setText(item.desc);
            viewHolder.update_dt.setText("发布时间：" + item.update_dt);

            viewHolder.image.setDefaultImageResId(R.drawable.pic_listitem_null);
            viewHolder.image.setErrorImageResId(R.drawable.pic_listitem_null);
            ImageLoader imageLoader = new ImageLoader(mReqQueue, new BitmapCache());
            viewHolder.image.setImageUrl(item.img_url, imageLoader);
            return convertView;
        }

        private final class ViewHolder{
            TextView title;
            TextView text;
            NetworkImageView image;
            TextView update_dt;
        }
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
        MyBaseAdapter adapter = new MyBaseAdapter(this);
        ListView lv = (ListView)findViewById(R.id.lv);

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setTitle("select index = " + position);
            }
        });
    }

    public class BitmapCache implements ImageLoader.ImageCache {

        private LruCache<String, Bitmap> mCache;

        public BitmapCache() {
            int maxSize = 10 * 1024 * 1024;
            mCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mCache.put(url, bitmap);
        }

    }
}
