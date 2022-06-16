package com.dpad.offerwall.bean;

/**
 * Created by Rocklee on 2017-04-25.
 */

public class HistoryInfo {
    private String title ="";
    private String time="";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRwd() {
        return rwd;
    }

    public void setRwd(String rwd) {
        this.rwd = rwd;
    }

    private String rwd="";
}
