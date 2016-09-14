package com.cfbb.android.protocol.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dell on 2016/4/7.
 * 自动投标Model
 */
public class AutoInvestInfoBean implements Parcelable {

    public int auto_type;
    public String auto_amount;
    public String rate_begin;
    public String rate_end;
    public String day_begin;
    public String day_end;
    public String repayMode_id;
    public int is_open;
    public int day_unit;

    public AutoInvestInfoBean()
    {

    }

    protected AutoInvestInfoBean(Parcel in) {
        auto_type = in.readInt();
        auto_amount = in.readString();
        rate_begin = in.readString();
        rate_end = in.readString();
        day_begin = in.readString();
        day_end = in.readString();
        repayMode_id = in.readString();
        is_open = in.readInt();
        day_unit = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(auto_type);
        dest.writeString(auto_amount);
        dest.writeString(rate_begin);
        dest.writeString(rate_end);
        dest.writeString(day_begin);
        dest.writeString(day_end);
        dest.writeString(repayMode_id);
        dest.writeInt(is_open);
        dest.writeInt(day_unit);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AutoInvestInfoBean> CREATOR = new Creator<AutoInvestInfoBean>() {
        @Override
        public AutoInvestInfoBean createFromParcel(Parcel in) {
            return new AutoInvestInfoBean(in);
        }

        @Override
        public AutoInvestInfoBean[] newArray(int size) {
            return new AutoInvestInfoBean[size];
        }
    };
}
