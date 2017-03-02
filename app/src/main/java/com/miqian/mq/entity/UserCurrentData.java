package com.miqian.mq.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Jackie on 2015/9/30.
 */
public class UserCurrentData implements Serializable{

    private UserRedeem userRedeem;
    private UserCurrent userCurrent;


    public UserRedeem getUserRedeem() {
        return userRedeem;
    }

    public void setUserRedeem(UserRedeem userRedeem) {
        this.userRedeem = userRedeem;
    }

    public UserCurrent getUserCurrent() {
        return userCurrent;
    }

    public void setUserCurrent(UserCurrent userCurrent) {
        this.userCurrent = userCurrent;
    }

    /**
     * 用户秒钱宝金额数据
     */
    public class UserCurrent implements Serializable{
        private BigDecimal prnAmt;                          //在投本金
        private BigDecimal yesterdayInterest;               //昨日收益
        private BigDecimal regIncome;                       //在投收益
        private BigDecimal yearInterest;                    //年化收益

        public BigDecimal getPrnAmt() {
            return prnAmt;
        }

        public void setPrnAmt(BigDecimal prnAmt) {
            this.prnAmt = prnAmt;
        }

        public BigDecimal getYesterdayInterest() {
            return yesterdayInterest;
        }

        public void setYesterdayInterest(BigDecimal yesterdayInterest) {
            this.yesterdayInterest = yesterdayInterest;
        }

        public BigDecimal getRegIncome() {
            return regIncome;
        }

        public void setRegIncome(BigDecimal regIncome) {
            this.regIncome = regIncome;
        }

        public BigDecimal getYearInterest() {
            return yearInterest;
        }

        public void setYearInterest(BigDecimal yearInterest) {
            this.yearInterest = yearInterest;
        }
    }

    /**
     * 可赎回数据
     */
    public class UserRedeem implements Serializable{
        private int curDayCount;                            //当日已赎回次数
        private int lmtDayCount;                            //赎回限制每日次数
        private BigDecimal curDayAmt;                       //当日已赎回总额
        private BigDecimal curMonthAmt;                     //当月已赎回总额
        private BigDecimal curDayResidue;                   //当日剩余可赎回额度
        private BigDecimal lmtDayAmt;                       //赎回限制每日金额
        private BigDecimal lmtMonthAmt;                     //赎回限制每月金额
        private String warmPrompt;                          //赎回温馨提示

        public int getCurDayCount() {
            return curDayCount;
        }

        public void setCurDayCount(int curDayCount) {
            this.curDayCount = curDayCount;
        }

        public int getLmtDayCount() {
            return lmtDayCount;
        }

        public void setLmtDayCount(int lmtDayCount) {
            this.lmtDayCount = lmtDayCount;
        }

        public BigDecimal getCurDayAmt() {
            return curDayAmt;
        }

        public void setCurDayAmt(BigDecimal curDayAmt) {
            this.curDayAmt = curDayAmt;
        }

        public BigDecimal getCurMonthAmt() {
            return curMonthAmt;
        }

        public void setCurMonthAmt(BigDecimal curMonthAmt) {
            this.curMonthAmt = curMonthAmt;
        }

        public BigDecimal getCurDayResidue() {
            return curDayResidue;
        }

        public void setCurDayResidue(BigDecimal curDayResidue) {
            this.curDayResidue = curDayResidue;
        }

        public BigDecimal getLmtDayAmt() {
            return lmtDayAmt;
        }

        public void setLmtDayAmt(BigDecimal lmtDayAmt) {
            this.lmtDayAmt = lmtDayAmt;
        }

        public BigDecimal getLmtMonthAmt() {
            return lmtMonthAmt;
        }

        public void setLmtMonthAmt(BigDecimal lmtMonthAmt) {
            this.lmtMonthAmt = lmtMonthAmt;
        }

        public String getWarmPrompt() {
            return warmPrompt;
        }

        public void setWarmPrompt(String warmPrompt) {
            this.warmPrompt = warmPrompt;
        }
    }

}