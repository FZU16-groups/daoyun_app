package cn.edu.fzu.daoyun_app;

public class SignInHistory {
    private String signID;
    private String startTime;
    private String endTime;
    private String lng;
    private String lat;
    private boolean isFinish;
    private String dateType;
    private String conDate;
    private String signinpeople;
    private String startminyte;
    private String value;
    public SignInHistory(String signID, String startTime, String endTime, String lng, String lat, boolean isFinish,String value) {
        this.signID = signID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lat = lat;
        this.lng = lng;
        this.isFinish = isFinish;
        this.value=value;
    }
    public SignInHistory(String signID, String startTime, String endTime, String lng, String lat, boolean isFinish,String signinpeople,String startminyte) {
        this.signID = signID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lat = lat;
        this.lng = lng;
        this.isFinish = isFinish;
        this.signinpeople=signinpeople;
        this.startminyte=startminyte;
    }


    public String getSignID() {
        return signID;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getLng() {
        return lng;
    }

    public String getLat() {
        return lat;
    }

    public boolean getIsFinish() {
        return isFinish;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getConDate() {
        return conDate;
    }

    public void setConDate(String conDate) {
        this.conDate = conDate;
    }

    public String getSigninpeople() {
        return signinpeople;
    }

    public void setSigninpeople(String signinpeople) {
        this.signinpeople = signinpeople;
    }

    public String getStartminyte() {
        return startminyte;
    }

    public void setStartminyte(String startminyte) {
        this.startminyte = startminyte;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
