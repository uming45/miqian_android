package com.miqian.mq.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.miqian.mq.R;
import com.miqian.mq.adapter.holder.HomeHeaderViewHolder;
import com.miqian.mq.adapter.holder.RegularEarnViewHoder;
import com.miqian.mq.entity.HomePageInfo;
import com.miqian.mq.entity.RegularEarn;

/**
 * Created by guolei_wang
 */
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_TYPE_HEADER = 0;
    private final int ITEM_TYPE_NEW = 1;
    private final int ITEM_TYPE_JINGXUAN = 2;
    HomePageInfo info;
    Activity activity;
    HomeHeaderViewHolder homeHeaderViewHolder;

    private int newCustomerSize = 0;
    private int subjectInfoSize = 0;

    public HomeAdapter(Activity activity, HomePageInfo info) {
        this.activity = activity;
        this.info = info;
        newCustomerSize = info.getNewCustomer() == null? 0 : info.getNewCustomer().size();
        subjectInfoSize = info.getSubjectInfo() == null? 0 : info.getSubjectInfo().size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_HEADER;
        } else if(position <= info.getNewCustomer().size()){
            return ITEM_TYPE_NEW;
        }else {
            return ITEM_TYPE_JINGXUAN;
        }
    }

    @Override
    public int getItemCount() {
        return 1 + newCustomerSize + subjectInfoSize;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM_TYPE_HEADER:
                ViewGroup vImage = (ViewGroup) mInflater.inflate(R.layout.fragment_home_recyclerview_item_advertisement,parent, false);
                homeHeaderViewHolder = new HomeHeaderViewHolder(vImage, info.getAdImgs());
                return homeHeaderViewHolder;
            default:
                return new RegularEarnViewHoder(mInflater.inflate(R.layout.regular_earn_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof RegularEarnViewHoder) {
            RegularEarn regularEarn;
            RegularEarnViewHoder holder = (RegularEarnViewHoder) viewHolder;
            if(position > newCustomerSize) {
                regularEarn = info.getSubjectInfo().get(position - newCustomerSize - 1);
                holder.setLableName("精选项目");
            }else {
                regularEarn = info.getNewCustomer().get(position - 1);
                holder.setLableName("新手专享");
                holder.showDiverView(false);
            }
            holder.bindView(activity.getBaseContext(), regularEarn, (position == 1 || position == newCustomerSize + 1) ? true : false);
        }
    }


    public void notifyDataSetChanged(HomePageInfo info) {
        this.info = info;
        notifyDataSetChanged();
    }

    /**
     * 设置图片自动滚动
     */
    public void setAutoScroll() {
        if(homeHeaderViewHolder != null) {
            homeHeaderViewHolder.handler.sendEmptyMessage(HomeHeaderViewHolder.MSG_ACTION_SLIDE_PAGE);
        }
    }
}