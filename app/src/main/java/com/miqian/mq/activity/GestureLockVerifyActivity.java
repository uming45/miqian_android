package com.miqian.mq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.encrypt.Encrypt;
import com.miqian.mq.utils.Constants;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.GestureLockView;

/**
 * 手势锁验证
 * Created by wangduo on 15/12/8.
 */
public class GestureLockVerifyActivity extends BaseFragmentActivity {

    private TextView tv_user;
    private TextView tv_tip;
    private TextView tv_forgetPsw;
    private GestureLockView lockView;
    private String gesturePsw; // 手势图案密码
    private int unlockCount; // 图案锁 剩余解锁验证次数

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_lock_verify);
        init();
    }

    @Override
    protected String getPageName() {
        return null;
    }

    private void init() {
        findView();
        fetchGestureLock();
        showUserPhoneNum();
        img_left.setVisibility(View.GONE);
        tv_forgetPsw.setOnClickListener(onClickListener);
    }

    private void showUserPhoneNum() {
        String telphone = Pref.getString(Pref.TELEPHONE, getBaseContext(), "");
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(telphone)) {
            for (int index = 0; index < telphone.length(); index++) {
                char temp = telphone.charAt(index);
                if (index > 2 && index < 7) {
                    sb.append("*");
                } else {
                    sb.append(temp);
                }
            }
        }
        tv_user.setText(sb.toString());
    }

    public static void main(String[] args) {
        String phoneNum = "18801023565";
        System.out.println(phoneNum.substring(3, 7));
    }

    private void findView() {
        tv_user = (TextView) findViewById(R.id.tv_user);
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        tv_forgetPsw = (TextView) findViewById(R.id.tv_forgetPsw);
        lockView = (GestureLockView) findViewById(R.id.lockView);
        lockView.setOnPatterChangeListener(onPatterChangeListener);
    }

    // 获取手势锁相关信息
    private void fetchGestureLock() {
        try {
            gesturePsw = Pref.getString(Pref.GESTUREPSW, getBaseContext(), null);
            unlockCount = Pref.getInt(Pref.UNLOCKCOUNT, getBaseContext(), 0);
            if (null != gesturePsw) {
                gesturePsw = Encrypt.decrypt(gesturePsw);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            logout();
        }
    };

    private void startShake() {
        TranslateAnimation animation = new TranslateAnimation(0, -5, 0, 0);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setDuration(50);
        animation.setRepeatCount(4);
        animation.setRepeatMode(Animation.REVERSE);
        tv_tip.startAnimation(animation);
    }

    private GestureLockView.OnPatterChangeListener onPatterChangeListener = new GestureLockView.OnPatterChangeListener() {
        @Override
        public void onPatterChanged(String password) {
            if (password.equals(gesturePsw)) {
                tv_tip.setText("验证成功");
                Pref.saveInt(Pref.UNLOCKCOUNT, Constants.MAXCOUNT, getBaseContext());
                if (null != desClass) {
                    startActivity(new Intent(getBaseContext(), desClass));
                }
                GestureLockVerifyActivity.this.finish();
            } else {
                lockView.showErrorState();
                unlockCount--;
                if (unlockCount <= 0) {
                    logout();
                    return;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("密码错误，还可以再输入").append(unlockCount).append("次");
                tv_tip.setText(sb.toString());
                tv_tip.setTextColor(getResources().getColor(R.color.mq_y));
                startShake();
            }
        }

        @Override
        public void onPatterStart() {
        }
    };

    // 验证失败 退出登录
    private void logout() {
        Pref.saveInt(Pref.UNLOCKCOUNT, 0, getBaseContext());
        UserUtil.clearUserInfo(getBaseContext());
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.VERIFYFAILED, true);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
    }

    private static Class<?> desClass; // 输入手势密码后跳转页面

    public static void startActivity(Context context, Class cl) {
        desClass = cl;
        Intent intent = new Intent(context, GestureLockVerifyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
