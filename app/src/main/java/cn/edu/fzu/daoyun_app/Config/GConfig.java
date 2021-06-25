package cn.edu.fzu.daoyun_app.Config;

import java.util.ArrayList;
import java.util.List;

public class GConfig {
    private static String USER_TOKEN="";
    public static  List<String> CLASSNAMES = new ArrayList<String>();

    public static String getUserToken() {
        return USER_TOKEN;
    }

    public static void setUserToken(String userToken) {
        USER_TOKEN = userToken;
    }

}
