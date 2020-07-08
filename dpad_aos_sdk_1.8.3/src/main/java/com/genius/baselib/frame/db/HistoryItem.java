package com.genius.baselib.frame.db;

/**
 * Created by Rocklee on 2017-05-15.
 */

public class HistoryItem {

    private String adid = "";
    private String title = "";
    private String action_str="";
    private String partId ="";

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getAdid() {
        return adid;
    }

    public void setAdid(String adid) {
        this.adid = adid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAction_str() {
        return action_str;
    }

    public void setAction_str(String action_str) {
        this.action_str = action_str;
    }
}
