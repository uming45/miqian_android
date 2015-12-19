package com.miqian.mq.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.adapter.AdapterInvalidMyTicket;
import com.miqian.mq.entity.Page;
import com.miqian.mq.entity.Promote;
import com.miqian.mq.entity.RedPaperData;
import com.miqian.mq.entity.Redpaper;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/8.
 */
public class MyTicketInvalidActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    public List<Promote> promList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RadioGroup radioGroup;
    private LinearLayout frameNone;

    private AdapterInvalidMyTicket adapterMyTicket;
    private int pageNo = 1;
    private String pageSize = "20";
    private Page page;
    private boolean isLoading = false;
    private String type;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MyTicketInvalidActivity.class);
        context.startActivity(intent);
    }
    @Override
    public void onCreate(Bundle arg0) {
        type = "GQ";
//        String jpushToken = getIntent().getStringExtra("token");
//        if (!TextUtils.isEmpty(jpushToken)) {
//            //通知进来的情况下，不是当前用户则退出此界面
//            String token = UserUtil.getToken(this);
//            if (UserUtil.hasLogin(this) && !token.equals(jpushToken)) {
//                isStop = true;
//                finish();
//            }
//        }
        super.onCreate(arg0);
    }

    @Override
    public void obtainData() {
//        if (isStop) {
//            return;
//        }
        pageNo = 1;
        mWaitingDialog.show();
        HttpRequest.getCustPromotion(mActivity, new ICallback<RedPaperData>() {


            @Override
            public void onSucceed(RedPaperData result) {
                mWaitingDialog.dismiss();
                Redpaper redpaper = result.getData();
                promList = redpaper.getCustPromotion();
                page = redpaper.getPage();
                if (redpaper != null) {
                    if (promList != null && promList.size() > 0) {
                        showContentView();
                        recyclerView.setVisibility(View.VISIBLE);
                        frameNone.setVisibility(View.GONE);
                        refreshView();
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        frameNone.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFail(String error) {
                mWaitingDialog.dismiss();
                Uihelper.showToast(mActivity, error);
                showErrorView();
            }
        }, type, String.valueOf(pageNo), pageSize);
    }

    @Override
    public void initView() {
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(this);
        frameNone = (LinearLayout) findViewById(R.id.frame_none);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).colorResId(R.color.mq_b4).size(1).build());
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                if (lastVisibleItem >= totalItemCount - 3) {
                    loadMore();
                }
            }
        });
    }

    private void loadMore() {
        if (!isLoading) {
            if (promList.size() >= page.getCount()) {
                return;
            }
            isLoading = true;
            pageNo += 1;
            HttpRequest.getCustPromotion(mActivity, new ICallback<RedPaperData>() {


                @Override
                public void onSucceed(RedPaperData result) {
                    List<Promote> tempList = result.getData().getCustPromotion();
                    if (promList != null && tempList != null && tempList.size() > 0) {
                        promList.addAll(tempList);
                        adapterMyTicket.notifyItemInserted(promList.size());
                    }
                    isLoading = false;
                }

                @Override
                public void onFail(String error) {
                    isLoading = false;

                }
            }, type, String.valueOf(pageNo), pageSize);
        }
    }


    private void refreshView() {
        adapterMyTicket = new AdapterInvalidMyTicket(mActivity, promList);
        adapterMyTicket.setMaxItem(page.getCount());
        recyclerView.setAdapter(adapterMyTicket);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_myredpaper_invalid;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("红包/券");
        mTitle.setRightText("使用规则");
        mTitle.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mActivity, "1042");
                WebActivity.startActivity(mActivity, Urls.web_ticket);
            }
        });

    }

    @Override
    protected String getPageName() {
        return "红包/券";
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.bt_overdue) {
            type = "GQ";
        } else if (checkedId == R.id.bt_used) {
            type = "YW";
        } else if (checkedId == R.id.bt_gift) {
            type = "ZS";
        }
        obtainData();
    }
}
