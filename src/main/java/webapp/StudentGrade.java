package webapp;

public class StudentGrade {

    private String instructorId;
    private int courseId;
    private int firstExam;
    private int secondExam;
    private int finalExam;
    private int midExam;
    private int quizzes;
    private int totalGrade;
    private StudentCourse course;

    public StudentGrade(String instructorId, int firstExam, int secondExam, int finalExam, int midExam, int quizzes, int totalGrade ,int courseId, StudentCourse course) {
        this.instructorId = instructorId;
        this.firstExam = firstExam;
        this.secondExam = secondExam;
        this.finalExam = finalExam;
        this.midExam = midExam;
        this.quizzes = quizzes;
        this.totalGrade = totalGrade;
        this.courseId =courseId;
        this.course = course;
    }
}
