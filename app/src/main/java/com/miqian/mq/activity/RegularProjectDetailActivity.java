package com.miqian.mq.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miqian.mq.R;
import com.miqian.mq.entity.RegularBase;
import com.miqian.mq.entity.RegularDetailResult;
import com.miqian.mq.entity.RegularEarnDetail;
import com.miqian.mq.entity.RegularProjectFeature;
import com.miqian.mq.entity.RegularProjectInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Constants;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.WFYTitle;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author wangduo
 * @description: 定期项目详情
 * @email: cswangduo@163.com
 * @date: 16/10/21
 */
public class RegularProjectDetailActivity extends RegularDetailActivity {

    public static void startActivity(Context context, String subjectId) {
        Intent intent = new Intent(context, RegularProjectDetailActivity.class);
        intent.putExtra(Constants.SUBJECTID, subjectId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        prodId = RegularBase.REGULAR_03;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("定期项目");
    }

    @Override
    public String getPageName() {
        return "定期赚详情";
    }

    // 获取数据:定期项目
    @Override
    public void obtainData() {
        if (inProcess) {
            return;
        }
        synchronized (mLock) {
            inProcess = true;
        }
        begin();
        swipeRefresh.setRefreshing(true);
        HttpRequest.getRegularDetail(mContext, subjectId, prodId, new ICallback<RegularDetailResult>() {

            @Override
            public void onSucceed(RegularDetailResult result) {
                synchronized (mLock) {
                    inProcess = false;
                }
                swipeRefresh.setRefreshing(false);
                end();
                if (result == null || result.getData() == null
                        || result.getData().getSubjectData() == null) {
                    return;
                }
                showContentView();
                mInfo = result.getData().getSubjectData();
                updateUI();
            }

            @Override
            public void onFail(String error) {
                synchronized (mLock) {
                    inProcess = false;
                }
                swipeRefresh.setRefreshing(false);
                end();
                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                showErrorView();
            }
        });
    }

    protected void updateUI() {
        updateProjectInfo();
        updateFestivalInfo(mInfo.getFestival88(), mInfo.getFestival88_url());
        updateMoreInfo();
        updateProjectFeature();
        updateProjectStatus();
    }

    /**
     * 标的更多信息
     */
    private void updateMoreInfo() {
        ArrayList<RegularEarnDetail> mList = mInfo.getSubjectBar();
        if (mList == null || mList.size() <= 0) {
            return;
        }

        if (null == viewDetail) {
            viewstub_detail.setLayoutResource(R.layout.regular_project_detail);
            viewDetail = viewstub_detail.inflate();
        }
        LinearLayout content = (LinearLayout) viewDetail.findViewById(R.id.llyt_content);
        TextView tv_seemore = (TextView) viewDetail.findViewById(R.id.tv_seemore);
        tv_seemore.setOnClickListener(mOnclickListener);

        content.removeAllViews();

        LayoutInflater mInflater = LayoutInflater.from(getBaseContext());
        int count = mList.size() > 4 ? 4 : mList.size();
        for (int index = 0; index < count; index++) {
            RegularEarnDetail detail = mList.get(index);
            View mView = mInflater.inflate(R.layout.item_regular_project_detail, null);
            ((TextView) mView.findViewById(R.id.tv_left)).setText(detail.getTitle());
            ((TextView) mView.findViewById(R.id.tv_right)).setText(detail.getName());
            content.addView(mView);
        }
    }

}
