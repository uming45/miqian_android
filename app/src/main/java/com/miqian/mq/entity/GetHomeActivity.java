package com.miqian.mq.entity;

/**
 * Created by guolei_wang on 15/12/17.
 * 首页运营活动返回数据
 */
public class GetHomeActivity {

    public static final String FLAG_UNSHOW = "0";   //常量 不弹窗
    public static final String FLAG_SHOW = "1";    //常量 弹窗
    public static final String ACTIVITY_TYPE_HOME = "0";    //活动类型常量 首页活动
    public static final String ACTIVITY_TYPE_PROMOTION = "2";    //活动类型常量  收到促销活动
    public static final String ACTIVITY_TYPE_PROMOTION_OVERDUE = "3";    //活动类型常量  促销过期活动

    private String activityId = "";                //活动 ID
    private String activityPlanId = "";            //活动计划 ID
    private String backgroundCase;            //文本描述
    private String jumpUrl;                   //活动跳转链接
    private String showFlag;                     //是否弹窗标示0:不弹窗 1：弹窗
    private String enterCase;                  //参加活动按钮文本
    private String titleCase;                  //对话框标题
    private String jumpNative;                  //跳转到原生的哪里 （新增字段）（jumpUrl和jumpNative只会一个有值）（e.g 1首页 2优惠券列表 3 定期首页）
    private String activityType;                  //活动类型 0为首页活动,2 收到促销活动，3促销过期活动（新增字段）(根据类型选择对应的样式)
    private long sysDate;                      //系统当前时间
    private long beginTime;                      //活动开始时间
    private long endTime;                      //活动结束时间

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityPlanId() {
        return activityPlanId;
    }

    public void setActivityPlanId(String activityPlanId) {
        this.activityPlanId = activityPlanId;
    }

    public String getBackgroundCase() {
        return backgroundCase;
    }

    public void setBackgroundCase(String backgroundCase) {
        this.backgroundCase = backgroundCase;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public String getShowFlag() {
        return showFlag;
    }

    public void setShowFlag(String showFlag) {
        this.showFlag = showFlag;
    }

    public String getEnterCase() {
        return enterCase;
    }

    public void setEnterCase(String enterCase) {
        this.enterCase = enterCase;
    }

    public String getTitleCase() {
        return titleCase;
    }

    public void setTitleCase(String titleCase) {
        this.titleCase = titleCase;
    }

    public long getSysDate() {
        return sysDate;
    }

    public void setSysDate(long sysDate) {
        this.sysDate = sysDate;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getJumpNative() {
        return jumpNative;
    }

    public void setJumpNative(String jumpNative) {
        this.jumpNative = jumpNative;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }
}
