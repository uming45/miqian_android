package com.miqian.mq.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.RegularEarn;
import com.miqian.mq.views.CircleBar;
import com.miqian.mq.views.DecoratorViewPager;

import java.util.List;

/**
 * Created by guolei_wang on 15/9/17.
 */
public class RegularListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final int ITEM_TYPE_HEADER = 0;
    private final int ITEM_TYPE_NORMAL = 1;

    private List<RegularEarn> items;
    private FragmentManager childFragmentManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    public RegularListAdapter(List<RegularEarn> items, FragmentManager childFragmentManager, SwipeRefreshLayout swipeRefreshLayout) {
        this.items = items;
        this.childFragmentManager = childFragmentManager;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == ITEM_TYPE_HEADER) {
            return new HeaderViewHolder(inflater.inflate(R.layout.fragment_regular_header, parent, false), childFragmentManager, swipeRefreshLayout);
        }else {
            return new ViewHolder(inflater.inflate(R.layout.regular_earn_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RegularEarn regularEarn = items.get(position);

        if(holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder)holder;
            viewHolder.tv_title.setText(regularEarn.getBdNm());
            viewHolder.tv_sub_title.setText(regularEarn.getBdDesp());
            viewHolder.tv_annurate_interest_rate.setText(regularEarn.getYrt());
            if(position > 0) {
                viewHolder.layout_regular_earn_head.setVisibility(View.GONE);
            }else {
                viewHolder.layout_regular_earn_head.setVisibility(View.VISIBLE);
            }
        }
    }

    public void addAll(List<RegularEarn> earnList){
        items.addAll(earnList);
        notifyDataSetChanged();
    }

    public void clear(){
        items.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return items == null? 0 : items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE_NORMAL;
//        return position == 0? ITEM_TYPE_HEADER : ITEM_TYPE_NORMAL;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private View layout_regular_earn_head;
        public TextView tv_lable_name;
        public TextView tv_invest_security;
        public TextView tv_title;
        public TextView tv_sub_title;
        public TextView tv_annurate_interest_rate;
        public TextView tv_duration;
        public TextView tv_progress;
        public TextView tv_sale_number;
        public CircleBar circlebar;
        public ViewHolder(View itemView) {
            super(itemView);

            layout_regular_earn_head = itemView.findViewById(R.id.layout_regular_earn_head);
            tv_lable_name = (TextView)itemView.findViewById(R.id.tv_lable_name);
            tv_invest_security = (TextView)itemView.findViewById(R.id.tv_invest_security);
            tv_title = (TextView)itemView.findViewById(R.id.tv_title);
            tv_sub_title = (TextView)itemView.findViewById(R.id.tv_sub_title);
            tv_annurate_interest_rate = (TextView)itemView.findViewById(R.id.tv_annurate_interest_rate);
            tv_duration = (TextView)itemView.findViewById(R.id.tv_duration);
            tv_progress = (TextView)itemView.findViewById(R.id.tv_progress);
            tv_sale_number = (TextView)itemView.findViewById(R.id.tv_sale_number);
            circlebar = (CircleBar)itemView.findViewById(R.id.circlebar);

            tv_lable_name.setText(R.string.regular_earn);
            tv_invest_security.setText(R.string.principal_interest_security);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        private DecoratorViewPager view_pager;
        public HeaderViewHolder(View itemView, FragmentManager fm, final SwipeRefreshLayout swipeRefreshLayout) {
            super(itemView);

            view_pager = (DecoratorViewPager)itemView.findViewById(R.id.view_pager);
            Log.d("HeaderViewHolder", "FragmentManager fm = " + fm.hashCode());
            view_pager.setAdapter(new RegularViewPagerAdapter(fm));
            // to cache all page, or we will see the right item delayed
            view_pager.setOffscreenPageLimit(3);
            view_pager.setPageMargin(itemView.getResources().getDimensionPixelSize(R.dimen.view_pager_margin));
            view_pager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_MOVE:
                            swipeRefreshLayout.setEnabled(false);
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            swipeRefreshLayout.setEnabled(true);
                            break;
                    }
                    return false;
                }
            });
//            view_pager.setOnPageChangeListener(new MyOnPageChangeListener());
        }


    }

}
