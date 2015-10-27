package com.miqian.mq.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.current.ActivityUserCurrent;
import com.miqian.mq.activity.user.UserRegularActivity;
import com.miqian.mq.adapter.RegularListAdapter;
import com.miqian.mq.entity.GetRegularInfo;
import com.miqian.mq.entity.GetRegularResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.MySwipeRefresh;

/**
 * Created by guolei_wang on 15/9/16.
 */
public class RegularFragment extends BasicFragment implements View.OnClickListener {

    RegularListAdapter mAdapter;
    private GetRegularInfo mData;
    private ImageButton btRight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_regular, container, false);
        findView(view);
        setView();
        if (mData == null) {
            getMainRegular();
        }
        return view;
    }

    private RecyclerView recyclerView;
    private MySwipeRefresh swipeRefresh;
    private TextView titleText;

    private void findView(View view) {
        btRight = (ImageButton) view.findViewById(R.id.bt_right);
        btRight.setImageResource(R.drawable.btn_regular_right);
        btRight.setOnClickListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        titleText = (TextView) view.findViewById(R.id.title);
        swipeRefresh = (MySwipeRefresh) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnPullRefreshListener(new MySwipeRefresh.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                getMainRegular();
            }
        });
    }

    private void setView() {
        titleText.setText("定期");
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            //用来标记是否正在向最后一个滑动，既是否向右滑动或向下滑动
//            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();

                    Log.d("onScrollStateChanged", "lastVisibleItem = " + lastVisibleItem + " ------------- totalItemCount = " + totalItemCount);

                    // 判断是否滚动到底部，并且是向右滚动
                    if (lastVisibleItem == (totalItemCount - 1)) {
//                        Toast.makeText(mActivity, "加载更多", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        if (mData != null) {
            mAdapter = new RegularListAdapter(mData, mApplicationContext, swipeRefresh);
            recyclerView.setAdapter(mAdapter);
        }
    }

    private boolean inProcess = false;
    private final Object mLock = new Object();

    private void getMainRegular() {
        if (inProcess) {
            return;
        }
        synchronized (mLock) {
            inProcess = true;
        }
        begin();
        swipeRefresh.setRefreshing(true);
        HttpRequest.getMainRegular(getActivity(), new ICallback<GetRegularResult>() {

            @Override
            public void onSucceed(GetRegularResult result) {
                synchronized (mLock) {
                    inProcess = false;
                }
                end();
                swipeRefresh.setRefreshing(false);
                if (result == null) return;
                mData = result.getData();
                if (mData == null) return;
                mAdapter = new RegularListAdapter(mData, mApplicationContext, swipeRefresh);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFail(String error) {
                synchronized (mLock) {
                    inProcess = false;
                }
                end();
                swipeRefresh.setRefreshing(false);
                Uihelper.showToast(getActivity(), error);
            }
        });
    }

    @Override
    protected String getPageName() {
        return "定期首页";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_right:
                UserUtil.isLogin(getActivity(), UserRegularActivity.class);
                break;
        }
    }
}