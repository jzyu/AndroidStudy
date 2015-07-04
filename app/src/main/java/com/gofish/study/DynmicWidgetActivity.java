package com.gofish.study;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class DynmicWidgetActivity extends Activity implements View.OnClickListener{
    List<TextView> mTags;
    ViewGroup mTagsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynmic_widget);

        initTagsView();

        findViewById(R.id.button_add).setOnClickListener(this);
        findViewById(R.id.button_remove).setOnClickListener(this);
    }

    private void initTagsView() {
        mTagsContainer = (ViewGroup)findViewById(R.id.tag_container);
        mTags = new ArrayList<TextView>();
    }

    private void addTag(String text) {
        TextView tag = (TextView)View.inflate(this, R.layout.widget_tag, null);
        tag.setText(text + " " + String.valueOf(mTags.size()));

        mTagsContainer.addView(tag);
        mTags.add(tag);
    }

    private void removeTag() {
        if (mTags.size() == 0) return;
        TextView tag = mTags.get(mTags.size() - 1);
        mTags.remove(mTags.size() - 1);
        mTagsContainer.removeView(tag);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add:
                addTag("new tag");
                break;
            case R.id.button_remove:
                removeTag();
                break;
        }
    }
}
