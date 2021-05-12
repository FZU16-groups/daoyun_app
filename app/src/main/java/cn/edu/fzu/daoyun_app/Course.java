package cn.edu.fzu.daoyun_app;

public class Course {
    private int imageId = -1;
    private String imgFilePath = "";
    //班课号
    private String classId;
    //课程名称
    private String courseName;
    private String teacherName;
    private String className;
    public String teacherPhone;
    private String courseTerm;

    public Course(int imageId, String courseName, String teacherName, String className, String classId){
        this.classId = classId;
        this.imageId = imageId;
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.className = className;
    }

    public Course(String imgFilePath, String courseName, String teacherName, String className, String classId){
        this.classId = classId;
        this.imgFilePath = imgFilePath;
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.className = className;
    }
    public Course(int imageId,String  courseTerm, String courseName, String classId){
        this.imageId = imageId;
        this.classId = classId;
        this. courseTerm= courseTerm;
        this.courseName = courseName;
    }
    public String getTeacherPhone() {
        return teacherPhone;
    }
    public String getCourseTerm() {
        return  courseTerm;
    }

    public String getClassId() {
        return classId;
    }

    public String getImgFilePath(){
        return imgFilePath;
    }

    public int getImageId(){
        return imageId;
    }

    public String getCourseName(){
        return courseName;
    }

    public String getTeacherName(){
        return teacherName;
    }

    public String getClassName(){
        return className;
    }
}