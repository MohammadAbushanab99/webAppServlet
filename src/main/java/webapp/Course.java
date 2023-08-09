package webapp;

public class Course {
    private int id;
    private String courseName;
    private String instructorId;
    private int numberOfStudents;
    private int gradeType;
    private int examType;

    public Course(int id, String courseName, int numberOfStudents, int gradeType, int examType ,String instructorId) {
        this.id = id;
        this.courseName = courseName;
        this.numberOfStudents = numberOfStudents;
        this.gradeType = gradeType;
        this.examType = examType;
        this.instructorId =instructorId;

    }

    public int getId() {
        return id;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public int getGradeType() {
        return gradeType;
    }

    public int getExamType() {
        return examType;
    }

    public void setGradeType(int gradeType) {
        this.gradeType = gradeType;
    }

    public void setExamType(int examType) {
        this.examType = examType;
    }
}
