package com.gofish.study.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;

import com.gofish.study.Fragment.TimelineFragment;
import com.gofish.study.R;

public class HomeActivity extends Activity{

    FragmentManager mFm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mFm = getFragmentManager();
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rg_tab);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentTransaction transaction = mFm.beginTransaction();
                Fragment fragment;

                switch (checkedId) {
                    default:
                    case R.id.rb_timeline:
                        fragment = TimelineFragment.newInstance("这是动态");
                        break;
                    case R.id.rb_explore:
                        fragment = TimelineFragment.newInstance("这是发现");
                        break;
                    case R.id.rb_notify:
                        fragment = TimelineFragment.newInstance("这是通知中心");
                        break;
                    case R.id.rb_user:
                        fragment = TimelineFragment.newInstance("这是个人中心");
                        break;
                }

                transaction.replace(R.id.fragment_placeholder, fragment);
                transaction.commit();
            }
        });
    }
}
