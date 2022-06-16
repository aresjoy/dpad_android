package com.dpad.offerwall.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class DPAdTabInfo implements Parcelable {
   public  String title;
    public String code;

    protected DPAdTabInfo(Parcel in) {
        title = in.readString();
        code = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(code);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DPAdTabInfo> CREATOR = new Creator<DPAdTabInfo>() {
        @Override
        public DPAdTabInfo createFromParcel(Parcel in) {
            return new DPAdTabInfo(in);
        }

        @Override
        public DPAdTabInfo[] newArray(int size) {
            return new DPAdTabInfo[size];
        }
    };
}
