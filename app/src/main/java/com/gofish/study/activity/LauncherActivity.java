package com.gofish.study.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gofish.study.DynmicWidgetActivity;


public class LauncherActivity extends ListActivity {
    public static final String [] options = {
            "ListView - BaseAdapter",
            "ListView - BaseAdapter Helper",
            "Animation - TweenAnimation",
            "Service - start&bind",
            "Handler - Demo1",
            "Handler - Demo2: Download pic",
            "Dynamic Add / Remove Widget",
            "Fragment Tab",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, options));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent;

        switch (position) {
            default:
            case 0:
                intent = new Intent(this, BaseAdapterLvActivity.class);
                break;
            case 1:
                intent = new Intent(this, BaseAdapterHelperActivity.class);
                break;
            case 2:
                intent = new Intent(this, TweenAnimActivity.class);
                break;
            case 3:
                intent = new Intent(this, TestServiceActivity.class);
                break;
            case 4:
                intent = new Intent(this, TestMessageHandlerActivity.class);
                break;
            case 5:
                intent = new Intent(this, TestMessageHandler2Activity.class);
                break;
            case 6:
                intent = new Intent(this, DynmicWidgetActivity.class);
                break;
            case 7:
                intent = new Intent(this, HomeActivity.class);
                break;
        }

        startActivity(intent);
    }
}
