
package com.dpad.offerwall.bean;

public class PartInfo {
    private String part_id ="";
    private String campaign_id="";
    private String landurl ="";

    public String getLandingUrl() {
        return landurl;
    }

    public void setLandingUrl(String landurl) {
        this.landurl = landurl;
    }

    public String getPart_id() {
        return part_id;
    }

    public void setPart_id(String part_id) {
        this.part_id = part_id;
    }

    public String getCampaign_id() {
        return campaign_id;
    }

    public void setCampaign_id(String campaign_id) {
        this.campaign_id = campaign_id;
    }
}
