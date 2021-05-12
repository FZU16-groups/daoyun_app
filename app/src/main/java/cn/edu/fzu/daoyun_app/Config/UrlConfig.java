package cn.edu.fzu.daoyun_app.Config;

public class UrlConfig {
    private static String ORIGION_URL = "http://47.98.151.20:8080/daoyun_service/";

    /**
     * 存放所有Url的配置文件
     */

    public enum UrlType {
        VER_LOGIN,
        PSD_LOGIN,
        Create_Class
    }

    public static String getUrl(UrlType urlType) {
        switch (urlType) {
            case VER_LOGIN:
                return ORIGION_URL + "login2";
            case PSD_LOGIN:
                return ORIGION_URL + "loginUser.do";
            case Create_Class:
                return ORIGION_URL + "insertCourses.do";
            default:
                return ORIGION_URL + "message?phone=";
        }
    }
}
