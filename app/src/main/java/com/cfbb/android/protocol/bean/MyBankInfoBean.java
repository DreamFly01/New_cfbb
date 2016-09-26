package com.cfbb.android.protocol.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dell on 2016/4/5.
 */
public class MyBankInfoBean implements Parcelable {

    public String bankCardId;
    public String imageUrl;
    public String bankNum;
    public String accountBalance;
    public String holdingMoney;
    public String investingMoney;
    public int    canDelete;
    public String hint;
    public String realName;
    public String bankName;
    public String inUseMoeny;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bankCardId);
        dest.writeString(this.imageUrl);
        dest.writeString(this.bankNum);
        dest.writeString(this.accountBalance);
        dest.writeString(this.holdingMoney);
        dest.writeString(this.investingMoney);
        dest.writeInt(this.canDelete);
        dest.writeString(this.hint);
        dest.writeString(this.realName);
        dest.writeString(this.bankName);
        dest.writeString(this.inUseMoeny);
    }

    public MyBankInfoBean() {
    }

    protected MyBankInfoBean(Parcel in) {
        this.bankCardId = in.readString();
        this.imageUrl = in.readString();
        this.bankNum = in.readString();
        this.accountBalance = in.readString();
        this.holdingMoney = in.readString();
        this.investingMoney = in.readString();
        this.canDelete = in.readInt();
        this.hint = in.readString();
        this.realName = in.readString();
        this.bankName = in.readString();
        this.inUseMoeny= in.readString();
    }

    public static final Parcelable.Creator<MyBankInfoBean> CREATOR = new Parcelable.Creator<MyBankInfoBean>() {
        @Override
        public MyBankInfoBean createFromParcel(Parcel source) {
            return new MyBankInfoBean(source);
        }

        @Override
        public MyBankInfoBean[] newArray(int size) {
            return new MyBankInfoBean[size];
        }
    };
}

