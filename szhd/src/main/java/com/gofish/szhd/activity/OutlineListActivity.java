package com.gofish.szhd.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.gofish.szhd.MyApplication;
import com.gofish.szhd.R;
import com.gofish.szhd.event.RequestResultEvent;
import com.gofish.szhd.model.OutlineItem;
import com.gofish.szhd.model.OutlineListDataModel;
import com.gofish.szhd.model.Outlines;
import com.gofish.utils.L;
import com.gofish.utils.T;

import de.greenrobot.event.EventBus;
import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import zhy_baseadapterhelper.CommonAdapter;
import zhy_baseadapterhelper.ViewHolder;


public class OutlineListActivity extends Activity {
    PtrFrameLayout mPtrFrameLayout;
    OutlineListDataModel mDataModel;
    ListView mListView;
    LoadMoreListViewContainer mLmContainer;
    CommonAdapter<OutlineItem> mLvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outline_list);

        mDataModel = new OutlineListDataModel(5);

        // pull to refresh
        /*
        mPtrFrameLayout = (PtrFrameLayout)findViewById(R.id.load_more_list_view_ptr_frame);
        mPtrFrameLayout.setLoadingMinTime(1000);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // here check list view, not content.
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mDataModel.queryFirstPage();
            }
        });
        */
        mLmContainer = (LoadMoreListViewContainer)
                findViewById(R.id.load_more_list_view_container);
        mLmContainer.useDefaultHeader();
        mLmContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                mDataModel.queryNextPage();
            }
        });

        mListView = (ListView)findViewById(R.id.lv);
        mLvAdapter= new CommonAdapter<OutlineItem>(this, null, R.layout.listitem){
            @Override
            public void convert(ViewHolder helper, OutlineItem item) {
                helper.setText(R.id.ItemTitle, item.title);
                helper.setText(R.id.ItemText, item.desc);
                helper.setText(R.id.ItemUpdateDt, item.update_dt);
                helper.setImageByUrl(R.id.network_image_view, item.img_url);
            }
        };
        mListView.setAdapter(mLvAdapter);

        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDataModel.queryFirstPage();
            }
        }, 100);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEventMainThread(RequestResultEvent event) {
        if (event.isSuccess) {
            mLmContainer.loadMoreFinish(mDataModel.getListPageInfo().isEmpty(), mDataModel.getListPageInfo().hasMore());
            mLvAdapter.setDataList(mDataModel.getListPageInfo().getDataList());
            mLvAdapter.notifyDataSetChanged();
        } else {
            T.showLong(event.errMessage);
        }
    }
}
