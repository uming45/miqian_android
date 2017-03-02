package com.miqian.mq.activity;

import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.LoginResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Constants;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.Dialog_Login;
import com.miqian.mq.views.MQMarqueeTextView;
import com.miqian.mq.views.MySwipeRefresh;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;

/**
 * @author wangduo
 * @description: 定期赚 定期计划 定期转让详情页
 * @email: cswangduo@163.com
 * @date: 16/6/2
 */
public abstract class ProjectDetailActivity extends BaseActivity {

    MySwipeRefresh swipeRefresh;

    // 最大可认购金额 如果为12个9 代表 无限购
    final BigDecimal MAXBUYAMT = new BigDecimal("999999999");

    /* 标的基本信息 */
    TextView tv_begin_countdown; // 开标倒计时
    TextView tv_name; // 标的名称
    ImageView iv_tag;
    TextView tv_description; // 标的描述
    TextView tv_profit_rate; // 标的年利率
    TextView tv_profit_rate_unit; // 标的年利率 单位％
    TextView tv_time_limit; // 标的期限
    TextView tv_time_limit_unit; // 标的期限 单位
    TextView tv_remain_amount; // 标的可认购金额
    TextView tv_people_amount; // 已认购人数
    TextView tv_info_right; // 默认文字(已认购人数)或原年化收益:
    /* 标的基本信息 */

    /* 标的活动信息 */
    private MQMarqueeTextView tv_festival; // 88理财节
    /* 标的活动信息 */

    /* 标的更多信息 */
    ViewStub viewstub_detail;
    View viewDetail;
    /* 标的更多信息 */

    /* 标的特色相关 */
    LinearLayout llyt_project_feature;
    ImageView iv1;
    ImageView iv2;
    ImageView iv3;
    TextView tv1;
    TextView tv2;
    TextView tv3;
    /* 标的特色相关 */

    /* 底部输入框相关 */
    EditText et_input;
    private Button btn_buy;
    private View view_close_keyboard;
    private View view_to_login; // 未登录情况下底部显示透明view 点击触发引导登录
    private RelativeLayout rlyt_input; // 底部状态:立即认购输入框
    private RelativeLayout rlyt_dialog;
    TextView tv_dialog_min_amount; // 起投金额
    TextView tv_dialog_max_amount_tip; // 最大可认购金额/实际支付金额 文案
    TextView tv_dialog_max_amount; // 最大可认购金额/实际支付金额
    Button btn_state; // 底部状态:标的状态:已满额 待开标
    private InputMethodManager imm;
    /* 底部输入框相关 */

    String subjectId; // 标的ID
    int prodId; // 产品类型:定期计划 定期赚
    String total_profit_rate; // 标的总年化利率:原始+赠送利率

    String input; // 用户输入金额

    private int screenHeight; // 屏幕高度

    boolean inProcess = false;
    final Object mLock = new Object();

