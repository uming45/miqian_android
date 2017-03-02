package com.miqian.mq.activity.current;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.adapter.SubscriptionRecordsAdapter;
import com.miqian.mq.entity.SubscriptionRecords;
import com.miqian.mq.entity.SubscriptionRecordsResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

import java.util.ArrayList;

/**
 * Created by wgl on 2016/10/25.
 */
public class SubscriptionRecordsActivity extends BaseActivity {
    private static final String PROJECT_CODE = "projectCode"; // 项目编号
    private RecyclerView recyclerView;
    private ArrayList<SubscriptionRecords.Products> dataList;
    private String projectCode = "";    //项目编号

    private SubscriptionRecordsAdapter subscriptionRecordsAdapter;

    @Override
    public void obtainData() {
        begin();
        HttpRequest.getUserCurrentProduct(mActivity, new ICallback<SubscriptionRecordsResult>() {
            @Override
            public void onSucceed(SubscriptionRecordsResult result) {
                end();
                if (result.getData() != null) {
                    dataList = result.getData().getProductList();
                    if (dataList != null && dataList.size() > 0) {
                        showContentView();
                        refreshView();
                    } else {
                        showEmptyView();
                    }
                }
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(mActivity, error);
                showErrorView();
            }
        }, projectCode);

    }

    @Override
    protected void showEmptyView() {
        super.showEmptyView();
        getTvTips().setText("项目已还款");
    }

    @Override
    public void onCreate(Bundle arg0) {
        projectCode = getIntent().getStringExtra(PROJECT_CODE);
        super.onCreate(arg0);
    }

    public static void startActivity(Context context, String projectCode) {
        Intent intent = new Intent(context, SubscriptionRecordsActivity.class);
        intent.putExtra(PROJECT_CODE, projectCode);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    private void refreshView() {
        subscriptionRecordsAdapter = new SubscriptionRecordsAdapter(this,dataList);
        recyclerView.setAdapter(subscriptionRecordsAdapter);
    }


    @Override
    public void initView() {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).colorResId(R.color.mq_b4).size(1).marginResId(R.dimen.margin_left_right).build());

        recyclerView.setHasFixedSize(true);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_subscription_records;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {

        mTitle.setTitleText("认购记录");
    }

    @Override
    protected String getPageName() {
        return "我的秒钱宝认购记录";
    }
}