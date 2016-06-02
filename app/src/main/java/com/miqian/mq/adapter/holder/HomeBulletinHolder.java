package com.miqian.mq.adapter.holder;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.AnnounceResultActivity;
import com.miqian.mq.activity.user.NoticeActivity;
import com.miqian.mq.entity.HomePageInfo;
import com.miqian.mq.entity.MessageInfo;
import com.miqian.mq.utils.Pref;

/**
 * Created by guolei_wang on 16/5/24.
 * 首页公告
 */
public class HomeBulletinHolder extends HomeBaseViewHolder implements View.OnClickListener {
    private ImageView img_bulletin;
    private TextView tv_content;
    private Context mContext;
    private MessageInfo mPushData;

    public HomeBulletinHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        img_bulletin = (ImageView) itemView.findViewById(R.id.img_bulletin);
        tv_content = (TextView) itemView.findViewById(R.id.tv_content);
    }

    @Override
    public void bindView(HomePageInfo mData) {
        mPushData = mData.getBsPushData();
        if (mData.getBsPushData() != null && !TextUtils.isEmpty(mData.getBsPushData().getTitle())) {
            tv_content.setText(mData.getBsPushData().getTitle());

            //本地公告时间戳
            long bulletinLocalTime = Pref.getLong(Pref.DATA_BULLETIN_TIME, mContext, 0);

            if (mData.getBsPushData().getSendTime() > bulletinLocalTime) {
                Pref.saveLong(Pref.DATA_BULLETIN_TIME, mData.getBsPushData().getSendTime(), mContext);
                img_bulletin.setImageResource(R.drawable.icon_home_bulletin_new);
            } else {
                img_bulletin.setImageResource(R.drawable.icon_home_bulletin);
            }
            img_bulletin.setOnClickListener(this);
            tv_content.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_content:
//                if (mPushData != null) {
//                    Intent intent = new Intent(mContext, AnnounceResultActivity.class);
//                    intent.putExtra("id", mPushData.getId());
//                    intent.putExtra("isMessage", false);
//                    mContext.startActivity(intent);
//                }
                mContext.startActivity(new Intent(mContext, NoticeActivity.class));
                break;
            case R.id.img_bulletin:
//                mContext.startActivity(new Intent(mContext, NoticeActivity.class));
                break;
            default:
                break;
        }
    }
}