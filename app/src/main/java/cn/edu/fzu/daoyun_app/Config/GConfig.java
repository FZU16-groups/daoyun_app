package cn.edu.fzu.daoyun_app.Config;

public class GConfig {
    private static String USER_TOKEN="";

    public static String getUserToken() {
        return USER_TOKEN;
    }

    public static void setUserToken(String userToken) {
        USER_TOKEN = userToken;
    }
}
