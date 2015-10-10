package com.miqian.mq.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.Redpaper;
import com.miqian.mq.utils.Uihelper;

import java.util.List;

/**
 * Created by Jackie on 2015/9/25.
 */
public class AdapterMyTicket extends RecyclerView.Adapter {

    private List<Redpaper.CustPromotion> promList;


    public AdapterMyTicket(List<Redpaper.CustPromotion> promList) {
        this.promList = promList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ticket, parent, false);
        return new ViewHolderTicket(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Redpaper.CustPromotion promote = promList.get(position);
        ((ViewHolderTicket) holder).textMoney.setText("￥" + promote.getCanUseAmt());
        ((ViewHolderTicket) holder).textType.setText("拾财券 【抵用比例"+ promote.getPrnUsePerc() + "%】");
        ((ViewHolderTicket) holder).limitType.setText(promote.getLimitMsg());
        ((ViewHolderTicket) holder).limitDate.setText("有效期至" + Uihelper.timestampToString(promote.getEndTimestamp()));
            ((ViewHolderTicket) holder).promoteChoosed.setVisibility(View.GONE);

        String state = promote.getSta();
        ((ViewHolderTicket) holder).imageState.setBackgroundResource(R.drawable.ticket_bg);
        if (!TextUtils.isEmpty(state)) {

            ((ViewHolderTicket) holder).imageState.setVisibility(View.VISIBLE);
            if ("YW".equals(state)) {
                ((ViewHolderTicket) holder).imageState.setBackgroundResource(R.drawable.hb_used);
                ((ViewHolderTicket) holder).textMoney.setBackgroundResource(R.drawable.ticket_bg_grey);
            } else if ("GQ".equals(state)) {
                ((ViewHolderTicket) holder).imageState.setBackgroundResource(R.drawable.hb_expired);
                ((ViewHolderTicket) holder).textMoney.setBackgroundResource(R.drawable.ticket_bg_grey);
            } else {
                ((ViewHolderTicket) holder).imageState.setVisibility(View.GONE);
            }
        }else {
            ((ViewHolderTicket) holder).imageState.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        if (promList != null) {
            return promList.size();
        }
        return 0;
    }

    class ViewHolderTicket extends RecyclerView.ViewHolder {

        public TextView textType;
        public TextView limitType;
        public TextView limitDate;
        public TextView textMoney;
        public ImageView promoteChoosed;
        public ImageView imageState;

        public ViewHolderTicket(View itemView) {
            super(itemView);
            textType = (TextView) itemView.findViewById(R.id.text_type);
            limitType = (TextView) itemView.findViewById(R.id.limit_type);
            limitDate = (TextView) itemView.findViewById(R.id.limit_date);
            textMoney = (TextView) itemView.findViewById(R.id.text_money);
            promoteChoosed = (ImageView) itemView.findViewById(R.id.promote_choosed);
            imageState = (ImageView) itemView.findViewById(R.id.image_state);
        }
    }

}
