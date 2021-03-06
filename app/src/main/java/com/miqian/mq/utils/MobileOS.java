package com.miqian.mq.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

/**
 * @author Jackie 主要是系统的常用函数
 **/
public class MobileOS {

    /**
     * md5加密
     *
     * @param s
     * @return
     */
    public static String getMd5(String s) {
        byte[] bytes = s.getBytes();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(bytes);
            return toHexString(algorithm.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取当前版本号
     *
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
        String version = "1.0";
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;

    }

    /**
     * 获取客户端版本号
     *
     * @param context
     * @return
     */
    public static String getClientVersion(Context context) {
        String verName = "unknown";
        try {
            verName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
        }
        return verName;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static int getVersionCode(Context context) {
        int version = -1;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    private static String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * 毫秒时间转换成小时分钟数
     *
     * @param milliseconds
     * @return
     */
    public static String toTimeString(int milliseconds) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H:mm:ss", Locale.UK);
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.format(milliseconds);
    }

    /***
     * 获取 IMEI, CellNumber, Sim serial number .etc.
     * </pre>
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = "";
        try {
            imei = telephonyManager.getDeviceId();
        } catch (Exception e) {
        }
        return !TextUtils.isEmpty(imei) ? imei : getLocalMacAddress(context);
    }

    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String address = info.getMacAddress();
        return !TextUtils.isEmpty(address) ? address : getAndroidId(context);
    }

    /**
     * 获取AndroidId
     */
    public static String getAndroidId(Context context) {
        String androidId = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (TextUtils.isEmpty(androidId)) {
            androidId = Pref.getString(Pref.DEVICE_ID, context, "");
            if (TextUtils.isEmpty(androidId)) {
                androidId = getRandomString();
                Pref.saveString(Pref.DEVICE_ID, androidId, context);
            }
        }
        return androidId;
    }

    /**
     * 获取不到设备IMEI IMSI等
     * 随机生成一个串作为DeviceId
     * Visitor_0000 + 随机数
     */
    public static String getRandomString() {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        sb.append(Pref.VISITOR);
        for (int i = 0; i < 10; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 获取运营商名字
     */
    public static String getOperatorName(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = telephonyManager.getSimOperator();
        String operatorName = "";
        if (operator != null) {
            if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {
                operatorName = "中国移动";
            } else if (operator.equals("46001") || operator.equals("46006")) {
                operatorName = "中国联通";
            } else if (operator.equals("46003") || operator.equals("46005")) {
                operatorName = "中国电信";
            } else if (operator.equals("46020")) {
                operatorName = "中国铁通";
            }
        }
        return operatorName;
    }

    /**
     * 获取手机型号
     */
    public static String getDeviceModel() {
        return TextUtils.isEmpty(android.os.Build.MODEL) ? "unknown" : android.os.Build.MODEL;
    }

    /**
     * 获取系统名称
     */
    public static String getVersionName(Context context) {
        return android.os.Build.VERSION.INCREMENTAL;
    }

    /**
     * 获取系统版本号
     */
    public static String getOsVersion() {
        String osVersion = android.os.Build.VERSION.RELEASE;
        return !TextUtils.isEmpty(osVersion) ? osVersion : "unknown";
    }

    /**
     * 获取网络类型，简单的区分wifi，移动数据网，无网络络三种
     *
     * @return
     */
    public static int getNetworkType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null) {
            if (!info.isAvailable()) {
                return -1;
            } else {
                if (info.getType() == ConnectivityManager.TYPE_WIFI && info.isConnected()) {
                    return ConnectivityManager.TYPE_WIFI;
                } else if (info.getType() == ConnectivityManager.TYPE_MOBILE && info.isConnected()) {
                    return ConnectivityManager.TYPE_MOBILE;
                }
            }
        }
        return -1;
    }

    /**
     * 判断网络状态
     */
    public static NetWorkEnum getNetWorkState(Context context) {
        int type = getNetworkType(context);
        if (type == ConnectivityManager.TYPE_MOBILE) {
            return NetWorkEnum.Mobile;
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            return NetWorkEnum.Wifi;
        } else {
            return NetWorkEnum.NoConnection;
        }
    }

    public enum NetWorkEnum {
        NoConnection, Mobile, Wifi
    }

    /**
     * 获取网络类型名称
     *
     * @return
     */
    public static String getNetworkString(Context context) {
        String strNetworkType = "unknown";
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();
                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000") || _strSubTypeName.equalsIgnoreCase("TDS_HSDPA")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = "MOBILE";
                        }
                        break;
                }
            }
        }
        return strNetworkType;
    }

    /**
     * 获取渠道名
     *
     * @param context 此处习惯性的设置为activity，实际上context就可以
     * @return 如果没有获取成功，那么返回值为空
     */
    public static String getChannelName(Context context) {
        if (context == null) {
            return null;
        }
        String channelName = "unknown";
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = applicationInfo.metaData.get("UMENG_CHANNEL") + "";
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            channelName = "unknown";
            e.printStackTrace();
        }
        return channelName;
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        String telRegex = "[1][3456789]\\d{9}";// "[1]"代表第1位为数字1,第二位为3到9，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    /**
     * 判断sd卡是否存在
     **/
    public static boolean isSDCardMounted() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     * 判断4.4以上（19）
     **/
    public static boolean isKitOrNewer() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * 判断4.1以上（16）
     **/
    public static boolean isJellyOrNewer() {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN;
    }

//    /**
//     * 判断指定包名的进程是否运行
//     *
//     * @param context
//     * @param packageName 指定包名
//     * @return 是否运行
//     */
//    public static boolean isRunning(Context context, String packageName) {
//        ActivityManager am = (ActivityManager) context.getSystemService("miqian");
//        List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
//        for (RunningAppProcessInfo rapi : infos) {
//            if (rapi.processName.equals(packageName)) {
//                return true;
//            }
//        }
//        return false;
//    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

}