    @Override
    public void onCreate(Bundle arg0) {
        subjectId = getIntent().getStringExtra(Constants.SUBJECTID);
        super.onCreate(arg0);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        showDefaultView();
        screenHeight = MobileOS.getScreenHeight(this);
        // 关于Android收起输入法时会出现屏幕部分黑屏解决
        // http://blog.csdn.net/lytxyc/article/details/44622367
        mContentView.getRootView().setBackgroundColor(getResources().getColor(R.color.white));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mContentView.addOnLayoutChangeListener(onLayoutChangeListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mContentView.removeOnLayoutChangeListener(onLayoutChangeListener);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_regular_detail;
    }

    @Override
    public void initView() {
        swipeRefresh = (MySwipeRefresh) findViewById(R.id.swipe_refresh);

        tv_begin_countdown = (TextView) findViewById(R.id.tv_begin_countdown);
        tv_name = (TextView) findViewById(R.id.tv_name);
        iv_tag = (ImageView) findViewById(R.id.iv_tag);
        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_profit_rate = (TextView) findViewById(R.id.tv_profit_rate);
        tv_profit_rate_unit = (TextView) findViewById(R.id.tv_profit_rate_unit);
        tv_time_limit = (TextView) findViewById(R.id.tv_time_limit);
        tv_time_limit_unit = (TextView) findViewById(R.id.tv_time_limit_unit);
        tv_remain_amount = (TextView) findViewById(R.id.tv_remain_amount);
        tv_people_amount = (TextView) findViewById(R.id.tv_people_amount);
        tv_info_right = (TextView) findViewById(R.id.tv_info_right);
        tv_festival = (MQMarqueeTextView) findViewById(R.id.tv_festival);
        viewstub_detail = (ViewStub) findViewById(R.id.viewstub_detail);

        llyt_project_feature = (LinearLayout) findViewById(R.id.llyt_project_feature);
        iv1 = (ImageView) findViewById(R.id.iv_1);
        iv2 = (ImageView) findViewById(R.id.iv_2);
        iv3 = (ImageView) findViewById(R.id.iv_3);
        tv1 = (TextView) findViewById(R.id.tv_1);
        tv2 = (TextView) findViewById(R.id.tv_2);
        tv3 = (TextView) findViewById(R.id.tv_3);

        rlyt_dialog = (RelativeLayout) findViewById(R.id.rlyt_dialog);
        view_to_login = findViewById(R.id.view_to_login);
        et_input = (EditText) findViewById(R.id.et_input);
        rlyt_input = (RelativeLayout) findViewById(R.id.rlyt_input);
        btn_buy = (Button) findViewById(R.id.btn_buy);
        view_close_keyboard = findViewById(R.id.view_close_keyboard);
        btn_state = (Button) findViewById(R.id.btn_state);
        btn_buy = (Button) findViewById(R.id.btn_buy);
        tv_dialog_min_amount = (TextView) findViewById(R.id.tv_dialog_min_amount);
        tv_dialog_max_amount = (TextView) findViewById(R.id.tv_dialog_max_amount);
        tv_dialog_max_amount_tip = (TextView) findViewById(R.id.tv_dialog_max_amount_tip);

        view_close_keyboard.setVisibility(View.GONE);

        initListener();
    }

    private void initListener() {
        swipeRefresh.setOnPullRefreshListener(mOnPullRefreshListener);
        view_close_keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
            }
        });
    }

    protected abstract void jumpToNextPageIfInputValid();

    private View.OnLayoutChangeListener onLayoutChangeListener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            if (btn_state.getVisibility() == View.VISIBLE
                    || view_to_login.getVisibility() == View.VISIBLE) {
                // 输入框未显示
                return;
            }
            if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > screenHeight / 3)) {
                showKeyBoardView();
            } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > screenHeight / 3)) {
                hideKeyBoardView();
            }
        }
    };

    private MySwipeRefresh.OnPullRefreshListener mOnPullRefreshListener = new MySwipeRefresh.OnPullRefreshListener() {
        @Override
        public void onRefresh() {
            obtainData();
        }
    };

    private View.OnFocusChangeListener mOnFousChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                showKeyBoardView();
            }
        }
    };

    /**
     * 屏蔽触摸 键盘弹出后 其它区域的点击事件
     */
    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    };

    /**
     * 弹出输入法键盘后额外显示view
     */
    private void showKeyBoardView() {
        view_close_keyboard.setVisibility(View.VISIBLE);
        rlyt_dialog.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏输入法后键盘额外显示view
     */
    private void hideKeyBoardView() {
        view_close_keyboard.setVisibility(View.GONE);
        rlyt_dialog.setVisibility(View.GONE);
    }

    /**
     * 此方法为不得已而为之之法 不可轻易尝试 其中有何风险待测试
     */
    private void closeKeyboard() {
        if (null != imm) {
            begin();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        end();
                    }
                }
            }).start();
        }
    }

    /**
     * 标的活动信息
     *
     * @param festival88
     * @param festival88_url
     */
    void updateFestivalInfo(String festival88, final String festival88_url) {
        if (!TextUtils.isEmpty(festival88)) {
            tv_festival.setVisibility(View.VISIBLE);
            tv_festival.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            tv_festival.getPaint().setAntiAlias(true);
            tv_festival.setText(festival88);
            if (!TextUtils.isEmpty(festival88_url)) {
                tv_festival.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WebActivity.startActivity(mActivity, festival88_url);
                    }
                });
            }
        } else {
            tv_festival.setVisibility(View.GONE);
        }
    }

    /**
     * 用户未登录状态下 已开标 未满额状态下 在输入框上蒙一层view，点击view先去登录
     * 用户已登录状态下 则没有此view
     */
    void showViewToLogin() {
        view_to_login.setVisibility(View.VISIBLE);
        view_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserUtil.hasLogin(ProjectDetailActivity.this)) {
                    enableInputEditText();
                    hideViewToLogin();
                    hideStateButton();
                    return;
                }
                showLoginDialog();
            }
        });
    }

    /**
     * 已登录 已满额 待开标状态下隐藏view
     */
    void hideViewToLogin() {
        view_to_login.setVisibility(View.GONE);
        view_to_login.setOnClickListener(null);
    }

    /**
     * 显示底部输入框 并使功能生效
     */
    void enableInputEditText() {
        rlyt_input.setVisibility(View.VISIBLE);
        rlyt_dialog.setOnTouchListener(mOnTouchListener);
        rlyt_input.setOnTouchListener(mOnTouchListener);
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToNextPageIfInputValid();
            }
        });
        et_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKeyBoardView();
            }
        });
        et_input.setOnFocusChangeListener(mOnFousChangeListener);
    }

    /**
     * 显示底部输入框 但使功能生效
     */
    void disableInputEditText() {
        rlyt_input.setVisibility(View.VISIBLE);
        rlyt_dialog.setOnTouchListener(null);
        rlyt_input.setOnTouchListener(null);
        btn_buy.setOnClickListener(null);
        et_input.setOnClickListener(null);
        et_input.setOnFocusChangeListener(null);
    }

    /**
     * 隐藏底部输入框
     */
    void hideInputEditText() {
        rlyt_input.setVisibility(View.GONE);
    }

    /**
     * 显示底部状态按钮(如：待开标、已满额)
     */
    void showStateButton() {
        btn_state.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏底部状态按钮(如：待开标、已满额)
     */
    void hideStateButton() {
        btn_state.setVisibility(View.GONE);
    }

    private Dialog_Login dialog_login;

    /**
     * 显示登录对话框 引导用户登录
     */
    private void showLoginDialog() {
        if (null == dialog_login) {
            dialog_login = new Dialog_Login(ProjectDetailActivity.this) {
                @Override
                public void login(String telephone, String password) {
                    begin();
                    HttpRequest.login(getApplicationContext(), new ICallback<LoginResult>() {
                        @Override
                        public void onSucceed(LoginResult result) {
                            enableInputEditText();
                            hideViewToLogin();
                            hideStateButton();
                            if (Pref.getBoolean(Pref.GESTURESTATE, ProjectDetailActivity.this, true)) {
                                GestureLockSetActivity.startActivity(ProjectDetailActivity.this, null, false);
                            }
                            obtainData();
                            end();
                        }

                        @Override
                        public void onFail(String error) {
                            end();
                            Uihelper.showToast(ProjectDetailActivity.this, error);
                        }
                    }, telephone, password);
                }
            };
            dialog_login.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    closeKeyboard();
                }
            });
        }
        if (!dialog_login.isShowing()) {
            dialog_login.show();
        }
    }

    /**
     * 为文本增加单位(元)
     *
     * @param textView
     */
    void addUnit(TextView textView) {
        SpannableString spannableString = new SpannableString("元");
        spannableString.setSpan(new TextAppearanceSpan(this, R.style.f3_b4_V2), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.append(spannableString);
    }

    /**
     * 获取最大可认购金额
     *
     * @param amountA
     * @param amountB
     * @return
     */
    BigDecimal getUpLimit(BigDecimal amountA, BigDecimal amountB) {
        if (amountA == null && amountB == null) {
            return null;
        } else if (amountA == null) {
            return amountB;
        } else if (amountB == null) {
            return amountA;
        } else {
            return amountA.compareTo(amountB) < 0 ? amountA : amountB;
        }
    }

}