package com.miqian.mq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.RegularBase;
import com.miqian.mq.entity.RegularEarnDetail;
import com.miqian.mq.utils.Constants;
import com.miqian.mq.views.WFYTitle;

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
        prodId = RegularBase.REGULAR_PROJECT;
        super.onCreate(arg0);
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("定期项目");
    }

    @Override
    public String getPageName() {
        return "定期赚详情";
    }

    /**
     * 标的更多信息
     */
    @Override
    public void updateMoreInfo() {
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

        LayoutInflater mInflater = LayoutInflater.from(getApplicationContext());
        int count = mList.size() > 4 ? 4 : mList.size();
        for (int index = 0; index < count; index++) {
            RegularEarnDetail detail = mList.get(index);
            View mView = mInflater.inflate(R.layout.item_project_detail, content, false);
            ((TextView) mView.findViewById(R.id.tv_left)).setText(detail.getTitle());
            mView.findViewById(R.id.tv_right).setVisibility(View.VISIBLE);
            ((TextView) mView.findViewById(R.id.tv_right)).setText(detail.getName());
            content.addView(mView);
        }
    }

}