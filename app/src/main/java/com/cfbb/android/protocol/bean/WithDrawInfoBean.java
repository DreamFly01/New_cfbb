package com.cfbb.android.protocol.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MrChang45 on 2016/4/5.
 * 提现初始化
 */
public class WithDrawInfoBean implements Parcelable {

    public String imageUrl;
    public String bankName;
    public String bankCardNum;
    public String tips;
    public String investedCanWithdraw;
    public String showCanWithdrawAmount;
    public String bankCardId;
    public String noInvestMin;
    public String noInvestFee;
    public String fastWithdrawFee;
    public String amount;
    public String bankCardCount;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeString(this.bankName);
        dest.writeString(this.bankCardNum);
        dest.writeString(this.tips);
        dest.writeString(this.investedCanWithdraw);
        dest.writeString(this.showCanWithdrawAmount);
        dest.writeString(this.bankCardId);
        dest.writeString(this.noInvestMin);
        dest.writeString(this.noInvestFee);
        dest.writeString(this.fastWithdrawFee);
        dest.writeString(this.amount);
        dest.writeString(this.bankCardCount);
    }

    public WithDrawInfoBean() {
    }

    protected WithDrawInfoBean(Parcel in) {
        this.imageUrl = in.readString();
        this.bankName = in.readString();
        this.bankCardNum = in.readString();
        this.tips = in.readString();
        this.investedCanWithdraw = in.readString();
        this.showCanWithdrawAmount = in.readString();
        this.bankCardId = in.readString();
        this.noInvestMin = in.readString();
        this.noInvestFee = in.readString();
        this.fastWithdrawFee = in.readString();
        this.amount = in.readString();
        this.bankCardCount = in.readString();
    }

    public static final Parcelable.Creator<WithDrawInfoBean> CREATOR = new Parcelable.Creator<WithDrawInfoBean>() {
        @Override
        public WithDrawInfoBean createFromParcel(Parcel source) {
            return new WithDrawInfoBean(source);
        }

        @Override
        public WithDrawInfoBean[] newArray(int size) {
            return new WithDrawInfoBean[size];
        }
    };
}
