package com.gofish.study.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.gofish.study.R;


public class TweenAnimActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tween_anim);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        findViewById(R.id.hello).startAnimation(anim);
    }
}
