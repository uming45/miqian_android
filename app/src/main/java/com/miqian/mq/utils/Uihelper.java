package com.miqian.mq.utils;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.widget.Toast;

import com.miqian.mq.MyApplication;
import com.miqian.mq.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2015/9/4.
 */
public class Uihelper {


    private static Toast mToast;
    public static final String RFC3339 = "yyyy-MM-dd HH:mm:ss";

    private static void initToast() {
        if (mToast == null) {
            mToast = Toast.makeText(MyApplication.getInstance(), "", Toast.LENGTH_SHORT);
        }
    }

    public static void showToast(Context context, final String content) {
        initToast();
        if (!TextUtils.isEmpty(content)) {
            mToast.setText(content);
            mToast.show();
        }
    }

    public static void showToast(Context context, int id) {
        initToast();
        mToast.setText(id);
        mToast.show();
    }

    /**
     * dip2px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dipValue * scale + 0.5 * (dipValue >= 0 ? 1 : -1));
    }

    /**
     * px2dip
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将时间转换为中文/无T
     *
     * @param time
     * @return
     */
    public static String dateToChineseStrings(Context context, String time) {
        Resources resources = context.getResources();
//
//        Date datetime = parseDates(timestampToDateStr(time));
//        if (datetime == null)
//            return "未知时间";
//
//        Date today = new Date();

        long serverTime = Long.parseLong(time);
        long currentTime = System.currentTimeMillis();
//        long seconds = (today.getTime() - datetime.getTime()) / 1000;
        long seconds = (currentTime - serverTime) / 1000;

        long year = seconds / (24 * 60 * 60 * 30 * 12);// 相差年数

        if (year > 0) {
            return year + resources.getString(R.string.time_year_ago);
        }
        long month = seconds / (24 * 60 * 60 * 30);// 相差月数
        if (month > 0) {
            return month + resources.getString(R.string.time_month_ago);
        }
        long date = seconds / (24 * 60 * 60); // 相差的天数
        if (date > 0) {
            return date + resources.getString(R.string.time_day_ago);
        }
        long hour = seconds / (60 * 60);// 相差的小时数
        if (hour > 0) {
            return hour + resources.getString(R.string.time_hour_ago);
        }
        long minute = seconds / (60);// 相差的分钟数
        if (minute > 0) {
            return minute + resources.getString(R.string.time_minute_ago);
        }
        long second = seconds;// 相差的秒数
        if (second > 0) {
            return resources.getString(R.string.time_one_minute_inner);
        }
        return "";
    }

    public static Date parseDates(String str) {
        // return parseDate(str,M_TIME_FORMAT);
        return parseDate(str, RFC3339);
    }

    /**
     * 将时间戳转换成yyyy-MM-dd HH:mm:ss
     *
     * @param timestamp
     * @return
     * @author yangsq
     * @date 2014-7-22
     */
    public static String timestampToDateStr(Double timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String date = sdf.format(new Date((long) (timestamp * 1000L)));
        return date;
    }

    public static String timestampToDateStr_other(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String date = sdf.format(new Date(timestamp));
        return date;
    }

    /**
     * 将时间戳换算成 MM月dd日 HH:mm分 (定期待开标项目开标时间)
     *
     * @param time
     * @return
     */
    public static String timeToDateRegular(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return String.format(Locale.getDefault(), "%02d", month) + "月" +
                String.format(Locale.getDefault(), "%02d", day) + "日" +
                " " +
                String.format(Locale.getDefault(), "%02d", hour) + ":" +
                String.format(Locale.getDefault(), "%02d", minute) + "发售";
    }

    /**
     * 将时间戳转换成yyyyMMdd
     *
     * @param timestamp
     * @return
     * @author Jackie
     * @date 2015-9-29
     */
    public static String timestampToString(String timestamp) {
        if (!TextUtils.isEmpty(timestamp)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
            Date date = new Date(Long.parseLong(timestamp));
            return sdf.format(date);
        }
        return "";
    }

    /**
     * 红包显示时间格式
     *
     * @param timestamp
     * @return
     * @author Jackie
     * @date 2015-9-29
     */
    public static String redPaperTime(String timestamp) {
        if (TextUtils.isEmpty(timestamp)) {
            return "";
        } else if ("253402185600000".equals(timestamp)) {
            return "无期限";
        } else {
            return "有效期至" + Uihelper.timestampToString(timestamp);
        }
    }

    /**
     * 将时间戳转换成yyyyMMdd
     *
     * @param timestamp
     * @return
     * @author Jackie
     * @date 2015-9-29
     */
    public static String timeToString(String timestamp) {
        if (!TextUtils.isEmpty(timestamp)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = new Date(Long.parseLong(timestamp));
            return sdf.format(date);
        }
        return "";
    }

    public static Date parseDate(String str, String format) {
        Date addTime = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
            if (!TextUtils.isEmpty(str)) {
                addTime = dateFormat.parse(str);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return addTime;
    }

    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > 400) {
            lastClickTime = currentTime;
            return false;
        }
        return true;
    }

    public static void setLastClickTime() {
        lastClickTime = 0;
    }
}
