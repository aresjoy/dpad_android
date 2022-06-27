package com.dpad.offerwall.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class DPAdTabInfo implements Parcelable {
    public String title = "";
    public List<String> type_list = new ArrayList<>();

    protected DPAdTabInfo(Parcel in) {
        title = in.readString();
        type_list = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeStringList(type_list);
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
