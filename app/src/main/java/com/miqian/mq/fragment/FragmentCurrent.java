package com.miqian.mq.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.activity.current.ActivityUserCurrent;
import com.miqian.mq.activity.current.CurrentInvestment;
import com.miqian.mq.entity.CurrentInfo;
import com.miqian.mq.entity.CurrentInfoResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.DialogPay;
import com.miqian.mq.views.MySwipeRefresh;
import com.miqian.mq.views.WaterWaveView;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;

public class FragmentCurrent extends Fragment implements View.OnClickListener {

    private View view;
    private WaterWaveView waterWaveView;
    private Button btInvestment;
    private TextView titleText;
    private TextView totalMoneyText;
    private TextView totalCountText;
    private TextView textDetail;
    private ImageButton btRight;
    private MySwipeRefresh swipeRefresh;

    private Activity mContext;
    private DialogPay dialogPay;

    private CurrentInfo currentInfo;

    private BigDecimal downLimit = BigDecimal.ONE;
    private BigDecimal upLimit = new BigDecimal(9999999999L);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        if (savedInstanceState == null || view == null) {
            view = inflater.inflate(R.layout.frame_current, null);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        findViewById(view);
        obtainData();
        return view;
    }

    private void findViewById(View view) {
        waterWaveView = (WaterWaveView) view.findViewById(R.id.wave_view);
        waterWaveView.setmWaterLevel(0.16F);
        waterWaveView.startWave();

        titleText = (TextView) view.findViewById(R.id.title);
        titleText.setText("活期");

        btRight = (ImageButton) view.findViewById(R.id.bt_right);
        btRight.setImageResource(R.drawable.btn_current_right);
        btRight.setOnClickListener(this);

        totalCountText = (TextView) view.findViewById(R.id.total_count);
        totalMoneyText = (TextView) view.findViewById(R.id.total_money);
        textDetail = (TextView) view.findViewById(R.id.text_detail);
        textDetail.setOnClickListener(this);

        btInvestment = (Button) view.findViewById(R.id.bt_investment);
        btInvestment.setOnClickListener(this);

        dialogPay = new DialogPay(mContext) {
            @Override
            public void positionBtnClick(String moneyString) {
                MobclickAgent.onEvent(mContext, "1057");
                if (!TextUtils.isEmpty(moneyString)) {
                    BigDecimal money = new BigDecimal(moneyString);
                    if (money.compareTo(downLimit) < 0) {
                        this.setTitle("提示：请输入大于等于" + downLimit + "元");
                        this.setTitleColor(getResources().getColor(R.color.mq_r1));
                    } else if (money.compareTo(upLimit) > 0) {
                        this.setTitle("提示：请输入小于等于" + upLimit + "元");
                        this.setTitleColor(getResources().getColor(R.color.mq_r1));
                    } else {
                        UserUtil.currenPay(mContext, moneyString, CurrentInvestment.PRODID_CURRENT, CurrentInvestment.SUBJECTID_CURRENT, "");
                        this.setEditMoney("");
                        this.setTitle("认购金额");
                        this.setTitleColor(getResources().getColor(R.color.mq_b1));
                        this.dismiss();
                    }
                } else {
                    this.setTitle("提示：请输入金额");
                    this.setTitleColor(getResources().getColor(R.color.mq_r1));
                }
            }

            @Override
            public void negativeBtnClick() {
                MobclickAgent.onEvent(mContext, "1056");
                this.setEditMoney("");
                this.setTitle("认购金额");
                this.setTitleColor(getResources().getColor(R.color.mq_b1));
            }
        };
        swipeRefresh = (MySwipeRefresh) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnPullRefreshListener(new MySwipeRefresh.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                obtainData();
            }
        });
    }

    private void refreshView() {
        if (currentInfo != null) {
            downLimit = new BigDecimal(currentInfo.getCurrentBuyDownLimit());
            upLimit = new BigDecimal(currentInfo.getCurrentBuyUpLimit());
            totalCountText.setText(currentInfo.getBuyItemCount());
            totalMoneyText.setText(currentInfo.getBuyTotalSum());
            if (currentInfo.getCurrentSwitch().equals("0")) {
                btInvestment.setText("已售罄");
                btInvestment.setEnabled(false);
            } else {
                btInvestment.setText("马上认购");
                btInvestment.setEnabled(true);
            }
        }
    }

    public void obtainData() {
        HttpRequest.getCurrentHome(mContext, new ICallback<CurrentInfoResult>() {
            @Override
            public void onSucceed(CurrentInfoResult result) {
                swipeRefresh.setRefreshing(false);
                currentInfo = result.getData();
                refreshView();
            }

            @Override
            public void onFail(String error) {
                swipeRefresh.setRefreshing(false);
                Uihelper.showToast(mContext, error);
            }
        });
    }


    @Override
    public void onDestroy() {
        waterWaveView.stopWave();
        waterWaveView = null;
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_investment:
                MobclickAgent.onEvent(mContext,"1007");
                UserUtil.loginPay(mContext, dialogPay);
                break;
            case R.id.bt_right:
                MobclickAgent.onEvent(mContext, "1005");
                UserUtil.isLogin(mContext, ActivityUserCurrent.class);
                break;
            case R.id.text_detail:
                MobclickAgent.onEvent(mContext, "1006");
                if (currentInfo != null) {
                    WebActivity.startActivity(mContext, Urls.web_current);
                }
                break;
            default:
                break;
        }
    }
}
