package com.dpad.offerwall.bean;

/**
 * Created by Rocklee on 2017-04-24.
 */

public class DPAdInfo {


    /**
     * adid : 2
     * title : CokePLAY
     * icon : https://cdn-ad-static.buzzvil.com/uploads/1489484200-1IKEE.png
     * action_description : [참여방법]
     - 앱을 설치하시면 포인트가 지급됩니다.

     [주의사항]
     - 이미 설치한 앱은 포인트가 지급되지 않습니다.
     - 3G/4G에서는 데이터 이용료가 발생할 수 있습니다.
     * description : 평생 잊지 못할 일생일대 짜릿한 기회!
     * package_name : kr.co.lotusport.cokehandsup
     * revenue_type : cpi
     * target_sex :
     * target_age_from : 12
     * target_age_to : 28
     * rwd : 11코인
     */

    private String adid;
    private String title;
    private String icon;
    private String action_description;
    private String description;
    private String package_name;
    private String revenue_type;
    private String target_sex;
    private String target_age_from;
    private String target_age_to;
    private String rwd;
    /**
     * revenue_type_str :
     */

    private String revenue_type_str;


    public boolean isParted() {
        return isParted;
    }

    public void setParted(boolean parted) {
        isParted = parted;
    }

    /**
     * adid : 1
     * nadid : 1150528
     * udid_required : 0
     * title : 여명 for Kakao
     * icon : https://cdn-ad-static.buzzvil.com/uploads/1489560794-V5KAU.png
     * action_description : [참여방법]
     - 앱을 설치하고 앱을 실행하시면 포인트가 지급됩니다.

     [주의사항]
     - 이미 설치한 앱은 포인트가 지급되지 않습니다.
     - 3G/4G에서는 데이터 이용료가 발생할 수 있습니다.
     * description : 밝아오는 여명처럼 눈부시고 찬란한 그래픽이 오픈 필드 가득 펼쳐진다!
     * package_name : com.kakaogames.lmzgplay
     * revenue_type : cpe
     * target_sex :
     * target_app :
     * target_relationship :
     * target_age_from :
     * target_age_to :
     * target_region :
     * target_carrier :
     * target_device_name :
     * rwd : 16코인
     * revenue_type_str : 실행형
     */
    private String partId ="";
    private String landing_url="";

    public String getLanding_url() {
        return landing_url;
    }

    public void setLanding_url(String landing_url) {
        this.landing_url = landing_url;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    private boolean isParted = false;


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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAction_description() {
        return action_description;
    }

    public void setAction_description(String action_description) {
        this.action_description = action_description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getRevenue_type() {
        return revenue_type;
    }

    public void setRevenue_type(String revenue_type) {
        this.revenue_type = revenue_type;
    }

    public String getTarget_sex() {
        return target_sex;
    }

    public void setTarget_sex(String target_sex) {
        this.target_sex = target_sex;
    }

    public String getTarget_age_from() {
        return target_age_from;
    }

    public void setTarget_age_from(String target_age_from) {
        this.target_age_from = target_age_from;
    }

    public String getTarget_age_to() {
        return target_age_to;
    }

    public void setTarget_age_to(String target_age_to) {
        this.target_age_to = target_age_to;
    }

    public String getRwd() {
        return rwd;
    }

    public void setRwd(String rwd) {
        this.rwd = rwd;
    }

    public String getRevenue_type_str() {
        return revenue_type_str;
    }

    public void setRevenue_type_str(String revenue_type_str) {
        this.revenue_type_str = revenue_type_str;
    }
}
