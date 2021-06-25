package cn.edu.fzu.daoyun_app.Config;

public class UrlConfig {
    private static String ORIGION_URL = "http://47.98.151.20:8080/daoyun_service/";

    /**
     * 存放所有Url的配置文件
     */

    public enum UrlType {
        VER_LOGIN,
        PSD_LOGIN,
        Create_Class,
        JOIN_CLASS,
        JPINED_CLASS,
        CREATE_CLASS,
        PERMIT_JOIN_CLASS,
        JUDGE_SIGNIN,
        SEND_SIGNIN,
        STOP_SIGNIN,
        STUDENT_JUDGE_SIGNIN,
        STUDENT_SIGNIN,
        CLASS_STUDENT,
        SIGNIN_STUDENT,
    }

    public static String getUrl(UrlType urlType) {
        switch (urlType) {
            case VER_LOGIN:
                return ORIGION_URL + "login2";
            case PSD_LOGIN:
                return ORIGION_URL + "loginUser.do";
            case Create_Class:
                return ORIGION_URL + "insertCourses.do";
            case JOIN_CLASS:
                return ORIGION_URL + "insertPersonCourse.do";
            case JPINED_CLASS:
                return ORIGION_URL + "addedCourse.do";
            case CREATE_CLASS:
                return ORIGION_URL + "createdCourse.do";
            case PERMIT_JOIN_CLASS:
                return ORIGION_URL + "updateByCoursePrimaryKey.do";
            case JUDGE_SIGNIN:
                return ORIGION_URL + "JudgeSignIn.do";
            case SEND_SIGNIN:
                return ORIGION_URL + "SendSign.do";
            case STOP_SIGNIN:
                return ORIGION_URL+"StopSignIn.do";
            case STUDENT_JUDGE_SIGNIN:
                return ORIGION_URL+"JudgeSignInByCourseNumber.do";
            case STUDENT_SIGNIN:
                return ORIGION_URL+"insertSignInMessage.do";
            case CLASS_STUDENT:
                return ORIGION_URL+"selectStudentsByCourseNumber.do";
            case SIGNIN_STUDENT:
                return ORIGION_URL+"SignInInform.do";
            default:
                return ORIGION_URL + "message?phone=";
        }
    }
}
