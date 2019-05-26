package com.github.binarywang.demo.wx.mp.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class VipEntity {
    private String vipNo; // ==> cVipno

    private String vipName; // ==> cVipName

    private Timestamp validStartTime; // ==> dValidStart

    private Timestamp validEndTime; // ==> dValidEnd

    private BigDecimal integration; // ==> fCurValue

    private String storeNo; // ==> cStoreNo

    private String storeName; // ==> cStore

    private BigDecimal balance; // ==> fMoney_Left

    private String openID; // ==> cWeixinId


    public String getVipNo() {
        return vipNo;
    }

    public void setVipNo(String vipNo) {
        this.vipNo = vipNo;
    }

    public BigDecimal getIntegration() {
        return integration;
    }

    public void setIntegration(BigDecimal integration) {
        this.integration = integration;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getVipName() {

        return vipName;
    }

    public void setVipName(String vipName) {
        this.vipName = vipName;
    }

    public Timestamp getValidStartTime() {
        return validStartTime;
    }

    public void setValidStartTime(Timestamp validStartTime) {
        this.validStartTime = validStartTime;
    }

    public Timestamp getValidEndTime() {
        return validEndTime;
    }

    public void setValidEndTime(Timestamp validEndTime) {
        this.validEndTime = validEndTime;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }



    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }


    public VipEntity(String vipNo, String vipName, Timestamp validStartTime, Timestamp validEndTime, BigDecimal integration, String storeNo, String storeName, BigDecimal balance, String openID) {
        this.vipNo = vipNo;
        this.vipName = vipName;
        this.validStartTime = validStartTime;
        this.validEndTime = validEndTime;
        this.integration = integration == null ? BigDecimal.ZERO : integration;
        this.storeNo = storeNo;
        this.storeName = storeName;
        this.balance = balance == null ? BigDecimal.ZERO : balance;
        this.openID = openID;
    }

    public VipEntity() {
    }

    @Override
    public String toString() {
        return "VipEntity{" +
                "vipNo='" + vipNo + '\'' +
                ", vipName='" + vipName + '\'' +
                ", validStartTime=" + validStartTime +
                ", validEndTime=" + validEndTime +
                ", integration=" + integration +
                ", storeNo='" + storeNo + '\'' +
                ", storeName='" + storeName + '\'' +
                ", balance=" + balance +
                ", openID='" + openID + '\'' +
                '}';
    }
}
