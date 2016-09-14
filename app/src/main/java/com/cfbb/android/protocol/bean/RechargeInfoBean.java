package com.cfbb.android.protocol.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dell on 2016/4/6.
 * 充值初始化model
 */
public class RechargeInfoBean implements Parcelable {
    public String noAgree;
    public String imageUrl;
    public String bankName;
    public String bankNum;
    public String accountName;
    public int rechargeState;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.noAgree);
        dest.writeString(this.imageUrl);
        dest.writeString(this.bankName);
        dest.writeString(this.bankNum);
        dest.writeString(this.accountName);
        dest.writeInt(this.rechargeState);
    }

    public RechargeInfoBean() {
    }

    protected RechargeInfoBean(Parcel in) {
        this.noAgree = in.readString();
        this.imageUrl = in.readString();
        this.bankName = in.readString();
        this.bankNum = in.readString();
        this.accountName = in.readString();
        this.rechargeState = in.readInt();
    }

    public static final Parcelable.Creator<RechargeInfoBean> CREATOR = new Parcelable.Creator<RechargeInfoBean>() {
        @Override
        public RechargeInfoBean createFromParcel(Parcel source) {
            return new RechargeInfoBean(source);
        }

        @Override
        public RechargeInfoBean[] newArray(int size) {
            return new RechargeInfoBean[size];
        }
    };
}
