package webapp;

public class StudentCourse {

    private int id;
    private String courseName;
    private String instructorName;
    private int examType;

    public StudentCourse(int id, String courseName ,String instructorName,int examType) {
        this.id = id;
        this.courseName = courseName;
        this.instructorName =instructorName;
        this.examType=examType;

    }
}
